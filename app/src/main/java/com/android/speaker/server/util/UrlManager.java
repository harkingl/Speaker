package com.android.speaker.server.util;

public class UrlManager {
//    private static final String BASE_URL = "http://47.98.226.152:8080";
    private static final String BASE_URL = "https://www.dongbudong.top/jing_chat_api";
    public static String formatUrl(String url) {
        if(url == null || "".equals(url)) {
            return "";
        }
        return url.startsWith("http") ? url : BASE_URL + url;
    }

    /**
     * 修改密码
     */
    public static final String USER_MODIFY_PWD = "/api/v1/user/modify_pwd";

    /**
     * 注册
     */
    public static final String USER_REGISTER = "/api/v1/user/register";
    /**
     * 登录
     */
    public static final String USER_LOGIN = "/api/v1/user/login";

    /**
     * 获取验证码
     */
    public static final String GET_CAPTCHA = "/api/v1/third_party/send_msg";

    /**
     * 获取用户信息
     */
    public static final String GET_USER_INFO = "/api/v1/user/user_info";

    /**
     * 最新公告
     */
    public static final String GET_NOTICE = "/api/v1/notice/latest";

    /**
     * 账户余额
     */
    public static final String GET_BALANCE = "/api/v1/user/balance";

    /**
     * 账单记录
     */
    public static final String GET_BILLING_RECORD = "/api/v1/billing_record/record";

    /**
     * 订单充值
     */
    public static final String ORDER_RECHARGE = "/api/v1/order/balance_recharge";

    /**
     * 实名认证
     */
    public static final String USER_AUTHENTICATION = "/api/v1/user/authentication";

    /**
     * 修改-忘记 支付密码
     */
    public static final String RESET_PAY_PWD = "/api/v1/user/modify_pay_password";

    /**
     * 防重令牌获取
     */
    public static final String GET_TRANSFER_TOKEN = "/api/v1/transfer/token";

    /**
     * 转账
     */
    public static final String TRANSFER = "/api/v1/transfer/transfer";

    /**
     * 获取当前用户信息
     */
    public static final String GET_CURR_USER_INFO = "/api/v1/user/curr_user";

    /**
     * 确认转账
     */
    public static final String TRANSFER_CONFIRM = "/api/v1/transfer/confirm";

    /**
     * 绑定支付宝
     */
    public static final String ALIPAY_BIND = "/api/v1/user/aliPay_binding";

    /**
     * 解绑支付宝
     */
    public static final String ALIPAY_UNBIND = "/api/v1/user/aliPay_untie";

    /**
     * 获取转账信息
     */
    public static final String GET_TRANSFER_INFO = "/api/v1/transfer/transfer_info";

    /**
     * 转账-退回
     */
    public static final String TRANSFER_REFUND = "/api/v1/transfer/refund";

    /**
     * 修改用户信息
     */
    public static final String MODIFY_USER_INFO = "/api/v1/user/modify_base";

    /**
     * 提现
     */
    public static final String WITHDRAWAL = "/api/v1/withdrawal/withdrawal";
}
