package com.kubian.mode;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * app 评论
 * 
 * @author wujun
 *
 */
@Entity
@Table(name = "Comment")
public class Comment implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	// 内容ID
	private Long conId;
	// 内容
	private String content;
	// 创建时间
	private Date createDate = new Date();
	// 用户ip
	private String ip;
	// 用户id
	private Long appUserId;
	// 用户昵称
	private String username;
	// 用户头像
	private String avator;
	// 状态 0-显示;1-屏蔽
	private String state = "0";
	// 作者回复
	private String replyContent;
	// 点赞数
	private Integer praiseCount = 0;
	// 标识字段 1.当前用户已点赞当前文章 0.没点赞
	private Integer give;
	// bih波士顿导报操作酷编数据时候用的 0.隐藏 1.显示
	private Integer bih;

	public Integer getBih() {
		return bih;
	}

	public void setBih(Integer bih) {
		this.bih = bih;
	}

	@Transient
	public Integer getGive() {
		return give;
	}

	public void setGive(Integer give) {
		this.give = give;
	}

	public Integer getPraiseCount() {
		return praiseCount;
	}

	public void setPraiseCount(Integer praiseCount) {
		this.praiseCount = praiseCount;
	}

	public Long getAppUserId() {
		return appUserId;
	}

	public void setAppUserId(Long appUserId) {
		this.appUserId = appUserId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getConId() {
		return conId;
	}

	public void setConId(Long conId) {
		this.conId = conId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAvator() {
		return avator;
	}

	public void setAvator(String avator) {
		this.avator = avator;
	}

	public String getReplyContent() {
		return replyContent;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

}
