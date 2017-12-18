package com.ludateam.wechat.dto;

import java.util.List;

import com.ludateam.wechat.entity.BindingEntity;

public class BindingResult extends ResultDto {

	private List<BindingEntity> bindingList;

	public BindingResult() {
		super.setErrcode("0");
		super.setErrmsg("success");
	}

	public List<BindingEntity> getBindingList() {
		return bindingList;
	}

	public void setBindingList(List<BindingEntity> bindingList) {
		this.bindingList = bindingList;
	}

}
