package org.mr.wu.model;

import java.sql.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//@Entity
//@Table(name="invitation")
public class Invitation {
	
//	@Id
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
//	@Column(name="title",length=100,nullable=false)
	private String title;
	
//	@Column(name="summary", length=255)
	private String summary;
	
//	@Column(name="author", length=50)
	private String author;
	
//	@Column(name="createdate",nullable=false)
	private Date createDate;
	
	private Set<Reply> replies;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Set<Reply> getReplies() {
		return replies;
	}

	public void setReplies(Set<Reply> replies) {
		this.replies = replies;
	}
}
