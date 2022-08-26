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
 * app栏目内容 屏蔽表
 * @author wujun
 *
 */
@Entity
@Table(name="ColumnContentShield")
public class ColumnContentShield implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	//栏目ID
	private Long colId;  
	//内容标题
	private String conTitle; 
	//内容摘要
	private String conRemark;
	//内容缩略图
	private String conImg;
	//内容
	private String conHtml;
	//直播地址
	private String conLivePath;
	//点播地址
	private String conVideoPath;
		
	//创建时间
	private Date createDate = new Date();  
	//点赞次数
	private Integer praiseCount = 1;
	//反赞次数
	private Integer dislikeCount = 0;
	//阅读次数
	private Integer playCount = 1;
	// 评论次数
	private Integer exaCount = 0;
	
	//广告链接地址
	private String linkUrl;
	//广告图片地址
	private String imgUrl;
	//状态 0-不显示;1-显示在我的;2-显示在主页
	private String state = "0";
	
	//所属app 用户
	private Long appUserId;
	// 所属栏目
	private String type;
	
	@Id  @GeneratedValue(strategy = GenerationType.AUTO) 
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getColId() {
		return colId;
	}
	public void setColId(Long colId) {
		this.colId = colId;
	}
	public String getConTitle() {
		return conTitle;
	}
	public void setConTitle(String conTitle) {
		this.conTitle = conTitle;
	}
	@Column(length = 50000000)
	public String getConRemark() {
		return conRemark;
	}
	public void setConRemark(String conRemark) {
		this.conRemark = conRemark;
	}
	public String getConImg() {
		return conImg;
	}
	public void setConImg(String conImg) {
		this.conImg = conImg;
	}
	@Column(length = 50000000)
	public String getConHtml() {
		return conHtml;
	}
	public void setConHtml(String conHtml) {
		this.conHtml = conHtml;
	}
	@Column(length = 50000000)
	public String getConLivePath() {
		return conLivePath;
	}
	public void setConLivePath(String conLivePath) {
		this.conLivePath = conLivePath;
	}
	@Column(length = 50000000)
	public String getConVideoPath() {
		return conVideoPath;
	}
	public void setConVideoPath(String conVideoPath) {
		this.conVideoPath = conVideoPath;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Integer getPraiseCount() {
		return praiseCount;
	}
	public void setPraiseCount(Integer praiseCount) {
		this.praiseCount = praiseCount;
	}
	public Integer getDislikeCount() {
		return dislikeCount;
	}
	public void setDislikeCount(Integer dislikeCount) {
		this.dislikeCount = dislikeCount;
	}
	public Integer getPlayCount() {
		return playCount;
	}
	public void setPlayCount(Integer playCount) {
		this.playCount = playCount;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public Integer getExaCount() {
		return exaCount;
	}
	public void setExaCount(Integer exaCount) {
		this.exaCount = exaCount;
	}
	public Long getAppUserId() {
		return appUserId;
	}
	public void setAppUserId(Long appUserId) {
		this.appUserId = appUserId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
