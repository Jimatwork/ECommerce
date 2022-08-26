package com.kubian.mode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * app首页栏目
 * 
 * @author liugang
 *
 */
@Entity
@Table(name = "Columns")
public class Columns implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	// 栏目名称
	private String colName;
	// 栏目Icon图片
	private String colIcon;
	// 1.新闻 2.直播 3.点播
	private String colLink;
	// 栏目链接页面
	private String colLinkHtml;
	// 栏目顺序
	private int sort;
	// 栏目链接页面 0-显示;1-屏蔽
	private String state = "0";
	private Long cid;
	// 栏目里面的内容

	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public String getColIcon() {
		return colIcon;
	}

	public void setColIcon(String colIcon) {
		this.colIcon = colIcon;
	}

	public String getColLink() {
		return colLink;
	}

	public void setColLink(String colLink) {
		this.colLink = colLink;
	}

	public String getColLinkHtml() {
		return colLinkHtml;
	}

	public void setColLinkHtml(String colLinkHtml) {
		this.colLinkHtml = colLinkHtml;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
