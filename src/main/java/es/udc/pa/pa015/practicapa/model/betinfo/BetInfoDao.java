package es.udc.pa.pa015.practicapa.model.betinfo;

import es.udc.pojo.modelutil.dao.GenericDao;

import java.util.List;

/**
 * Interface of the bet info dao.
 */
public interface BetInfoDao extends GenericDao<BetInfo, Long> {

  /**
   * This method find the bets by an user id.
   * @param userId
   *          User id to search
   * @param startindex
   *          Number of element where start the list
   * @param count
   *          Number of elements
   * @return List of betInfo
   */
  public List<BetInfo> findBetsByUserId(Long userId, int startindex, int count);
}
