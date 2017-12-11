package org.mr.wu.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mr.wu.dao.InvitationDao;
import org.mr.wu.dao.ReplyDao;
import org.mr.wu.model.Invitation;
import org.mr.wu.service.InvitationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("invitationService")
public class InvitationServiceImpl  implements InvitationService{

	@Resource(name="invitationDao")
	private InvitationDao invitationDao;
	
	@Resource(name="replyDao")
	private ReplyDao replyDao;
	
	@Override
	public Map<String, Object> queryInvitations(String title, int pageIndex) {
		int count = invitationDao.getInvitationCount(title);
		int totalPage = count % 4 == 0 ? count/4 : count/4+1;
		List<Invitation> invitations = invitationDao.queryInvitations(title,pageIndex);
		Map<String, Object> map = new HashMap<>();
		map.put("totalPage", totalPage);
		map.put("invitations", invitations);
		return map;
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public void deleteInvitation(int id) {
		replyDao.deleteReplies(id);
		invitationDao.deleteInvitation(id);
	}
}
