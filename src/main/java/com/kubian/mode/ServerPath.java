package com.kubian.mode;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 服务器地址
 * 
 * @author liugang
 *
 */
@Entity
@Table(name = "ServerPath")
public class ServerPath implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	// 国内服务器地址
	private String serverPath;
	// 国外服务器地址
	private String fcServerPath;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFcServerPath() {
		return fcServerPath;
	}

	public void setFcServerPath(String fcServerPath) {
		this.fcServerPath = fcServerPath;
	}

	public String getServerPath() {
		return serverPath;
	}

	public void setServerPath(String serverPath) {
		this.serverPath = serverPath;
	}

}
