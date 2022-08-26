package com.kubian.mode;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 普通广告 ClassName: Advertising
 * 
 * @Description: TODO
 * @author HD
 * @date 2018年5月8日
 */
@Entity
@Table(name = "Advertising")
public class Advertising implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	// Banner图片地址
	private String imgUrl;
	// 链接地址
	private String linkUrl;
	// 描述
	private String remark;
	// 广告位置 0.页面上方 1.页面左边 2.页面右边
	private String site;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
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

}
