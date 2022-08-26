package com.kubian.mode;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 我的云盘录像
 * @author liugang
 *
 */
@Entity
@Table(name="CloudVcr")
public class CloudVcr implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	//用户id
	private String uid;
	//录像标题
	private String vcrTitle;
	//录像描述
	private String vcrDigest;
	//录像文件保存路径
	private String vcrFileName;
	//录像日期
	private Date vcrDate = new Date();
	//录像文件缩略图保存路径
	private String vcrImgName;
	
	
	
	
	@Id  @GeneratedValue(strategy = GenerationType.AUTO) 
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getVcrTitle() {
		return vcrTitle;
	}
	public void setVcrTitle(String vcrTitle) {
		this.vcrTitle = vcrTitle;
	}
	public String getVcrDigest() {
		return vcrDigest;
	}
	public void setVcrDigest(String vcrDigest) {
		this.vcrDigest = vcrDigest;
	}
	public String getVcrFileName() {
		return vcrFileName;
	}
	public void setVcrFileName(String vcrFileName) {
		this.vcrFileName = vcrFileName;
	}
	public Date getVcrDate() {
		return vcrDate;
	}
	public void setVcrDate(Date vcrDate) {
		this.vcrDate = vcrDate;
	}
	public String getVcrImgName() {
		return vcrImgName;
	}
	public void setVcrImgName(String vcrImgName) {
		this.vcrImgName = vcrImgName;
	}
	
}
