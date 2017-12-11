package org.mr.wu.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.mr.wu.dao.ReplyDao;
import org.mr.wu.model.Reply;
import org.mr.wu.service.ReplyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("replyService")
public class ReplyServiceImpl implements ReplyService{

	@Resource(name="replyDao")
	private ReplyDao replyDao;
	
	@Override
	public List<Reply> queryReplies(int id) {
		return replyDao.queryReplies(id);
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public void addReply(int invid, String author, String content) {
		replyDao.addReply(invid, author, content);
	}

}
