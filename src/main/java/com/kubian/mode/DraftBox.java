package com.kubian.mode;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 草稿箱 app栏目内容
 * @author liugang
 *
 */
@Entity
@Table(name="DraftBox")
public class DraftBox implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	//栏目ID
	private Long colId;  
	//内容标题
	private String conTitle; 
	//内容摘要
	private String conRemark;
	//内容缩略图（大图）1280*720
	private String conImg;
	//内容缩略图（小图 微信分享用）200*170
	private String conImg1;
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
	//所属app 用户
	private Long appUserId;
	//状态 1-显示在发现;2-显示在主页;3-发现不显示;4-首页不显示;
	private String state = "1";
	// 所属栏目
	private String type;
	//栏目顺序
	private Long sort;
	// 用户名称
	private String userName;
	//状态 0-默认;1-显示在顶部;
	private String hot = "0";
	//是否公开
	private Boolean isPublic;
	//关注
	private String follow = "0";
	// 菜单
	//栏目ID
	private Long colIdP;
	//栏目ID
	private String colName="";
	
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
	public Long getSort() {
		return sort;
	}
	public void setSort(Long sort) {
		this.sort = sort;
	}
	@Transient
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getHot() {
		return hot;
	}
	public void setHot(String hot) {
		this.hot = hot;
	}
	public Boolean getIsPublic() {
		return isPublic;
	}
	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}
	public String getConImg1() {
		return conImg1;
	}
	public void setConImg1(String conImg1) {
		this.conImg1 = conImg1;
	}
	public String getFollow() {
		return follow;
	}
	public void setFollow(String follow) {
		this.follow = follow;
	}
	@Transient
	public Long getColIdP() {
		return colIdP;
	}
	public void setColIdP(Long colIdP) {
		this.colIdP = colIdP;
	}
	@Transient
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}
	
	
	
}
