package es.udc.pa.pa015.practicapa.test.model.event;

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
import es.udc.pa.pa015.practicapa.model.eventService.EventDateException;
import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfo;
import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfoDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class EventInfoDaoUnitTest {

	private final static Long NON_EXISTENT_CATEGORY_ID = (long) -1;

	@Autowired
	private EventInfoDao eventInfoDao;

	@Autowired
	private SessionFactory sessionFactory;

	CategoryInfo category1;
	CategoryInfo category2;
	EventInfo eventNoStarted1;
	EventInfo eventNoStarted2;
	EventInfo eventStarted1;
	EventInfo eventStarted2;

	private void initializeCategories() {
		category1 = new CategoryInfo("category1");
		category2 = new CategoryInfo("category2");
		sessionFactory.getCurrentSession().saveOrUpdate(category1);
		sessionFactory.getCurrentSession().saveOrUpdate(category2);
	}

	private void initializeEvents() {
		Calendar after = Calendar.getInstance();
		after.add(Calendar.WEEK_OF_YEAR, 1);
		eventNoStarted1 = new EventInfo("event1", after, category1);
		eventNoStarted2 = new EventInfo("event2 key", after, category2);
		sessionFactory.getCurrentSession().saveOrUpdate(eventNoStarted1);
		sessionFactory.getCurrentSession().saveOrUpdate(eventNoStarted2);
		Calendar before = Calendar.getInstance();
		before.add(Calendar.WEEK_OF_YEAR, -1);
		eventStarted1 = new EventInfo("event1 key", before, category1);
		eventStarted2 = new EventInfo("event2 key", before, category2);
		sessionFactory.getCurrentSession().saveOrUpdate(eventStarted1);
		sessionFactory.getCurrentSession().saveOrUpdate(eventStarted2);
	}
	
	/**
	/**
	 * PR-UN-00X
	 * 
	 */
	
	@Test
	public void testFindAllStratedEvents(){

		/* Setup */
		initializeCategories();
		initializeEvents();
		
		List<EventInfo> eventsCreated = new ArrayList<EventInfo>();
		
		eventsCreated.add(eventStarted2);
		
		List<EventInfo> eventsFound = new ArrayList<EventInfo>();
		
		/* Call */
		eventsFound = eventInfoDao.findEvents(null, NON_EXISTENT_CATEGORY_ID, true, 1, 4);
		
		/* Assertion */
		assertEquals(eventsFound,eventsCreated);
		assertEquals(1, eventsFound.size());
	}

	/**
	/**
	 * PR-UN-00X
	 * 
	 */
	
	@Test
	public void testFindEventsStartedByKeywords() {

		/* Setup */
		initializeCategories();
		initializeEvents();
		
		List<EventInfo> eventsStartedCreated = new ArrayList<EventInfo>();
		
		eventsStartedCreated.add(eventStarted1);
		eventsStartedCreated.add(eventStarted2);
		
		List<EventInfo> eventsFound = new ArrayList<EventInfo>();
		
		/* Call */
		eventsFound = eventInfoDao.findEvents(null, NON_EXISTENT_CATEGORY_ID, true, 0, 4);
		
		/* Assertion */
		assertEquals(eventsFound,eventsStartedCreated);
	}

	/**
	/**
	 * PR-UN-00X
	 * 
	 */
	
	@Test
	public void testFindEventsNoStartedByCategory() throws InstanceNotFoundException, EventDateException {

		/* Setup */
		initializeCategories();
		initializeEvents();
		
		List<EventInfo> eventsNoStartedCreated = new ArrayList<EventInfo>();
		
		eventsNoStartedCreated.add(eventNoStarted1);
		
		List<EventInfo> eventsFound = new ArrayList<EventInfo>();
		
		/* Call */
		eventsFound = eventInfoDao.findEvents(null, category1.getCategoryId(), false, 0, 4);
		
		/* Assertion */
		assertEquals(eventsFound,eventsNoStartedCreated);
	}

	/**
	/**
	 * PR-UN-00X
	 * 
	 */
	
	@Test
	public void testFindEventsStartedByCategory() throws InstanceNotFoundException, EventDateException {

		/* Setup */
		initializeCategories();
		initializeEvents();
		
		List<EventInfo> eventsStartedCreated = new ArrayList<EventInfo>();
		
		eventsStartedCreated.add(eventStarted1);
		
		List<EventInfo> eventsFound = new ArrayList<EventInfo>();
		
		/* Call */
		eventsFound = eventInfoDao.findEvents(null, category1.getCategoryId(), true, 0, 4);
		
		/* Assertion */
		assertEquals(eventsFound,eventsStartedCreated);
	}
	
	/**
	/**
	 * PR-UN-00X
	 * 
	 */
	
	@Test
	public void testFindAllEventsStartedByKeywordsWithCategory(){

		/* Setup */
		initializeCategories();
		initializeEvents();
		
		List<EventInfo> eventsCreated = new ArrayList<EventInfo>();
		
		eventsCreated.add(eventStarted2);
		
		List<EventInfo> eventsFound = new ArrayList<EventInfo>();
		
		/* Call */
		eventsFound = eventInfoDao.findEvents("key", category2.getCategoryId(), true, 0, 4);
		
		/* Assertion */
		assertEquals(eventsFound,eventsCreated);
	}

}