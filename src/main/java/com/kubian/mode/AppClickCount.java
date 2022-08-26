package com.kubian.mode;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="AppClickCount")
public class AppClickCount implements Serializable{

	/**
	 *浏览次数
	 * @author liugang 
	 */
	private static final long serialVersionUID = 1L;
	//自增长id
	private Long id;
	//栏目id
	private Long colId;
	//浏览次数
	private Integer clickcount;
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
	public Integer getClickcount() {
		return clickcount;
	}
	public void setClickcount(Integer clickcount) {
		this.clickcount = clickcount;
	}
}
