package org.mr.wu.action;

import java.util.Map;

import javax.annotation.Resource;

import org.mr.wu.service.InvitationService;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;


@Controller("invitationAction")
public class InvitationAction extends ActionSupport{

	private Map<String, Object> invitations;
	
	private int deleteId;
	
	private boolean deleteResult;
	
	private String title;
	
	private int pageIndex;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	@Resource(name="invitationService")
	private InvitationService invitationService;
	
	public String search(){
		setInvitations(invitationService.queryInvitations(title, pageIndex));
		return SUCCESS;
	}
	
	public String delete(){
		invitationService.deleteInvitation(deleteId);
		deleteResult = true;
		return SUCCESS;
	}

	public Map<String, Object> getInvitations() {
		return invitations;
	}

	public void setInvitations(Map<String, Object> invitations) {
		this.invitations = invitations;
	}

	public int getDeleteId() {
		return deleteId;
	}

	public void setDeleteId(int deleteId) {
		this.deleteId = deleteId;
	}

	public boolean isDeleteResult() {
		return deleteResult;
	}

	public void setDeleteResult(boolean deleteResult) {
		this.deleteResult = deleteResult;
	}
	
}
