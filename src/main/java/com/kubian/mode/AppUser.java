package com.kubian.mode;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Value;

/**
 * APP用户表
 *
 * @author wujun
 */
@Entity
@Table(name = "AppUser")
public class AppUser implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String phone;
	private String phonePwd;
	// 微信openid
	private String openId;
	// 状态 0-显示;1-屏蔽
	private String state = "0";
	// 头像
	private String userImg;
	// 昵称
	private String userName;
	// 前缀
	private String prefix;
	// 是否打开pc登录 0 不打开 1.打开
	private Integer isPc = 0;
	// 左上角水印
	private String syImg1;
	// 我的水印图片中
	private String syImg2;
	// 我的水印图片小
	private String syImg3;
	// 我的默认缩略图
	private String sltImg;
	// 验证状态 1，发起好友请求，等待对方通过。 2，验证通过互为好友 3，验证不通过
	private Integer isPass = 1;
	// 关注
	private Integer attention = 0;
	// 粉丝
	private Integer fans = 0;
	// 是否使用水印 0.不使用 1.使用
	private Integer isWatermark = 1;
	// 网站编号
	private Long number;
	// 商家ID
	private Long bid;

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public Integer getIsWatermark() {
		return isWatermark;
	}

	public void setIsWatermark(Integer isWatermark) {
		this.isWatermark = isWatermark;
	}

	public Integer getAttention() {
		return attention;
	}

	public void setAttention(Integer attention) {
		this.attention = attention;
	}

	public Integer getFans() {
		return fans;
	}

	public void setFans(Integer fans) {
		this.fans = fans;
	}

	@Transient
	public Integer getIsPass() {
		return isPass;
	}

	public void setIsPass(Integer isPass) {
		this.isPass = isPass;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhonePwd() {
		return phonePwd;
	}

	public void setPhonePwd(String phonePwd) {
		this.phonePwd = phonePwd;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getIsPc() {
		return isPc;
	}

	public void setIsPc(Integer isPc) {
		this.isPc = isPc;
	}

	public String getSyImg1() {
		return syImg1;
	}

	public void setSyImg1(String syImg1) {
		this.syImg1 = syImg1;
	}

	public String getSyImg2() {
		return syImg2;
	}

	public void setSyImg2(String syImg2) {
		this.syImg2 = syImg2;
	}

	public String getSyImg3() {
		return syImg3;
	}

	public void setSyImg3(String syImg3) {
		this.syImg3 = syImg3;
	}

	public String getSltImg() {
		return sltImg;
	}

	public void setSltImg(String sltImg) {
		this.sltImg = sltImg;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public Long getBid() {
		return bid;
	}

	public void setBid(Long bid) {
		this.bid = bid;
	}
}
