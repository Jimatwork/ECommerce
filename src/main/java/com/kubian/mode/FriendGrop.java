package com.kubian.mode;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 好友分组表
 * ClassName: FriendGrop 
 * @Description: TODO
 * @author HD
 * @date 2018年5月11日
 */
@Entity
@Table(name="friendGrop")
public class FriendGrop implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	//用户id
	private Long userId;
	//分组名称
	private String gropName="我的好友";
	
	@Id  @GeneratedValue(strategy = GenerationType.AUTO) 
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getGropName() {
		return gropName;
	}
	public void setGropName(String gropName) {
		this.gropName = gropName;
	}
	
}
