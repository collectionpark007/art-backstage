package com.art.support.common;


/**
 * 返回码统一类
 *
 * @author never
 */
public interface ReturnCode {

    String retCode = "retCode";
    String retMessage = "retMessage";
    String retEnMessage = "retEnMessage";

    /**
     * 公共
     */
    String success = "0000";//成功
    String error = "0001";//通用错误


    /**
     * 登录模块 L
     */
    String sessionInvalid = "L000";//用户未登录，session失效

    /**
     * 注册模块
     */
    String inviteCodeInvalid = "R001";//注册邀请码无效

    /**
     * 登录
     */
    String merchantNotLogin = "L001";

    /**
     * 商品模块 P
     */
    String productNotExists = "P000";//商品不存在

    /**
     * 商品模块 P
     */
    String productNotStock = "P001";//商品库存不足

    /**
     * 验证模块
     */
    String googleNotBind = "A000";//未绑定谷歌验证

    /**
     * 运费
     */
    String notSupportShipping = "S004";//不支持配送
    /**
     * 运费
     */
    String paymentRedirect = "P001";//支付id找不到


    /** otc接口返回码 */

    String otcMemberNotExist = "O001"; //otc用户不存在

    String quantityError = "O002";//金额错误

    String qtyGreaterThanBalance = "O003"; //锁定金额大于用户余额

    String assetError = "O004"; //币种错误

    String lockNoNotExist = "O005"; //锁定单不存在

    String freezeBalanceError = "O006"; //取消锁定时，冻结金额不足异常

    String LockHasCancelled = "O007"; //锁定状态错误，请勿重复操作

    String signValidateError = "O008"; //前面验证错误

    /** otc接口返回码 end */
}
