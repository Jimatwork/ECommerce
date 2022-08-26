package com.kubian.mode;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 直播频道 ClassName: LiveStream
 * 
 * @Description: TODO
 * @author HD
 * @date 2018年6月4日
 */
@Entity
@Table(name = "liveStream")
public class LiveStream implements Serializable {
	private static final long serialVersionUID = -3906850698113592463L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	// 频道名称
	private String lsName;
	// 直播地址
	private String lsParh;
	// 直播密码
	private String lsPwd;
	// 观看地址
	private String lookPath;
	// 用户id
	private Long appUserId;
	// 创建时间
	private Date createDate = new Date();

	public Long getAppUserId() {
		return appUserId;
	}

	public void setAppUserId(Long appUserId) {
		this.appUserId = appUserId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLsName() {
		return lsName;
	}

	public void setLsName(String lsName) {
		this.lsName = lsName;
	}

	public String getLsParh() {
		return lsParh;
	}

	public void setLsParh(String lsParh) {
		this.lsParh = lsParh;
	}

	public String getLsPwd() {
		return lsPwd;
	}

	public void setLsPwd(String lsPwd) {
		this.lsPwd = lsPwd;
	}

	public String getLookPath() {
		return lookPath;
	}

	public void setLookPath(String lookPath) {
		this.lookPath = lookPath;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
