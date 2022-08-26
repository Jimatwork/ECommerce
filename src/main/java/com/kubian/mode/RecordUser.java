package com.kubian.mode;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 记录添加用户表
 * @author wujun
 *
 */
@Entity
@Table(name="RecordUser")
public class RecordUser implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	// EtcUser 对应ID
	private Long pid;
	//用户头像地址
	private String logoImgUrl;
	//用户昵称
	private String nickName;
	
	@Id  @GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Long getPid() {
		return pid;
	}
	public void setPid(Long pid) {
		this.pid = pid;
	}
	
}
