package org.mr.wu.dao.impl;

import java.sql.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.mr.wu.dao.ReplyDao;
import org.mr.wu.model.Invitation;
import org.mr.wu.model.Reply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

@Repository("replyDao")
public class ReplyDaoImpl extends HibernateDaoSupport implements ReplyDao{
	
	@Override
	public List<Reply> queryReplies(int id) {
		String hql = "from Reply r where r.invitation.id=?";
		Query query = getSessionFactory().getCurrentSession().createQuery(hql);
		query.setParameter(0, id);
		return query.list();
	}

	@Override
	public void deleteReplies(int id) {
		List<Reply> replies = queryReplies(id);
		getHibernateTemplate().deleteAll(replies);
	}

	@Override
	public void addReply(int invid, String author, String content) {
		Reply reply = new Reply();
		reply.setAuthor(author);
		reply.setContent(content);
		reply.setCreateDate(new Date(System.currentTimeMillis()));
		Session sesion = getSessionFactory().getCurrentSession();
		Invitation invitation = (Invitation) sesion.load(Invitation.class, invid);
		reply.setInvitation(invitation);
		sesion.save(reply);
	}

}
