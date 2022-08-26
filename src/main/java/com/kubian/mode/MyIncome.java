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
 * 我的打赏
 * @author liugang
 *
 */
@Entity
@Table(name="MyIncome")
public class MyIncome implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	//被打赏人Id
	private String dsUid;
	//被打赏人名称
	private String dsUname;
	//打赏人Id
	private String dsId;
	//打赏人名称
	private String dsName;
	//打赏金额
	private Integer dsSum;
	//打赏日期
	private Date dsDate = new Date();
	//打赏文章Id
	private String dsConId;
	//打赏文章名称
	private String dsConName;
	//打赏支付订单号
	private String outTradeNo;
	//打赏类型  APP内打赏和微信分享打赏
	private String dsType;
	
	
	
	@Id  @GeneratedValue(strategy = GenerationType.AUTO) 
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDsUid() {
		return dsUid;
		
	}
	public void setDsUid(String dsUid) {
		this.dsUid = dsUid;
	}
	public String getDsUname() {
		return dsUname;
	}
	public void setDsUname(String dsUname) {
		this.dsUname = dsUname;
	}
	public String getDsId() {
		return dsId;
	}
	public void setDsId(String dsId) {
		this.dsId = dsId;
	}
	public String getDsName() {
		return dsName;
	}
	public void setDsName(String dsName) {
		this.dsName = dsName;
	}
	public Integer getDsSum() {
		return dsSum;
	}
	public void setDsSum(Integer dsSum) {
		this.dsSum = dsSum;
	}
	public Date getDsDate() {
		return dsDate;
	}
	public void setDsDate(Date dsDate) {
		this.dsDate = dsDate;
	}
	public String getDsConId() {
		return dsConId;
	}
	public void setDsConId(String dsConId) {
		this.dsConId = dsConId;
	}
	@Column(length = 50000000)
	public String getDsConName() {
		return dsConName;
	}
	public void setDsConName(String dsConName) {
		this.dsConName = dsConName;
		
	}
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public String getDsType() {
		return dsType;
	}
	public void setDsType(String dsType) {
		this.dsType = dsType;
	}
	
	public MyIncome(String dsUid,String dsUname,Integer dsSum){
		this.dsUid=dsUid;
		this.dsUname=dsUname;
		this.dsSum=dsSum;
	}
	
	public MyIncome(){		
	}
}
