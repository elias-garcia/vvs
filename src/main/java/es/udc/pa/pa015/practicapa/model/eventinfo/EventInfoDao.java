package es.udc.pa.pa015.practicapa.model.eventinfo;

import es.udc.pojo.modelutil.dao.GenericDao;

import java.util.List;

/**
 * EventInfoDao.
 */
public interface EventInfoDao extends GenericDao<EventInfo, Long> {

  /**
   * Method that find all events.
   * @param keywords
   *            keywords to search
   * @param categoryId
   *            categoryId to search
   * @param eventsStarted
   *            indicates if the events have started
   * @param startIndex
   *            number of the first element of the list
   * @param count
   *            number of elements
   * @return list of events
   */
  List<EventInfo> findEvents(String keywords, Long categoryId,
      boolean eventsStarted, int startIndex, int count);
}
