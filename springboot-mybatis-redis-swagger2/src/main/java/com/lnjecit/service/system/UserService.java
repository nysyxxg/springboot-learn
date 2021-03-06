package com.lnjecit.service.system;

import com.lnjecit.common.base.BaseService;
import com.lnjecit.entity.system.User;

/**
 * Create by lnj
 * create time: 2018-05-13 22:34:10
 */
public interface UserService extends BaseService<User> {

    /**
     * 用户名是否存在
     *
     * @param username 用户名
     * @return
     */
    boolean existUsername(String username);

    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * @return
     */
    User getByUsername(String username);

    /**
     * 更新用最后登录信息
     */
    void updateLastLoginInfo();

    /**
     * 为用户选择关联的角色
     *
     * @param userId  用户id
     * @param roleIds 关联角色的id数组
     * @return
     */
    void selectRoles(Long userId, Long[] roleIds) throws Exception;
}
