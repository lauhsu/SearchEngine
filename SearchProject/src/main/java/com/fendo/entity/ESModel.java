package com.fendo.entity;

import java.io.Serializable;

import javax.persistence.*;

/**
 * On 2/2/2018.2:03 PM
 */
@Entity
@Table(name = "t_search")
public class ESModel implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_from")
    private String source_from;//

    @Column(name = "date_time")
    private String date_time;//

    @Column(name = "url")
    private String url;//ur

    @Column(name = "type")
    private String type;//

    @Column(name = "title")
    private String remark;//
    
    @Column(name = "author")
    private String author;//

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getSource_from() {
		return source_from;
	}

	public void setSource_from(String source_from) {
		this.source_from = source_from;
	}

	public String getDate_time() {
		return date_time;
	}

	public void setDate_time(String date_time) {
		this.date_time = date_time;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	public ESModel() {}
	public ESModel(Long id, String source_from, String date_time, String url, String type, String remark, String author) {
		this.id = id;
		this.source_from = source_from;
		this.date_time = date_time;
		this.url = url;
		this.type = type;
		this.remark = remark;
		this.author = author;
	}
    
    
}