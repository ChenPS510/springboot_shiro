package com.mk.shiro.service;

import com.mk.shiro.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @author mk.chen
 */
@Service
public class AuthService {

    /**
     * 定义验证码字符.去除了O和I等容易混淆的字母
     */
    public static final char[] ALPHA = {'2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'G', 'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    @Autowired
    private CacheService cacheService;

    /**
     * 根据每次请求的requestId获取验证码，存入redis
     *
     * @param requestId
     * @return
     */
    public String getCode(String requestId) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(ALPHA[new Random().nextInt(ALPHA.length)]);
        }
        String code = sb.toString();
        Cache validateCodeCache = cacheService.getValidateCodeCache();
        validateCodeCache.put(requestId, code);
        System.out.println("验证码：" + code);
        return code;
    }

    /**
     * 校验该requestId对应的验证码是否正确，正确后在redis中删除验证码
     *
     * @param code
     * @param requestId
     */
    public void validateCode(String code, String requestId) {
        Cache validateCodeCache = cacheService.getValidateCodeCache();
        String existCode = validateCodeCache.get(requestId, String.class);
        if (!code.equalsIgnoreCase(existCode)) {
            throw new BusinessException("api.error.code.auth.code_error", "api.error.message.auth.code_error", true);
        }
        validateCodeCache.evict(requestId);
    }
}
