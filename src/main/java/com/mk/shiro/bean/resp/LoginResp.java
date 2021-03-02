package com.mk.shiro.bean.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: mk.chen
 * @Date: 2020/1/10 14:41
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResp {

    private String token;
    private UserInfo userInfo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private Integer id;
        private String name;
        private Integer roleId;
        private String roleName;

        /**
         * 判断当前用户是否是超管
         */
        private Boolean isRoot;
    }
}

