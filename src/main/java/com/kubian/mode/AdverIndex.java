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
 * Banner
 * @author wujun
 *
 */
@Entity
@Table(name="AdverIndex")
public class AdverIndex implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	//栏目名称
	private String indname; 
	//栏目顺序
	private int sort;
	//栏目里面的内容
	private List<Advertising> indContents = new ArrayList<Advertising>();
	
	
	@Id  @GeneratedValue(strategy = GenerationType.AUTO) 
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getIndname() {
		return indname;
	}
	public void setIndname(String indname) {
		this.indname = indname;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	
	@Transient
	public List<Advertising> getIndContents() {
		return indContents;
	}
	public void setIndContents(List<Advertising> indContents) {
		this.indContents = indContents;
	}
	
	
}
