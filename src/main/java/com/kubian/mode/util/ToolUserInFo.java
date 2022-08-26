package com.kubian.mode.util;

/**
 * 用户信息工具类 ClassName: ToolUserInFo
 * 
 * @Description: TODO
 * @author HD
 * @date 2018年5月15日
 */
public class ToolUserInFo {
	private Long id;
	private String userName;
	private String userImg;
	// 0.是app用户 1.是后台用户
	private Integer judge;
	// 判断被关注人有没有关注关注人（有没有关注粉丝）1.已关注粉丝 2.没关注
	private Integer flag;

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Integer getJudge() {
		return judge;
	}

	public void setJudge(Integer judge) {
		this.judge = judge;
	}

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

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}
}
