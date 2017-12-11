package org.mr.wu.dao;

import java.util.List;

import org.mr.wu.model.Invitation;

public interface InvitationDao {

	List<Invitation> queryInvitations(String title, int pageIndex);
	
	int getInvitationCount(String title);
	
	void deleteInvitation(int id);
}
