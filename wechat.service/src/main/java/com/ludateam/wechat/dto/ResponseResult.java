package com.ludateam.wechat.dto;

import java.util.List;
import java.util.Map;

public class ResponseResult extends ResultDto {

	private List<Map<String,String>> bindingList;

	public ResponseResult() {
		super.setErrcode("0");
		super.setErrmsg("操作成功");
	}

	public List<Map<String,String>> getBindingList() {
		return bindingList;
	}

	public void setBindingList(List<Map<String,String>> bindingList) {
		this.bindingList = bindingList;
	}

	 

}
