package com.ludateam.wechat.dto;

import java.util.List;

import com.ludateam.wechat.entity.UserEntity;

public class UserListDto extends ResultDto {

	/** 成员列表 */
	private List<UserEntity> userlist;

	public List<UserEntity> getUserlist() {
		return userlist;
	}

	public void setUserlist(List<UserEntity> userlist) {
		this.userlist = userlist;
	}
}
