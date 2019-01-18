package com.fendo.dto;

import java.util.Date;

public class ModelDto {
private String title;//标题
private String insertTime;
private String url;//url
private String text;//全文
private String type;//类型
private String shortText;
public ModelDto() {}
public ModelDto(String title, String insertTime, String url, String text, String type, String shortText) {
	this.title = title;
	this.insertTime = insertTime;
	this.url = url;
	this.text = text;
	this.type = type;
	this.shortText = shortText;
}

	public String getShortText() {
	return shortText;
}
public void setShortText(String shortText) {
	this.shortText = shortText;
}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
