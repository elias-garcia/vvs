package es.udc.pa.pa015.practicapa.test.model.eventinfo;

import static es.udc.pa.pa015.practicapa.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa015.practicapa.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa015.practicapa.model.categoryinfo.CategoryInfo;
import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfo;
import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfoDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class EventInfoDaoUnitTest {

	private final String EXISTENT_CATEGORY_NAME0 = "Fútbol";
	private final String EXISTENT_CATEGORY_NAME1 = "Baloncesto";
	private final String EXISTENT_EVENT_NAME0 = "Real Madrid - Barcelona";
	private final String EXISTENT_EVENT_NAME1 = "Real Madrid - Barcelona";
	private final String EXISTENT_EVENT_NAME2 = "Deportivo - Alaves";
	private final String EXISTENT_EVENT_NAME3 = "Unicaja - Fuenlabrada";
	private final String KEYWORDS = "Ma";
	private final long NON_EXISTENT_CATEGORY_ID = -1;

	List<EventInfo> persistentEventInfos = new ArrayList<>();
	List<CategoryInfo> persistentCategoryInfos = new ArrayList<>();

	@Autowired
	private EventInfoDao eventInfoDao;

	@Autowired
	private SessionFactory sessionFactory;

	private Calendar getPastDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -1);
		return calendar;
	}

	private Calendar getFutureDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, 1);
		return calendar;
	}

	private void initializeEventInfos() {
		CategoryInfo categoryInfo1 = new CategoryInfo(EXISTENT_CATEGORY_NAME0);
		CategoryInfo categoryInfo2 = new CategoryInfo(EXISTENT_CATEGORY_NAME1);
		persistentCategoryInfos.add(categoryInfo1);
		persistentCategoryInfos.add(categoryInfo2);
		persistentEventInfos.add(new EventInfo(EXISTENT_EVENT_NAME0, getPastDate(), categoryInfo1));
		persistentEventInfos.add(new EventInfo(EXISTENT_EVENT_NAME1, getPastDate(), categoryInfo2));
		persistentEventInfos.add(new EventInfo(EXISTENT_EVENT_NAME2, getFutureDate(), categoryInfo1));
		persistentEventInfos.add(new EventInfo(EXISTENT_EVENT_NAME3, getFutureDate(), categoryInfo2));
		sessionFactory.getCurrentSession().saveOrUpdate(categoryInfo1);
		sessionFactory.getCurrentSession().saveOrUpdate(categoryInfo2);
		sessionFactory.getCurrentSession().saveOrUpdate(persistentEventInfos.get(0));
		sessionFactory.getCurrentSession().saveOrUpdate(persistentEventInfos.get(1));
		sessionFactory.getCurrentSession().saveOrUpdate(persistentEventInfos.get(2));
		sessionFactory.getCurrentSession().saveOrUpdate(persistentEventInfos.get(3));
	}

	/**
	 * PR-UN-007
	 */
	@Test
	public void testFindEventsWithoutFilters() {
		/* Setup */
		initializeEventInfos();
		/* Call */
		List<EventInfo> results = eventInfoDao.findEvents(null, null, true, 0, 4);
		/* Assertion */
		assertEquals(results, persistentEventInfos);
	}

	/**
	 * PR-UN-008
	 */
	@Test
	public void testFindEventsFilteringWithKeywords() {
		/* Setup */
		initializeEventInfos();
		persistentEventInfos.remove(2);
		persistentEventInfos.remove(3);
		/* Call */
		List<EventInfo> results = eventInfoDao.findEvents(KEYWORDS, null, true, 0, 4);
		/* Assertion */
		assertEquals(results, persistentEventInfos);
	}

	/**
	 * PR-UN-009
	 * 
	 * @throws InstanceNotFoundException
	 */
	@Test(expected = InstanceNotFoundException.class)
	public void testFindEventsFilteringWithANonExistentCategoryId() {
		/* Setup */
		initializeEventInfos();
		/* Call */
		eventInfoDao.findEvents(null, NON_EXISTENT_CATEGORY_ID, true, 0, 4);
		/* Assertion */
		/* InstanceNotFoundException expected */
	}

	/**
	 * PR-UN-010
	 * 
	 * @throws InstanceNotFoundException
	 */
	@Test
	public void testFindEventsFilteringWithCategoryInfo() {
		/* Setup */
		initializeEventInfos();
		persistentEventInfos.remove(1);
		persistentEventInfos.remove(3);
		/* Call */
		List<EventInfo> results = eventInfoDao.findEvents(null, persistentCategoryInfos.get(0).getCategoryId(), true, 0,
				4);
		/* Assertion */
		assertEquals(results, persistentEventInfos);
	}

	/**
	 * PR-UN-011
	 * 
	 * @throws InstanceNotFoundException
	 */
	@Test
	public void testFindEventsFilteringWithStartDate() {
		/* Setup */
		initializeEventInfos();
		persistentEventInfos.remove(0);
		persistentEventInfos.remove(1);
		/* Call */
		List<EventInfo> results = eventInfoDao.findEvents(null, null, false, 0, 4);
		/* Assertion */
		assertEquals(results, persistentEventInfos);
	}

	/**
	 * PR-UN-012
	 * 
	 * @throws InstanceNotFoundException
	 */
	@Test
	public void testFindEventsFilteringWithStartInteAndCount() {
		/* Setup */
		initializeEventInfos();
		persistentEventInfos.remove(2);
		persistentEventInfos.remove(3);
		/* Call */
		List<EventInfo> results = eventInfoDao.findEvents(null, null, true, 0, 2);
		/* Assertion */
		assertEquals(results, persistentEventInfos);
	}

}