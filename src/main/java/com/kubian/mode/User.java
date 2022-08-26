package com.kubian.mode;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 用户表
 * 
 * @author liugang
 *
 */
@Entity
@Table(name = "User")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	// app用户打开Pc登录的userid
	private Long auId;
	private String userName;
	private String userPwd;
	// 头像地址
	private String img;
	// 前缀
	private String prefix;
	// 用户角色 1=管理员，2=普通编辑
	private Integer userRole;
	// 昵称
	private String nickName;
	// 水印图
	private String watermark;
	// 用户左上角水印
	private String userWatermark;
	// 启动图片
	private String startPath;
	// 启动图片的显示时间 (秒)
	private Integer showTime;
	// 0.隐藏ios微信登录 1.显示ios微信登录
	private Integer flag;
	// 网站编码
	private Long number;

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public String getUserWatermark() {
		return userWatermark;
	}

	public void setUserWatermark(String userWatermark) {
		this.userWatermark = userWatermark;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Integer getShowTime() {
		return showTime;
	}

	public void setShowTime(Integer showTime) {
		this.showTime = showTime;
	}

	public String getStartPath() {
		return startPath;
	}

	public void setStartPath(String startPath) {
		this.startPath = startPath;
	}

	public String getWatermark() {
		return watermark;
	}

	public void setWatermark(String watermark) {
		this.watermark = watermark;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
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

	public Integer getUserRole() {
		return userRole;
	}

	public void setUserRole(Integer userRole) {
		this.userRole = userRole;
	}

	public Long getAuId() {
		return auId;
	}

	public void setAuId(Long auId) {
		this.auId = auId;
	}

}
