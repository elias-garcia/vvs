package es.udc.pa.pa015.practicapa.test.model.eventservice;

import static es.udc.pa.pa015.practicapa.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa015.practicapa.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa015.practicapa.model.betservice.TypeNotMultipleException;
import es.udc.pa.pa015.practicapa.model.bettype.BetType;
import es.udc.pa.pa015.practicapa.model.bettype.BetTypeDao;
import es.udc.pa.pa015.practicapa.model.categoryinfo.CategoryInfo;
import es.udc.pa.pa015.practicapa.model.categoryinfo.CategoryInfoDao;
import es.udc.pa.pa015.practicapa.model.eventService.EventDateException;
import es.udc.pa.pa015.practicapa.model.eventService.EventInfoBlock;
import es.udc.pa.pa015.practicapa.model.eventService.EventService;
import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfo;
import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfoDao;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOption;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE,
		SPRING_CONFIG_TEST_FILE }) 
@Transactional
public class EventServiceTest {

	private final static Long NON_EXISTENT_CATEGORY_ID = (long) -1;
	private final static Long NON_EXISTENT_EVENT_ID = (long) -1;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private EventInfoDao eventInfoDao;
	
	@Autowired
	private CategoryInfoDao categoryInfoDao;
	
	@Autowired
	private BetTypeDao betTypeDao;
	
