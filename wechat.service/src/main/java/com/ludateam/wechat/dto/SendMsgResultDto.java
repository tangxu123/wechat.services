package com.ludateam.wechat.dto;

public class SendMsgResultDto extends ResultDto {

	/** 失败的成员ID列表 */
	private String invaliduser = "";
	/** 失败的部门ID列表 */
	private String invalidparty = "";
	/** 失败的标签ID列表 */
	private String invalidtag = "";

	public String getInvaliduser() {
		return invaliduser;
	}

	public void setInvaliduser(String invaliduser) {
		this.invaliduser = invaliduser;
	}

	public String getInvalidparty() {
		return invalidparty;
	}

	public void setInvalidparty(String invalidparty) {
		this.invalidparty = invalidparty;
	}

	public String getInvalidtag() {
		return invalidtag;
	}

	public void setInvalidtag(String invalidtag) {
		this.invalidtag = invalidtag;
	}

}
