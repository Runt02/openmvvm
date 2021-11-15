package com.runt.open.mvvm.ui.login;

/**
 * Created by Administrator on 2021/11/15 0015.
 */

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户表 AdvertCustomer
 * @author Runt_自动生成
 * @email qingingrunt2010@qq.com
 * @date 2020-04-17 20:28:26
 */
public class UserBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static UserBean user;

    public static UserBean getUser() {
        return user;
    }

    public static void setUser(UserBean user) {
        UserBean.user = user;
    }

    /** 主键 **/
    private String id;


    /** 手机号 **/
    private String phone;

    /** 登录token **/
    private String token;

    /** 昵称 **/
    private String username;

    /** 头像 **/
    private String head;

    /** 金币数量 **/
    private int coin;

    /* 连续签到天数 */
    private int sign;

    /** 用户类型(0推广，1店员，2商家，3广告商) **/
    private Integer type;

    /** 上级管理者 **/
    private String upuser;

    /** 余额 **/
    private BigDecimal balance;

    /** 会员等级（0普通用户，1会员） **/
    private Integer vlevel;

    /** 会员到期时间 **/
    private Date vtime;

    /** 状态0通过，1审核中，-1封收益 ， -2 限制支付 ， -3限制广告 ，-4限制发送短信，-5永久限制发送短信，-6 限制登录 **/
    private String status;

    /** 真实姓名 **/
    private String realname;

    /** 银行卡 **/
    private String bank;

    /** 支付宝 **/
    private String alipay;

    /** 微信 **/
    private String wechat;

    /** QQ **/
    private String qq;

    /** 创建时间 **/
    private Date ctime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }


    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }


    public String getUpuser() {
        return upuser;
    }

    public void setUpuser(String upuser) {
        this.upuser = upuser;
    }


    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }


    public Integer getVlevel() {
        return vlevel;
    }

    public void setVlevel(Integer vlevel) {
        this.vlevel = vlevel;
    }


    public Date getVtime() {
        return vtime;
    }

    public void setVtime(Date vtime) {
        this.vtime = vtime;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getAlipay() {
        return alipay;
    }

    public void setAlipay(String alipay) {
        this.alipay = alipay;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }


    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }


    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "id='" + id + '\'' +
                ", phone='" + phone + '\'' +
                ", token='" + token + '\'' +
                ", username='" + username + '\'' +
                ", head='" + head + '\'' +
                ", coin=" + coin +
                ", sign=" + sign +
                ", type=" + type +
                ", upuser='" + upuser + '\'' +
                ", balance=" + balance +
                ", vlevel=" + vlevel +
                ", vtime=" + vtime +
                ", status=" + status +
                ", wechat='" + wechat + '\'' +
                ", qq='" + qq + '\'' +
                ", ctime=" + ctime +
                '}';
    }
}