package es.udc.pa.pa015.practicapa.model.betinfo;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository("betInfoDao")
public class BetInfoDaoHibernate extends GenericDaoHibernate<BetInfo,Long> implements BetInfoDao {

	@SuppressWarnings("unchecked")
	public List<BetInfo> findBetsByUserId(Long userId,
			int startindex, int count) {
		
		return getSession().createQuery(
				"SELECT b " +
				"FROM BetInfo b " +
				"WHERE b.user.userProfileId = :userId " +
				"ORDER BY b.betDate DESC").
				setParameter("userId", userId).
				setFirstResult(startindex).
				setMaxResults(count).
				list();
	}
}