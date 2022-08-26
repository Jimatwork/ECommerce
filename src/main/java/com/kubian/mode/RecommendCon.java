package com.kubian.mode;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 推荐的详情表 ClassName: RecommendCon
 * 
 * @Description: TODO
 * @author HD
 * @date 2018年6月22日
 */
@Entity
@Table(name = "recommendCon")
public class RecommendCon implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	// 内容id
	private Long conId;
	// 推荐表id
	private Long rId;
	// 创建时间
	private Date createDate = new Date();
	// 排序的时间
	private Date pxDate;
	
	public Date getPxDate() {
		return pxDate;
	}

	public void setPxDate(Date pxDate) {
		this.pxDate = pxDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getConId() {
		return conId;
	}

	public void setConId(Long conId) {
		this.conId = conId;
	}

	public Long getrId() {
		return rId;
	}

	public void setrId(Long rId) {
		this.rId = rId;
	}

}
