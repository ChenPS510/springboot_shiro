package com.mk.shiro.exception;

/**
 * @Author: mk.chen
 * @Date: 2020/1/2 11:21
 * @Description:
 */
public class ResultCode {
    public ResultCode() {
    }

    public static enum Codes implements CodeEnum {
        PARRAMS_ERROR(20000, "操作失败"),
        NOT_FIND(404, "接口不存在"),
        NOT_ALLOWED(405, "不支持该请求"),
        SUCCESS(10000, "成功"),
        NOT_LOGIN(30001, "未登录"),
        DATATIMEOUT(3002, "数据已过期"),
        NOT_REGIST(3003, "未注册"),
        REGISTED(3004, "账号已注册"),
        UNKNOW(50000, "内容未找到"),
        BUSINESS_ERROR(60000, "处理异常"),
        FILE_NOT_FIND(70001, "文件不存在"),
        SUBMIT_REPEAT(90001, "已经提交请求，不能重复提交"),
        SQL_INJECTION(90002, "请求数据存在安全风险"),
        SINLE_RATE_LIMIT(90003, "请求次数已达上限，请稍后重试");

        private Integer code;
        private String message;

        private Codes(Integer code, String message) {
            this.code = code;
            this.message = message;
        }

        @Override
        public Integer getCode() {
            return this.code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        @Override
        public String getMessage() {
            return this.message;
        }

        public void setMesssage(String messsage) {
            this.message = messsage;
        }
    }
}
