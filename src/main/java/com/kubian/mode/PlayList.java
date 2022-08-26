package com.kubian.mode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



/**
 * 节目单
 * @author
 */
@Entity
@Table(name="PlayList")
public class PlayList implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private long id; 
	private String playListName; //节目单名称
	private Date createDate; //创建时间
	private String remark;   //描述  
	private String contentId; //内容ID
	
	
	@Id  @GeneratedValue(strategy = GenerationType.AUTO) 
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getPlayListName() {
		return playListName;
	}
	public void setPlayListName(String playListName) {
		this.playListName = playListName;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

	
	
}
