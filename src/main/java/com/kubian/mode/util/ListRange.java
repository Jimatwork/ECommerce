package com.kubian.mode.util;

import java.util.ArrayList;
import java.util.List;

public class ListRange<T> {
	boolean success; //是否成功
	String message; //消息
	long totalSize; //数据的长度
	List<T> list; //存放数据集合
	
	public ListRange() {
		this.totalSize = 0;
		this.list = new ArrayList<T>();
	}
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public long getTotalSize() {
		return totalSize;
	}
	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	
	
}
