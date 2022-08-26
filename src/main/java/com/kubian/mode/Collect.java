package com.kubian.mode;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 收藏表
 * ClassName: Collect 
 * @Description: TODO
 * @author HD
 * @date 2018年4月28日
 */
@Entity
@Table(name = "collect")
public class Collect implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	// 用户id
	private Long appUserId;
	// 内容id
	private Long columnContentId;
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
	public Long getColumnContentId() {
		return columnContentId;
	}
	public void setColumnContentId(Long columnContentId) {
		this.columnContentId = columnContentId;
	}
	
	
}
