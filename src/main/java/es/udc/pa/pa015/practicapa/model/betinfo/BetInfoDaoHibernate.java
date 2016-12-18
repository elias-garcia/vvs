package es.udc.pa.pa015.practicapa.model.betinfo;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This class implements BetInfoDao interface.
 */
@Repository("betInfoDao")
public class BetInfoDaoHibernate extends GenericDaoHibernate<BetInfo, Long>
    implements BetInfoDao {

  /**
   * This method find a bet by an user id.
   * @param userId
   *          user to search
   * @param startindex
   *          number of first element to list
   * @param count
   *          number of elements to list
   * @return BetInfo list
   */
  @SuppressWarnings("unchecked")
  public final List<BetInfo> findBetsByUserId(final Long userId,
      final int startindex,
      final int count) {

    return getSession().createQuery("SELECT b " + "FROM BetInfo b "
        + "WHERE b.user.userProfileId = :userId " + "ORDER BY b.betDate DESC")
        .setParameter("userId", userId).setFirstResult(startindex)
        .setMaxResults(count).list();
  }
}
