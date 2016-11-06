package es.udc.pa.pa015.practicapa.model.eventinfo;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository("eventInfoDao")
public class EventInfoDaoHibernate extends GenericDaoHibernate<EventInfo, Long> implements EventInfoDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<EventInfo> findEvents(String keyWords, Long categoryId, boolean eventsStarted, int startIndex,
			int count) {

		String[] words = keyWords != null ? keyWords.split(" ") : null;
		String hqlQuery = "SELECT e FROM EventInfo e";

		if (categoryId != null) {
			hqlQuery += " WHERE e.category.categoryId = :categoryId";
			if (words != null)
				hqlQuery += " AND ";
		}

		if (words != null && words.length > 0) {
			if (categoryId == null) {
				hqlQuery += " WHERE ";
			}
			for (int i = 0; i < words.length; i++) {
				if (i > 0) {
					hqlQuery += " AND";
				}
				hqlQuery += " LOWER(e.eventName) LIKE LOWER(:eventName" + i + ")";
			}
		}

		Calendar eventDate = Calendar.getInstance();
		if ((words == null) && (categoryId == null)) {
			if (!eventsStarted) {
				hqlQuery += " WHERE e.eventDate >= :eventDate";
			}
		} else {
			if (!eventsStarted) {
				hqlQuery += " AND e.eventDate >= :eventDate";
			}
		}

		Query queryHql = getSession().createQuery(hqlQuery + " ORDER BY e.eventDate, e.eventName");

		if (categoryId != null) {
			queryHql.setParameter("categoryId", categoryId);
		}

		if (words != null && words.length > 0) {
			for (int i = 0; i < words.length; i++) {
				queryHql.setString("eventName" + i + "", "%" + words[i] + "%");
			}
		}

		if (!eventsStarted) {
			queryHql.setParameter("eventDate", eventDate);
		}

		return queryHql.setFirstResult(startIndex).setMaxResults(count).list();
	}
}