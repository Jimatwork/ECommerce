package com.kubian.mode;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 会议室
 * @author liugang
 *
 */
@Entity
@Table(name="MeetingRoomPart")
public class MeetingRoomPart implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	private String userId;
	private String partAlias;
	//会议状态  0：未查看   1：已查看
	private Integer msgStatus = 1;
	
	
	@Id  @GeneratedValue(strategy = GenerationType.AUTO) 
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getMsgStatus() {
		return msgStatus;
	}
	public void setMsgStatus(Integer msgStatus) {
		this.msgStatus = msgStatus;
	}
	@Column(length = 500000)
	public String getPartAlias() {
		return partAlias;
	}
	public void setPartAlias(String partAlias) {
		this.partAlias = partAlias;
	}
	
	
	
	
}
