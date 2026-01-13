package com.zzq.framework.aop;

import com.zzq.common.annotation.DataScope;
import com.zzq.common.core.domain.BaseEntity;
import com.zzq.common.constant.Constants;
import com.zzq.common.utils.StringUtils;
import com.zzq.common.utils.ConvertUtils;
import com.zzq.framework.domain.dto.LoginUserDTO;
import com.zzq.framework.domain.dto.SysRoleDTO;
import com.zzq.framework.domain.entity.SysUser;
import com.zzq.framework.utils.SecurityUtils;
import com.zzq.framework.security.context.PermissionContextHolder;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 数据过滤处理
 *
 * @author ruoyi
 */
@Aspect
@Component
public class DataScopeAspect
{
    /**
     * 全部数据权限
     */
    public static final Integer DATA_SCOPE_ALL = 1;

    /**
     * 自定数据权限
     */
    public static final Integer DATA_SCOPE_CUSTOM = 2;

    /**
     * 部门数据权限
     */
    public static final Integer DATA_SCOPE_DEPT = 3;

    /**
     * 部门及以下数据权限
     */
    public static final Integer DATA_SCOPE_DEPT_AND_CHILD = 4;

    /**
     * 仅本人数据权限
     */
    public static final Integer DATA_SCOPE_SELF = 5;

    /**
     * 数据权限过滤关键字
     */
    public static final String DATA_SCOPE = "dataScope";

    @Before("@annotation(controllerDataScope)")
    public void doBefore(JoinPoint point, DataScope controllerDataScope) throws Throwable {
        // 清空params.dataScope参数
        clearDataScope(point);
        handleDataScope(point, controllerDataScope);
    }

    protected void handleDataScope(final JoinPoint joinPoint, DataScope controllerDataScope) {
        // 获取当前的用户
        LoginUserDTO loginUser = SecurityUtils.getLoginUser();
        if (loginUser != null)
        {
            SysUser currentUser = loginUser.getUser();
            // 如果是超级管理员，则不过滤数据
            if (currentUser != null && !currentUser.isAdmin())
            {
                // 这里的 permission 来自 @PreAuthorize 注解
                String permission = StringUtils.defaultIfEmpty(controllerDataScope.permission(), PermissionContextHolder.getContext());
                dataScopeFilter(joinPoint, loginUser, controllerDataScope.deptAlias(), controllerDataScope.userAlias(), permission);
            }
        }
    }

    /**
     * 数据范围过滤
     *
     * @param joinPoint 切点
     * @param loginUser 登录用户
     * @param deptAlias 部门别名
     * @param userAlias 用户别名
     * @param permission 权限字符串
     */
    public static void dataScopeFilter(JoinPoint joinPoint, LoginUserDTO loginUser, String deptAlias, String userAlias, String permission) {
        StringBuilder sqlString = new StringBuilder();
        List<Integer> conditions = new ArrayList<Integer>();
        List<String> scopeCustomIds = new ArrayList<String>();
        // 遍历所有角色，找到其中为 自定权限 的角色
        loginUser.getRoles().forEach(role -> {
            // 判断条件：自定权限+角色正常+有菜单权限 => 做角色的合并
            if (Objects.equals(role.getDataScope(), DATA_SCOPE_CUSTOM)
                    && Objects.equals(role.getStatus(), Constants.NORMAL)
                    && (StringUtils.isEmpty(permission) || StringUtils.containsAny(role.getPermissions(), ConvertUtils.toStrArray(permission)))
            ) {
                scopeCustomIds.add(ConvertUtils.toStr(role.getId()));
            }
        });

        // 再次遍历用户的所有角色，拼接SQL语句
        for (SysRoleDTO role : loginUser.getRoles()) {
            Integer dataScope = role.getDataScope();
            // 当角色状态为禁用或者这个dataScope类型被处理过了后，就不再处理
            if (conditions.contains(dataScope)
                    || Objects.equals(role.getStatus(), Constants.DISABLE)
            ) {
                continue;
            }
            // 当该角色的权限的不包含当前权限时，不处理
            if (StringUtils.isNotBlank(permission)
                    && !StringUtils.containsAny(role.getPermissions(), ConvertUtils.toStrArray(permission))) {
                continue;
            }
            // 角色权限为全部数据权限
            if (DATA_SCOPE_ALL.equals(dataScope)) {
                // 清空sqlString
                sqlString = new StringBuilder();
                conditions.add(dataScope);
                // 已经有全部数据权限了，直接退出循环
                break;
            }
            else if (DATA_SCOPE_CUSTOM.equals(dataScope)) {
                if (scopeCustomIds.size() > 1) {
                    // 多个自定数据权限使用in查询，避免多次拼接。
                    sqlString.append(String.format(" OR %s.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id in (%s) ) ", deptAlias, String.join(",", scopeCustomIds)));
                }
                else {
                    // 单个自定义数据权限用=查询
                    sqlString.append(String.format(" OR %s.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = %s ) ", deptAlias, role.getId()));
                }
            }
            // 本部门数据
            else if (DATA_SCOPE_DEPT.equals(dataScope)) {
                sqlString.append(String.format(" OR %s.dept_id = %s ", deptAlias, loginUser.getDeptId()));
            }
            // 本部门及子部门
            else if (DATA_SCOPE_DEPT_AND_CHILD.equals(dataScope)) {
                sqlString.append(String.format(" OR %s.dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = %s or find_in_set( %s , ancestors ) )", deptAlias, loginUser.getDeptId(), loginUser.getDeptId()));
            }
            // 仅本人数据权限
            else if (DATA_SCOPE_SELF.equals(dataScope)) {
                if (StringUtils.isNotBlank(userAlias)) {
                    sqlString.append(String.format(" OR %s.user_id = %s ", userAlias, loginUser.getUserId()));
                }
                else {
                    // 数据权限为仅本人且没有userAlias别名不查询任何数据
                    sqlString.append(String.format(" OR %s.dept_id = 0 ", deptAlias));
                }
            }
            conditions.add(dataScope);
        }

        // 角色都不包含传递过来的权限字符，这个时候sqlString也会为空，所以要限制一下，不查询任何数据
        if (CollectionUtils.isEmpty(conditions)) {
            sqlString.append(String.format(" OR %s.dept_id = 0 ", deptAlias));
        }

        if (StringUtils.isNotBlank(sqlString.toString())) {
            Object params = joinPoint.getArgs()[0];
            // 模式变量，在类型检查时直接提取对象的值
            if (params instanceof BaseEntity baseEntity) {
                baseEntity.getParams().put(DATA_SCOPE, " AND (" + sqlString.substring(4) + ")");
            }
        }
    }

    /**
     * 拼接权限sql前先清空params.dataScope参数防止注入
     */
    private void clearDataScope(final JoinPoint joinPoint) {
        Object params = joinPoint.getArgs()[0];
        if (params instanceof BaseEntity baseEntity) {
            baseEntity.getParams().put(DATA_SCOPE, "");
        }
    }
}
