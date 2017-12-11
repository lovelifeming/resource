package org.mr.wu.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.mr.wu.dao.InvitationDao;
import org.mr.wu.model.Invitation;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

@Repository("invitationDao")
public class InvitationDaoImpl extends HibernateDaoSupport implements InvitationDao{

	@Override
	public List<Invitation> queryInvitations(String title, int pageIndex) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria c = session.createCriteria(Invitation.class);
		if(title != null && !"".equals(title))
			c.add(Restrictions.ilike("title", title, MatchMode.ANYWHERE));
		c.addOrder(Order.desc("createDate"));
		c.setFirstResult((pageIndex-1)*4);
		c.setMaxResults(4);
		return c.list();
	}

	@Override
	public int getInvitationCount(String title) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria c = session.createCriteria(Invitation.class);
		if(title != null && !"".equals(title))
			c.add(Restrictions.ilike("title", title, MatchMode.ANYWHERE));
		c.setProjection(Projections.rowCount());
		return ((Long) c.uniqueResult()).intValue();
	}

	@Override
	public void deleteInvitation(int id) {
		Session session = getSessionFactory().getCurrentSession();
		Invitation invitation = (Invitation) session.get(Invitation.class, id);
		if(invitation != null){
//			Transaction ts = session.beginTransaction();
			session.delete(invitation);
//			ts.commit();
		}
		
	}

}
