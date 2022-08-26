package com.kubian.mode;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户表
 * @author liugang
 *
 */
@Entity
@Table(name="EtcUser")
public class EtcUser implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	private String userName;  
	private String userPwd;
	//用户类型 1:管理员; 2:VIP; 3:普通用户
	private Integer userType = 3;
	//用户邮箱
	private String userEmail;
	//用户绑定的手机号，不绑定为null
	private String userTel;
	//用户绑定的手机号的激活码
	private String validateCode;
	//用户注册的IP
	private String loginIp;
	//用户最近登录的设备ID
	private String eqNumber;
	//用户最近登录的时间
	private Date loginTime = new Date();
	//用户头像地址
	private String logoImgUrl;
	//用户昵称
	private String nickName;
	//用户登录App的方式 1:注册用户；2:微信或QQ
	private Integer loginWay=1;
	//用户以微信或QQ登录的openid,将做为识别用户的唯一标示
	private String openId;
	//是否正在直播   0:没直播   1：正在直播
	private Integer isLive=0;
	//直播截图
	private String liveImgUrl;
	//设备直播默认地址
	private String eqLiveUrl;
	//申请VIP
	private Integer vipStat;
	
	
	
	@Id  @GeneratedValue(strategy = GenerationType.AUTO) 
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserTel() {
		return userTel;
	}
	public void setUserTel(String userTel) {
		this.userTel = userTel;
	}
	public String getValidateCode() {
		return validateCode;
	}
	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}
	public String getLoginIp() {
		return loginIp;
	}
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	public String getEqNumber() {
		return eqNumber;
	}
	public void setEqNumber(String eqNumber) {
		this.eqNumber = eqNumber;
	}
	public Date getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}
	public String getLogoImgUrl() {
		return logoImgUrl;
	}
	public void setLogoImgUrl(String logoImgUrl) {
		this.logoImgUrl = logoImgUrl;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Integer getLoginWay() {
		return loginWay;
	}
	public void setLoginWay(Integer loginWay) {
		this.loginWay = loginWay;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public Integer getIsLive() {
		return isLive;
	}
	public void setIsLive(Integer isLive) {
		this.isLive = isLive;
	}
	public String getLiveImgUrl() {
		return liveImgUrl;
	}
	public void setLiveImgUrl(String liveImgUrl) {
		this.liveImgUrl = liveImgUrl;
	}
	public String getEqLiveUrl() {
		return eqLiveUrl;
	}
	public void setEqLiveUrl(String eqLiveUrl) {
		this.eqLiveUrl = eqLiveUrl;
	}
	public Integer getVipStat() {
		return vipStat;
	}
	public void setVipStat(Integer vipStat) {
		this.vipStat = vipStat;
	}
}
