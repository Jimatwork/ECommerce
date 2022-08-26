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
 * app栏目内容备份表
 * 
 * @author liugang
 *
 */
@Entity
@Table(name = "backUpColumnContent")
public class BackUpColumnContent implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	private Long id;
	// 栏目ID
	private Long colId;
	// 内容标题
	private String conTitle;
	// 内容摘要
	private String conRemark;
	// 内容缩略图（大图）1280*720
	private String conImg;
	// 内容缩略图（小图 微信分享用）200*170
	private String conImg1;
	// 内容
	private String conHtml;
	// 直播地址
	private String conLivePath;
	// 点播地址
	private String conVideoPath;
	// 创建时间
	private Date createDate = new Date();
	// 点赞次数
	private Integer praiseCount = 0;
	// 反赞次数
	private Integer dislikeCount = 0;
	// 阅读次数
	private Integer playCount = 0;
	// 评论次数
	private Integer exaCount = 0;
	// 广告链接地址
	private String linkUrl;
	// 广告图片地址
	private String imgUrl;
	// 所属app 用户
	private Long appUserId;
	// 后台用户
	private Long userId;
	// 状态 0.后台发布的 头条 隐藏 1.后台发布的 头条 显示 2.代表是app用户发布的 发现 显示3.发现 隐藏的app用户发布的数据
	private String state = "1";
	// 栏目顺序
	private Long sort = 0L;
	// 状态 0-默认;1-显示在轮播图片; 2.显示在滚动
	private String hot = "0";
	// 轮播图片和滚动头条的时间
	private Date hotDate;
	// 是否公开
	private Boolean isPublic;
	// 关注
	private String follow = "0";
	private String type;
	// 栏目名称

	private String colName = "";
	// 是否加入延伸阅读 这里设置为时间戳，为了获取的时候排序方便
	private Long isFurther;
	// pc登录用户是否加入了广告图片
	private String userAdImg;
	// 背景音乐
	private String backgroundMusic;
	// 标识字段 1.当前用户已点赞当前文章 0.没点赞
	private Integer give;

	// 作者姓名
	private String userName;
	// 作者头像
	private String userImg;
	// 文字转化的语音地址
	private String mp3Name;
	// app用户的默认缩略图
	private String sltImg;
	// 不为空是置顶的内容 为空不是置顶的内容
	private Date top;
	// 热门 不为空是置顶的内容 为空不是置顶的内容
	private Date hTop;
	// 热门的操作 状态 0-默认;1-显示在轮播图片; 2.显示在滚动
	private String hHot;
	// 0.是手机端编辑的 1.是pc端编辑的
	private Integer redact;

	public Integer getRedact() {
		return redact;
	}

	public void setRedact(Integer redact) {
		this.redact = redact;
	}

	public String gethHot() {
		return hHot;
	}

	public void sethHot(String hHot) {
		this.hHot = hHot;
	}

	public Date getHotDate() {
		return hotDate;
	}

	public void setHotDate(Date hotDate) {
		this.hotDate = hotDate;
	}

	public Date gethTop() {
		return hTop;
	}

	public void sethTop(Date hTop) {
		this.hTop = hTop;
	}

	public Date getTop() {
		return top;
	}

	public void setTop(Date top) {
		this.top = top;
	}

	@Transient
	public String getSltImg() {
		return sltImg;
	}

	public void setSltImg(String sltImg) {
		this.sltImg = sltImg;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMp3Name() {
		return mp3Name;
	}

	public void setMp3Name(String mp3Name) {
		this.mp3Name = mp3Name;
	}

	@Transient
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Transient
	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}

	@Transient
	public Integer getGive() {
		return give;
	}

	public void setGive(Integer give) {
		this.give = give;
	}

	public String getBackgroundMusic() {
		return backgroundMusic;
	}

	public void setBackgroundMusic(String backgroundMusic) {
		this.backgroundMusic = backgroundMusic;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public Long getSort() {
		return sort;
	}

	public void setSort(Long sort) {
		this.sort = sort;
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
	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public Long getIsFurther() {
		return isFurther;
	}

	public void setIsFurther(Long isFurther) {
		this.isFurther = isFurther;
	}

	public String getUserAdImg() {
		return userAdImg;
	}

	public void setUserAdImg(String userAdImg) {
		this.userAdImg = userAdImg;
	}

}
