package es.udc.pa.pa015.practicapa.model.eventinfo;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository("eventInfoDao")
public class EventInfoDaoHibernate extends GenericDaoHibernate<EventInfo, Long> implements EventInfoDao {

	@SuppressWarnings("unchecked")
	public List<EventInfo> findEvents(String keywords, Long categoryId,
			boolean eventsStarted, int startIndex, int count) {
		
		String[] words = keywords != null ? keywords.split(" ") : null;
        String queryString = "SELECT e FROM EventInfo e";
        
        if (words != null && words.length > 0) {
            queryString += " WHERE";
            for (int i = 0; i < words.length; i++) {
                if (i > 0) {
                    queryString += " OR";
                }
                queryString += " LOWER(e.eventName) LIKE :word" + i;
            }
        }
        
        if (categoryId.longValue() != -1) {
        	if (words == null || words.length <= 0)
        		queryString += " WHERE e.category.categoryId = :categoryId";
        	else queryString += " AND e.category.categoryId = :categoryId";
        }
        
        if (!eventsStarted) {
        	if ((words == null || words.length <= 0) && categoryId.longValue() == -1)
        		queryString += " WHERE e.eventDate >= :eventDate";
        	else queryString += " AND e.eventDate >= :eventDate";
        }
        
        queryString += " ORDER BY e.eventDate ASC";
        
        Query query = getSession().createQuery(queryString);
        
        /* Fill keywords parameters */
        if (words != null && words.length > 0)
	        for (int i = 0; i < words.length; i++) {
	        	query.setParameter("word" + i, "%" + words[i].toLowerCase() + "%");
	        }
        
        /* Fill category parameter */
        if (categoryId.longValue() != -1)
        	query.setParameter("categoryId", categoryId);
        
        /* Fill date parameter */
        if (!eventsStarted)
        	query.setCalendar("eventDate", Calendar.getInstance());
        
        return query.setFirstResult(startIndex).setMaxResults(count).list();
	}
}