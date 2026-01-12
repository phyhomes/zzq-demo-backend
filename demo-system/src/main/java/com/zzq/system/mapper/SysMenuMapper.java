package com.zzq.system.mapper;


import com.zzq.system.domain.SysMenuTree;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Project : zzq-demo-backend
 * @Time    : 2026-01-10 22:43
 * @Author  : ZZQ
 * @Desc    : 针对表【sys_menu(菜单权限表)】的数据库操作Mapper
 */
@Mapper
public interface SysMenuMapper {


    List<SysMenuTree> selectAllMenus();

    List<SysMenuTree> selectMenusByUserId(Long userId);
}
