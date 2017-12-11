package org.mr.wu.dao;

import java.util.List;

import org.mr.wu.model.Reply;

public interface ReplyDao {

	List<Reply> queryReplies(int id);
	
	void deleteReplies(int id);
	
	void addReply(int invid, String author, String content);
}
