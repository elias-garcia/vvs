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
	private final long ALL_CATEGORYS = -1;

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
		List<EventInfo> results = eventInfoDao.findEvents(null, ALL_CATEGORYS, true, 0, 4);
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
		persistentEventInfos.remove(3);
		persistentEventInfos.remove(2);
		/* Call */
		List<EventInfo> results = eventInfoDao.findEvents(KEYWORDS, ALL_CATEGORYS, true, 0, 4);
		/* Assertion */
		assertEquals(results, persistentEventInfos);
	}

	/**
	 * PR-UN-010.
	 */
	@Test
	public void testFindEventsFilteringWithCategoryInfo() {
		/* Setup */
		initializeEventInfos();
		persistentEventInfos.remove(3);
		persistentEventInfos.remove(1);
		/* Call */
		List<EventInfo> results = eventInfoDao.findEvents(null, persistentCategoryInfos.get(0).getCategoryId(), true, 0,
				4);
		/* Assertion */
		assertEquals(results, persistentEventInfos);
	}

	/**
	 * PR-UN-011
	 */
	@Test
	public void testFindEventsFilteringWithStartDate() {
		/* Setup */
		initializeEventInfos();
		persistentEventInfos.remove(1);
		persistentEventInfos.remove(0);
		/* Call */
		List<EventInfo> results = eventInfoDao.findEvents(null, ALL_CATEGORYS, false, 0, 4);
		/* Assertion */
		assertEquals(results, persistentEventInfos);
	}

	/**
	 * PR-UN-012
	 */
	@Test
	public void testFindEventsFilteringWithStartIndexAndCount() {
		/* Setup */
		initializeEventInfos();
		persistentEventInfos.remove(3);
		persistentEventInfos.remove(2);
		/* Call */
		List<EventInfo> results = eventInfoDao.findEvents(null, ALL_CATEGORYS, true, 0, 2);
		/* Assertion */
		assertEquals(results, persistentEventInfos);
	}

	/**
	 * PR-UN-058
	 */
	@Test
	public void testFindEventsFilteringWithTwoValidKeywordsAndCategoryIdAndStartedDate() {
		/* Setup */
		initializeEventInfos();

		/* Call */
		List<EventInfo> results = eventInfoDao.findEvents(EXISTENT_EVENT_NAME2,
				persistentCategoryInfos.get(0).getCategoryId(), false, 0, 2);
		/* Assertion */
		assertEquals(results.get(0), persistentEventInfos.get(2));
	}

}
