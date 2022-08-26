package com.kubian.mode.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultVO extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;

	public ResultVO() {
		put("code", 0);
		put("msg", "操作成功");
	}

	public static ResultVO error() {
		return error(1, "操作失败");
	}

	public static ResultVO error(String msg) {
		return error(500, msg);
	}

	public static ResultVO error(int code, String msg) {
		ResultVO r = new ResultVO();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}

	public static ResultVO custom(int code, String msg, List data ,Integer size) {
		ResultVO r = new ResultVO();
		r.put("code", code);
		r.put("msg", msg);
		r.put("data", data);
		r.put("size", size);
		return r;
	}

	public static ResultVO ok(String msg) {
		ResultVO r = new ResultVO();
		r.put("msg", msg);
		return r;
	}

	public static ResultVO ok(Integer code,String msg) {
		ResultVO r = new ResultVO();
		r.put("msg", msg);
		r.put("code",code);
		return r;
	}

	public static ResultVO ok(Map<String, Object> map) {
		ResultVO r = new ResultVO();
		r.putAll(map);
		return r;
	}

	public static ResultVO ok() {
		return new ResultVO();
	}

	@Override
	public ResultVO put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
