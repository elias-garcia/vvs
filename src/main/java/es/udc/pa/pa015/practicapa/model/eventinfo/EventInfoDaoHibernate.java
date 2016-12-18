package es.udc.pa.pa015.practicapa.model.eventinfo;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.List;

/**
 * Event repository.
 */
@Repository("eventInfoDao")
public class EventInfoDaoHibernate extends GenericDaoHibernate<EventInfo, Long>
    implements EventInfoDao {

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
  @SuppressWarnings("unchecked")
  public final List<EventInfo> findEvents(final String keywords,
      final Long categoryId, final boolean eventsStarted,
      final int startIndex, final int count) {

    String[] words = keywords != null ? keywords.split(" ") : null;
    String queryString = "SELECT e FROM EventInfo e";

    if (words != null && words.length > 0) {
      queryString += " WHERE";
      for (int i = 0; i < words.length; i++) {
        if (i > 0) {
          queryString += " AND";
        }
        queryString += " LOWER(e.eventName) LIKE :word" + i;
      }
    }

    if (categoryId.longValue() != -1) {
      if (words == null || words.length <= 0) {
        queryString += " WHERE e.category.categoryId = :categoryId";
      } else {
        queryString += " AND e.category.categoryId = :categoryId";
      }
    }

    if (!eventsStarted) {
      if ((words == null || words.length <= 0)
          && categoryId.longValue() == -1) {
        queryString += " WHERE e.eventDate >= :eventDate";
      } else {
        queryString += " AND e.eventDate >= :eventDate";
      }
    }

    queryString += " ORDER BY e.eventDate ASC";

    Query query = getSession().createQuery(queryString);

    /* Fill keywords parameters */
    if (words != null && words.length > 0) {
      for (int i = 0; i < words.length; i++) {
        query.setParameter("word" + i, "%" + words[i].toLowerCase() + "%");
      }
    }

    /* Fill category parameter */
    if (categoryId.longValue() != -1) {
      query.setParameter("categoryId", categoryId);
    }

    /* Fill date parameter */
    if (!eventsStarted) {
      query.setCalendar("eventDate", Calendar.getInstance());
    }

    return query.setFirstResult(startIndex).setMaxResults(count).list();
  }
}