	@Test
	public void testCreateAndFindEvent()
			throws InstanceNotFoundException, EventDateException {
		
		Calendar after = Calendar.getInstance();
		after.add(Calendar.WEEK_OF_YEAR, 1);
		
		CategoryInfo category = new CategoryInfo("categoria1");
		categoryInfoDao.save(category);
		
		EventInfo event = eventService.createEvent("evento1", after,
				category.getCategoryId());
		
		EventInfo finded = eventInfoDao.find(event.getEventId());
		
		assertEquals(event, finded);
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void testCreateEventWithNonExistenCateogryId()
			throws InstanceNotFoundException, EventDateException {
	
		Calendar after = Calendar.getInstance();
		after.add(Calendar.WEEK_OF_YEAR, 1);
		
		eventService.createEvent("evento1", after, NON_EXISTENT_CATEGORY_ID);
	}
	
	@Test(expected = EventDateException.class)
	public void testCreateEventWithInvalidDate()
			throws InstanceNotFoundException, EventDateException {
	
		Calendar before = Calendar.getInstance();
		before.add(Calendar.WEEK_OF_YEAR, -1);
		
		CategoryInfo category = new CategoryInfo("categoria1");
		categoryInfoDao.save(category);
		
		eventService.createEvent("evento1", before, category.getCategoryId());
	}
	
	@Test
	public void testFindEventsNotStartedByKeywords()
			throws InstanceNotFoundException, EventDateException {
		
		Calendar after = Calendar.getInstance();
		after.add(Calendar.WEEK_OF_YEAR, 1);

		CategoryInfo category = new CategoryInfo("categoria1");
		categoryInfoDao.save(category);

		EventInfo event1 = eventService.createEvent("evento 1", after,
				category.getCategoryId());
		EventInfo event2 = eventService.createEvent("evento 2", after,
				category.getCategoryId());

		EventInfoBlock finded1 = eventService.findEvents("eVe",
				NON_EXISTENT_CATEGORY_ID , false, 0, 10);
		
		assertEquals(event1, finded1.getEvents().get(0));
		assertEquals(event2, finded1.getEvents().get(1));
	}

	@Test
	public void testFindEventsNotStartedByCategory()
			throws InstanceNotFoundException, EventDateException {
		
		Calendar after = Calendar.getInstance();
		after.add(Calendar.WEEK_OF_YEAR, 1);
		
		CategoryInfo category = new CategoryInfo("categoria1");
		categoryInfoDao.save(category);

		EventInfo event1 = eventService.createEvent("evento1", after,
				category.getCategoryId());
		EventInfo event2 = eventService.createEvent("evento2", after,
				category.getCategoryId());
		
		EventInfoBlock finded1 = eventService.findEvents(null,
				category.getCategoryId(), false, 0, 10);	
		
		assertEquals(event1, finded1.getEvents().get(0));
		assertEquals(event2, finded1.getEvents().get(1));
	}
	
	@Test
	public void testFindEventsNotStartedByCategoryAndKeywords()
			throws InstanceNotFoundException, EventDateException {
		
		Calendar after = Calendar.getInstance();
		after.add(Calendar.WEEK_OF_YEAR, 1);
		
		CategoryInfo category = new CategoryInfo("categoria1");
		categoryInfoDao.save(category);

		EventInfo event1 = eventService.createEvent("evento1", after,
				category.getCategoryId());
		
		EventInfoBlock finded1 = eventService.findEvents("evento1",
				category.getCategoryId(), false, 0, 10);	
		assertEquals(event1, finded1.getEvents().get(0));
	}

	@Test
	public void testFindEventsStartedByKeywords()
			throws InstanceNotFoundException, EventDateException {
		
		Calendar before = Calendar.getInstance();
		before.add(Calendar.WEEK_OF_YEAR, -1);

		CategoryInfo category = new CategoryInfo("categoria1");
		categoryInfoDao.save(category);

		EventInfo event1 = new EventInfo("evento 1", before, category);
		eventInfoDao.save(event1);
		EventInfo event2 = new EventInfo("evento 2", before, category);
		eventInfoDao.save(event2);
		
		EventInfoBlock finded1 = eventService.findEvents("eVe",
				NON_EXISTENT_CATEGORY_ID , true, 0, 10);
		
		assertEquals(event1, finded1.getEvents().get(0));
		assertEquals(event2, finded1.getEvents().get(1));
	}

	@Test
	public void testFindEventsStartedByCategory()
			throws InstanceNotFoundException, EventDateException {
		
		Calendar before = Calendar.getInstance();
		before.add(Calendar.WEEK_OF_YEAR, -1);
		
		CategoryInfo category = new CategoryInfo("categoria1");
		categoryInfoDao.save(category);

		EventInfo event1 = new EventInfo("evento 1", before, category);
		eventInfoDao.save(event1);
		EventInfo event2 = new EventInfo("evento 2", before, category);
		eventInfoDao.save(event2);
		
		EventInfoBlock finded1 = eventService.findEvents(null,
				category.getCategoryId(), true, 0, 10);	
		
		assertEquals(event1, finded1.getEvents().get(0));
		assertEquals(event2, finded1.getEvents().get(1));
	}
	
	@Test
	public void testFindEventsStartedByCategoryAndKeywords()
			throws InstanceNotFoundException, EventDateException {
		
		Calendar before = Calendar.getInstance();
		before.add(Calendar.WEEK_OF_YEAR, -1);
		
		CategoryInfo category = new CategoryInfo("categoria1");
		categoryInfoDao.save(category);

		EventInfo event1 = new EventInfo("evento 1", before, category);
		eventInfoDao.save(event1);
		
		EventInfoBlock finded1 = eventService.findEvents("evEn",
				category.getCategoryId(), true, 0, 10);	
		
		assertEquals(event1, finded1.getEvents().get(0));
	}
	
	@Test
	public void testFindAllEvents() throws InstanceNotFoundException, EventDateException {
		
		Calendar after = Calendar.getInstance();
		after.add(Calendar.WEEK_OF_YEAR, 1);
		
		Calendar before = Calendar.getInstance();
		before.add(Calendar.WEEK_OF_YEAR, -1);
		
		CategoryInfo category = new CategoryInfo("categoria1");
		categoryInfoDao.save(category);
		
		EventInfo event1 = new EventInfo("evento 1", before, category);
		eventInfoDao.save(event1);
		EventInfo event2 = new EventInfo("evento 2", before, category);
		eventInfoDao.save(event2);
		
		EventInfoBlock finded = eventService.findEvents("event",
				category.getCategoryId(), true, 0, 10);	
		
		assertEquals(2, finded.getEvents().size());
	}
	
	@Test
	public void testFindEventsPaginationWithMoreElements()
		throws InstanceNotFoundException, EventDateException {
		
		Calendar after = Calendar.getInstance();
		after.add(Calendar.WEEK_OF_YEAR, 1);
		
		CategoryInfo category = new CategoryInfo("categoria1");
		categoryInfoDao.save(category);
		
		eventService.createEvent("evento1", after, category.getCategoryId());
		eventService.createEvent("evento2", after, category.getCategoryId());
		eventService.createEvent("evento3", after, category.getCategoryId());
		eventService.createEvent("evento4", after, category.getCategoryId());
		
		EventInfoBlock events = eventService.findEvents("event",
				category.getCategoryId(), true, 0, 3);
		
		assertTrue(events.getExistMoreEvents());		
	}

	@Test
	public void testFindEventsPaginationWithoutMoreElements()
		throws InstanceNotFoundException, EventDateException {
		
		Calendar after = Calendar.getInstance();
		after.add(Calendar.WEEK_OF_YEAR, 1);
		
		CategoryInfo category = new CategoryInfo("categoria1");
		categoryInfoDao.save(category);
		
		eventService.createEvent("evento1", after, category.getCategoryId());
		eventService.createEvent("evento2", after, category.getCategoryId());
		eventService.createEvent("evento3", after, category.getCategoryId());
		eventService.createEvent("evento4", after, category.getCategoryId());
		
		EventInfoBlock events = eventService.findEvents("event",
				category.getCategoryId(), true, 0, 4);
		
		assertFalse(events.getExistMoreEvents());	
	}

	@Test
	public void testFindEventsEmpty() throws InstanceNotFoundException {
		
		EventInfoBlock finded = eventService.findEvents("evento1",
				NON_EXISTENT_CATEGORY_ID, false, 0, 10);
		
		assertTrue(finded.getEvents().isEmpty());
	}
	
	@Test
	public void addBetType() throws InstanceNotFoundException, EventDateException {
		
		Calendar now = Calendar.getInstance();
		now.add(Calendar.WEEK_OF_YEAR, 1);
		
		CategoryInfo category = new CategoryInfo("categoria1");
		categoryInfoDao.save(category);
		
		EventInfo event = eventService.createEvent("Barça-Madrid",
				now, category.getCategoryId());
		
		BetType type = new BetType("¿Quien ganará?", true, event);
		
		List<TypeOption> options = new ArrayList<TypeOption>();
		options.add(new TypeOption(1.20, "Cristiano Ronaldo", type));
		options.add(new TypeOption(1.20, "Lionel Messi", type));
		
		eventService.addBetType(event.getEventId(), type, options);
		
		List<BetType> eventTypes = new ArrayList<BetType>(event.getBetTypes());
		List <TypeOption> typeOptions =
				new ArrayList<TypeOption>(eventTypes.get(0).getTypeOptions());
		
		assertEquals(options.get(0), typeOptions.get(0));
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void addBetTypeToNonExistentEvent()
			throws InstanceNotFoundException, EventDateException {
		
		Calendar now = Calendar.getInstance();
		now.add(Calendar.WEEK_OF_YEAR, 1);
		
		CategoryInfo category = new CategoryInfo("categoria1");
		categoryInfoDao.save(category);
		
		EventInfo event = eventService.createEvent("Barça-Madrid",
				now, category.getCategoryId());
		
		BetType type = new BetType("¿Quien ganará?", true, event);
		
		List<TypeOption> options = new ArrayList<TypeOption>();
		options.add(new TypeOption(1.20, "Cristiano Ronaldo", type));
		options.add(new TypeOption(1.20, "Lionel Messi", type));
		
		eventService.addBetType(NON_EXISTENT_EVENT_ID, type, options);
	}
	
	@Test
	public void testPickWinnersNull() 
			throws InstanceNotFoundException, TypeNotMultipleException, EventDateException {
		
		Calendar betDateAfter = Calendar.getInstance();
		betDateAfter.add(Calendar.WEEK_OF_YEAR, 1);
		
		CategoryInfo category = new CategoryInfo("categoria1");
		categoryInfoDao.save(category);
		
		EventInfo event = eventService.createEvent("Barça-Madrid", betDateAfter,
				category.getCategoryId());
		
		BetType type = new BetType("¿Quien ganará?", false, event);
		
		List<TypeOption> options = new ArrayList<TypeOption>();
		TypeOption option1 = new TypeOption(1.20, "Barça", type);
		options.add(option1);
		TypeOption option2 = new TypeOption(10, "Real Madrid", type);
		options.add(option2);
		
		eventService.addBetType(event.getEventId(), type, options);
		
		eventService.pickWinners(null, type.getTypeId());
		
		assertFalse(option1.getIsWinner());
		assertFalse(option2.getIsWinner());
		assertTrue(type.getPickedWinners());
	}
	
	@Test
	public void testPickWinner() 
			throws InstanceNotFoundException, TypeNotMultipleException, EventDateException {
		
		Calendar betDateAfter = Calendar.getInstance();
		betDateAfter.add(Calendar.WEEK_OF_YEAR, 1);
		
		CategoryInfo category = new CategoryInfo("categoria1");
		categoryInfoDao.save(category);
		
		EventInfo event = eventService.createEvent("Barça-Madrid", betDateAfter,
				category.getCategoryId());
		
		BetType type = new BetType("¿Quien ganará?", true, event);
		
		List<TypeOption> options = new ArrayList<TypeOption>();
		TypeOption option1 = new TypeOption(1.20, "Barça", type);
		options.add(option1);
		TypeOption option2 = new TypeOption(10, "Real Madrid", type);
		options.add(option2);
		
		eventService.addBetType(event.getEventId(), type, options);
		
		List<Long> optionsIds = new ArrayList<Long>();
		optionsIds.add(option1.getOptionId());
		optionsIds.add(option2.getOptionId());
		
		eventService.pickWinners(optionsIds, type.getTypeId());
		
		assertTrue(option1.getIsWinner());
		assertTrue(option2.getIsWinner());
		assertTrue(type.getPickedWinners());
	}
	
	@Test
	public void testPickOneWinner() 
			throws InstanceNotFoundException, TypeNotMultipleException, EventDateException {
		
		Calendar betDateAfter = Calendar.getInstance();
		betDateAfter.add(Calendar.WEEK_OF_YEAR, 1);
		
		CategoryInfo category = new CategoryInfo("categoria1");
		categoryInfoDao.save(category);
		
		EventInfo event = eventService.createEvent("Barça-Madrid", betDateAfter,
				category.getCategoryId());
		
		BetType type = new BetType("¿Quien ganará?", false, event);
		
		List<TypeOption> options = new ArrayList<TypeOption>();
		TypeOption option1 = new TypeOption(1.20, "Barça", type);
		options.add(option1);
		TypeOption option2 = new TypeOption(10, "Real Madrid", type);
		options.add(option2);
		
		eventService.addBetType(event.getEventId(), type, options);
		
		List<Long> optionsIds = new ArrayList<Long>();
		optionsIds.add(option1.getOptionId());
		
		eventService.pickWinners(optionsIds, type.getTypeId());
		
		assertTrue(option1.getIsWinner());
		assertFalse(option2.getIsWinner());
		assertTrue(type.getPickedWinners());
	}
	
	@Test(expected = TypeNotMultipleException.class)
	public void testPickWinnerTypeNotMultiple() 
			throws InstanceNotFoundException, TypeNotMultipleException, EventDateException {
		
		Calendar betDateAfter = Calendar.getInstance();
		betDateAfter.add(Calendar.WEEK_OF_YEAR, 1);
		
		CategoryInfo category = new CategoryInfo("categoria1");
		categoryInfoDao.save(category);
		
		EventInfo event = eventService.createEvent("Barça-Madrid", betDateAfter,
				category.getCategoryId());
		
		BetType type = new BetType("¿Quien ganará?", false, event);
		
		List<TypeOption> options = new ArrayList<TypeOption>();
		TypeOption option1 = new TypeOption(1.20, "Barça", type);
		options.add(option1);
		TypeOption option2 = new TypeOption(10, "Real Madrid", type);
		options.add(option2);
		
		eventService.addBetType(event.getEventId(), type, options);
		
		List<Long> optionsIds = new ArrayList<Long>();
		optionsIds.add(option1.getOptionId());
		optionsIds.add(option2.getOptionId());
		
		eventService.pickWinners(optionsIds, type.getTypeId());
		
		assertTrue(option1.getIsWinner());
		assertTrue(option2.getIsWinner());
	}

}