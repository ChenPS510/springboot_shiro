package com.mk.shiro.bean.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @Author: mk.chen
 * @Date: 2020/1/9 20:42
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginReq {
    @NotBlank
    private String phone;
    @NotBlank
    private String password;
    @NotBlank
    private String requestId;
    @NotBlank
    private String verificationCode;
}
