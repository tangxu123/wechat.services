package com.ludateam.wechat.dto;

public class ResponseResult extends ResultDto {

	private Object bindingList;

	public ResponseResult() {
		super.setErrcode("0");
		super.setErrmsg("操作成功");
	}

	public Object getBindingList() {
		return bindingList;
	}

	public void setBindingList(Object bindingList) {
		this.bindingList = bindingList;
	}

	 

}
