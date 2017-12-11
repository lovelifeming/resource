package org.mr.wu.service;

import java.util.List;

import org.mr.wu.model.Reply;

public interface ReplyService {
	
	List<Reply> queryReplies(int id);
	
	void addReply(int invid, String author, String content);
}
