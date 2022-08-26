package com.kubian.mode;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 关注表 ClassName: Attention
 * 
 * @Description: TODO
 * @author HD
 * @date 2018年4月28日
 */
@Entity
@Table(name = "attention")
public class Attention implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	// 关注人id
	private Long followerId;
	// 被关注人id
	private Long beFollowedId;
	// 0.是app用户 1.是后台用户
	private Integer judge;
	

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

	public Long getFollowerId() {
		return followerId;
	}

	public void setFollowerId(Long followerId) {
		this.followerId = followerId;
	}

	public Long getBeFollowedId() {
		return beFollowedId;
	}

	public void setBeFollowedId(Long beFollowedId) {
		this.beFollowedId = beFollowedId;
	}

}
