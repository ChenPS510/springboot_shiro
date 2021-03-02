package com.mk.shiro.util;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * @Author: mk.chen
 * @Date: 2020/1/16 10:19
 * @Description:
 */
public class CheckPwd {
    /**
     * 检验密码过程demo
     *
     * @param args
     */
    public static void main(String[] args) {
        // 1.原始密码 aa123456
        String originPwd = "aa123456";
        // 2.经过md5加密处理，前端需要对原始密码做MD5处理，再传给后端
        String md5Pwd = DigestUtils.md5DigestAsHex(originPwd.getBytes());
        // 2.对md5Pwd加盐加密处理，得到数据库存的密文密码
        String salt = "123";
        String dbPwd = new Md5Hash(md5Pwd, new String(salt.getBytes(), StandardCharsets.UTF_8), 2).toString();

        System.out.println(md5Pwd + " --> " + dbPwd);
    }
}
