package org.mr.wu.action;

import java.util.List;

import javax.annotation.Resource;

import org.mr.wu.model.Reply;
import org.mr.wu.service.ReplyService;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;

@Controller("replyAction")
public class ReplyAction extends ActionSupport{

	private List<Reply> replies;
	
	private int viewId;
	
	private int replyId;
	
	private int invid;
	
	private String author;
	
	private String content;
	
	private boolean addReplyResult;
	
	@Resource(name="replyService")
	private ReplyService replyService;
	
	
	public String viewReply(){
		replies = replyService.queryReplies(viewId);
		return SUCCESS;
	}
	
	public String gotoReply(){
		return SUCCESS;
	}
	
	public String addReply(){
		if(author == null || "".equals(author)){
			author = "ƒ‰√˚”√ªß";
		}
		try {
			replyService.addReply(invid, author, content);
			addReplyResult = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	

	public List<Reply> getReplies() {
		return replies;
	}

	public void setReplies(List<Reply> replies) {
		this.replies = replies;
	}


	public int getViewId() {
		return viewId;
	}


	public void setViewId(int viewId) {
		this.viewId = viewId;
	}


	public int getReplyId() {
		return replyId;
	}


	public void setReplyId(int replyId) {
		this.replyId = replyId;
	}

	public int getInvid() {
		return invid;
	}

	public void setInvid(int invid) {
		this.invid = invid;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isAddReplyResult() {
		return addReplyResult;
	}

	public void setAddReplyResult(boolean addReplyResult) {
		this.addReplyResult = addReplyResult;
	}
	
}
