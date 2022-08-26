package com.kubian.mode;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * app首页滚动字幕
 * @author liugang
 *
 */
@Entity
@Table(name="Marquee")
public class Marquee implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	//字幕内容
	private String content;  
	
	@Id  @GeneratedValue(strategy = GenerationType.AUTO) 
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
