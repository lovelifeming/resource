package org.mr.wu.service;

import java.util.Map;

public interface InvitationService {
	
	Map<String, Object> queryInvitations(String title, int pageIndex);

	void deleteInvitation(int id);
}
