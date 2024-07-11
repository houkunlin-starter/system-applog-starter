package com.houkunlin.system.applog;

import java.io.Serializable;

/**
 * 获取当前用户信息
 *
 * @author HouKunLin
 */
public interface ICurrentUser {
    /**
     * 获取当前登录用户ID
     *
     * @return 当前登录用户ID
     */
    Serializable currentUserId();
}
