package com.zzq.system.service.impl;

import com.zzq.common.constant.Constants;
import com.zzq.common.core.domain.AjaxResult;
import com.zzq.common.utils.StringUtils;
import com.zzq.common.utils.TreeUtils;
import com.zzq.framework.domain.dto.LoginUserDTO;
import com.zzq.framework.utils.SecurityUtils;
import com.zzq.system.domain.MetaVO;
import com.zzq.system.domain.RouterVO;
import com.zzq.system.domain.SysMenuTree;
import com.zzq.system.mapper.SysMenuMapper;
import com.zzq.system.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @Project : zzq-demo-backend
 * @Time    : 2026-01-10 22:43
 * @Author  : ZZQ
 * @Desc    : 针对表【sys_menu(菜单权限表)】的数据库操作Service实现
 */
@Service
public class SysMenuServiceImpl implements SysMenuService {
    public static final Long MENU_ROOT_ID = 0L;

    @Autowired
    private SysMenuMapper sysMenuMapper;

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @Override
    public AjaxResult getRouters() {
        LoginUserDTO loginUser = SecurityUtils.getLoginUser();
        List<SysMenuTree> menus;
        if (loginUser.getUser().isAdmin()) {
            menus = sysMenuMapper.selectAllMenus();
        } else {
            Long userId = SecurityUtils.getUserId();
            menus = sysMenuMapper.selectMenusByUserId(userId);
        }

        List<SysMenuTree> list = TreeUtils.listToTree(menus, MENU_ROOT_ID);
        List<RouterVO> routers = buildRouterVO(list);


        return AjaxResult.success(routers);
    }

    /**
     * 构建前端路由所需要的信息
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    private List<RouterVO> buildRouterVO(List<SysMenuTree> menus) {
        List<RouterVO> routers = new LinkedList<RouterVO>();
        for (SysMenuTree menu : menus) {
            RouterVO router = new RouterVO();
            router.setHidden(menu.getVisible() == 0);
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setQuery(menu.getQuery());
            router.setMeta(new MetaVO(menu.getName(), menu.getIcon(), menu.getCacheFlag() == 0, menu.getPath()));
            List<SysMenuTree> cMenus = menu.getChildren();

            if (!CollectionUtils.isEmpty(cMenus) && Constants.TYPE_CONTENT.equals(menu.getType()))
            {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildRouterVO(cMenus));
            }
            else if (isMenuFrame(menu))
            {
                router.setMeta(null);
                List<RouterVO> childrenList = new ArrayList<RouterVO>();
                RouterVO children = new RouterVO();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(getRouteName(menu.getRouteName(), menu.getPath()));
                children.setMeta(new MetaVO(menu.getName(), menu.getIcon(), menu.getCacheFlag() == 0, menu.getPath()));
                children.setQuery(menu.getQuery());
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            else if (Objects.equals(menu.getParentId(), MENU_ROOT_ID)
                    && isInnerLink(menu))
            {
                router.setMeta(new MetaVO(menu.getName(), menu.getIcon()));
                router.setPath("/");
                List<RouterVO> childrenList = new ArrayList<RouterVO>();
                RouterVO children = new RouterVO();
                String routerPath = innerLinkReplaceEach(menu.getPath());
                children.setPath(routerPath);
                children.setComponent(Constants.INNER_LINK);
                children.setName(getRouteName(menu.getRouteName(), routerPath));
                children.setMeta(new MetaVO(menu.getName(), menu.getIcon(), menu.getPath()));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }

        return routers;
    }

    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    private String getRouteName(SysMenuTree menu)
    {
        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame(menu))
        {
            return "";
        }
        return getRouteName(menu.getRouteName(), menu.getPath());
    }

    /**
     * 获取路由名称，如没有配置路由名称则取路由地址
     *
     * @param name 路由名称
     * @param path 路由地址
     * @return 路由名称（驼峰格式）
     */
    public String getRouteName(String name, String path)
    {
        // 优先用name，其次才用path
        String routerName = StringUtils.isNotBlank(name) ? name : path;
        return StringUtils.capitalize(routerName);
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMenuFrame(SysMenuTree menu)
    {
        return Objects.equals(menu.getParentId(), MENU_ROOT_ID)
                && Constants.TYPE_MENU.equals(menu.getType())
                && Objects.equals(menu.getFrameFlag(), Constants.NO_FRAME);
    }


    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenuTree menu)
    {
        String routerPath = menu.getPath();
        // 内链打开外网方式
        if (menu.getParentId().intValue() != MENU_ROOT_ID && isInnerLink(menu))
        {
            routerPath = innerLinkReplaceEach(routerPath);
        }
        // 非外链并且是一级目录（类型为目录）
        if ( Objects.equals(menu.getParentId(), MENU_ROOT_ID)
                && Constants.TYPE_CONTENT.equals(menu.getType())
                && Constants.NO_FRAME.equals(menu.getFrameFlag()))
        {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame(menu))
        {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 是否为内链组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isInnerLink(SysMenuTree menu)
    {
        // 不是外链 && 路径是链接的形式
        return menu.getFrameFlag().equals(Constants.NO_FRAME) && StringUtils.isHttp(menu.getPath());
    }

    /**
     * 内链域名特殊字符替换
     *
     * @return 替换后的内链域名
     */
    public String innerLinkReplaceEach(String path)
    {
        return StringUtils.replaceEach(
                path,
                new String[] { Constants.HTTP, Constants.HTTPS, Constants.WWW, ".", ":" },
                new String[] { "", "", "", "/", "/" }
        );
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(SysMenuTree menu)
    {
        String component = Constants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu))
        {
            component = menu.getComponent();
        }
        else if (StringUtils.isEmpty(menu.getComponent())
                && !Objects.equals(menu.getParentId(), MENU_ROOT_ID)
                && isInnerLink(menu))
        {
            component = Constants.INNER_LINK;
        }
        else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu))
        {
            component = Constants.PARENT_VIEW;
        }
        return component;
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(SysMenuTree menu)
    {
        return !Objects.equals(menu.getParentId(), MENU_ROOT_ID)
                && Constants.TYPE_CONTENT.equals(menu.getType());
    }
}
