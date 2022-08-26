package com.kubian.mode;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 浏览区广告
 * ClassName: AdvertisingEx 
 * @Description: TODO
 * @author HD
 * @date 2018年5月8日
 */
@Entity
@Table(name="AdvertisingEx")
public class AdvertisingEx implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	//Banner图片地址
	private String imgUrl; 
	//链接地址
	private String linkUrl;
	//描述
	private String remark;
	//位置 1:栏目界面上 2:栏目界面下 3:详情界面上 4:详情界面下
	private String site;
	// 所属栏目
	private Long colid;
	// 所属栏目
	private String colName;
	
	@Id  @GeneratedValue(strategy = GenerationType.AUTO) 
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public Long getColid() {
		return colid;
	}
	public void setColid(Long colid) {
		this.colid = colid;
	}
	@Transient
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}
	
}
