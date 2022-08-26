package com.kubian.mode;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 好友 ClassName: Friend
 * 
 * @Description: TODO
 * @author HD
 * @date 2018年5月15日
 */
@Entity
@Table(name = "friend")
public class Friend implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	// 用户id
	private Long appUserId;
	// 好友id
	private Long friendId;
	// 验证状态 1，发起好友请求，等待对方通过。 2，验证通过互为好友 3，验证不通过
	private Integer isPass = 1;
	// 发起请求的描述
	private String description;
	// 分组id
	private Long friendGropId;
	// 时间
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createDate;

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAppUserId() {
		return appUserId;
	}

	public void setAppUserId(Long appUserId) {
		this.appUserId = appUserId;
	}

	public Long getFriendId() {
		return friendId;
	}

	public void setFriendId(Long friendId) {
		this.friendId = friendId;
	}

	public Integer getIsPass() {
		return isPass;
	}

	public void setIsPass(Integer isPass) {
		this.isPass = isPass;
	}

	public Long getFriendGropId() {
		return friendGropId;
	}

	public void setFriendGropId(Long friendGropId) {
		this.friendGropId = friendGropId;
	}

}
