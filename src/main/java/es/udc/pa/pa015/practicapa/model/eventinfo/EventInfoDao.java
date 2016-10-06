package es.udc.pa.pa015.practicapa.model.eventinfo;

import java.util.List;
import es.udc.pojo.modelutil.dao.GenericDao;

public interface EventInfoDao extends GenericDao<EventInfo, Long> {
	
	public List<EventInfo> findEvents(String keywords, Long categoryId,
			boolean eventsStarted, int startIndex, int count);
}