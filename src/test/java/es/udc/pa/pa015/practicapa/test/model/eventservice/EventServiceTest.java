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

import es.udc.pa.pa015.practicapa.model.bettype.BetType;
import es.udc.pa.pa015.practicapa.model.bettype.BetTypeDao;
import es.udc.pa.pa015.practicapa.model.categoryinfo.CategoryInfo;
import es.udc.pa.pa015.practicapa.model.categoryinfo.CategoryInfoDao;
import es.udc.pa.pa015.practicapa.model.eventService.DuplicatedResultTypeOptionsException;
import es.udc.pa.pa015.practicapa.model.eventService.EventDateException;
import es.udc.pa.pa015.practicapa.model.eventService.EventInfoBlock;
import es.udc.pa.pa015.practicapa.model.eventService.EventService;
import es.udc.pa.pa015.practicapa.model.eventService.NoAssignedTypeOptionsException;
import es.udc.pa.pa015.practicapa.model.eventService.NullEventNameException;
import es.udc.pa.pa015.practicapa.model.eventService.StartIndexOrCountException;
import es.udc.pa.pa015.practicapa.model.eventService.TypeNotMultipleException;
import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfo;
import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfoDao;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOption;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOptionDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class EventServiceTest {

	private final String EXISTENT_CATEGORY_NAME0 = "Baloncesto";
	private final String EXISTENT_CATEGORY_NAME1 = "Fútbol";
	private final String EXISTENT_EVENT_NAME0 = "Real Madrid - Barcelona";
	private final String EXISTENT_EVENT_NAME1 = "Real Madrid - Barcelona";
	private final String EXISTENT_EVENT_NAME2 = "Deportivo - Alaves";
	private final String EXISTENT_EVENT_NAME3 = "Unicaja - Fuenlabrada";
	private final long NON_EXISTENT_ID = -1;
	private static Calendar pastDate = null;
	private static Calendar futureDate = null;
	private static String BETTYPE_QUESTION = "¿Quién ganará el encuentro?";
	private static double TYPEOPTION_ODD = 1.0;
	private static String TYPEOPTION_RESULT1 = "Barcelona";
	private static String TYPEOPTION_RESULT2 = "Madrid";

	List<CategoryInfo> persistentCategoryInfos = new ArrayList<>();
	List<EventInfo> persistentEventInfos = new ArrayList<>();

	@Autowired
	private EventInfoDao eventInfoDao;

	@Autowired
	private CategoryInfoDao categoryInfoDao;

	@Autowired
	private BetTypeDao betTypeDao;

	@Autowired
	private TypeOptionDao typeOptionDao;

	@Autowired
	private EventService eventService;

	private Calendar getPastDate() {
		if (pastDate == null) {
			pastDate = Calendar.getInstance();
			pastDate.add(Calendar.HOUR, -1);
		}
		return pastDate;
	}

	private Calendar getFutureDate() {
		if (futureDate == null) {
			futureDate = Calendar.getInstance();
			futureDate.add(Calendar.HOUR, 1);
		}
		return futureDate;
	}

	private void initializeCategoryInfos() {
		persistentCategoryInfos.add(new CategoryInfo(EXISTENT_CATEGORY_NAME0));
		persistentCategoryInfos.add(new CategoryInfo(EXISTENT_CATEGORY_NAME1));
		categoryInfoDao.save(persistentCategoryInfos.get(0));
		categoryInfoDao.save(persistentCategoryInfos.get(1));
	}

	private void initializeEventInfos() {
		persistentEventInfos.add(new EventInfo(EXISTENT_EVENT_NAME0, getPastDate(), persistentCategoryInfos.get(0)));
		persistentEventInfos.add(new EventInfo(EXISTENT_EVENT_NAME1, getPastDate(), persistentCategoryInfos.get(1)));
		persistentEventInfos.add(new EventInfo(EXISTENT_EVENT_NAME2, getFutureDate(), persistentCategoryInfos.get(0)));
		persistentEventInfos.add(new EventInfo(EXISTENT_EVENT_NAME3, getFutureDate(), persistentCategoryInfos.get(1)));
		eventInfoDao.save(persistentEventInfos.get(0));
		eventInfoDao.save(persistentEventInfos.get(1));
		eventInfoDao.save(persistentEventInfos.get(2));
		eventInfoDao.save(persistentEventInfos.get(3));
	}

	private List<Long> getNonExistentTypeOptions() {
		List<Long> typeOptionsIds = new ArrayList<>();
		typeOptionsIds.add(NON_EXISTENT_ID);
		return typeOptionsIds;
	}

	private List<Long> getTypeOptionsIds(List<TypeOption> typeOptions) {
		List<Long> typeOptionsIds = new ArrayList<>();
		for (TypeOption typeOption : typeOptions)
			typeOptionsIds.add(typeOption.getOptionId());
		return typeOptionsIds;
	}

	/**
	 * PR-IT-021
	 */
	@Test
	public void testCreateAndFindEvent() throws InstanceNotFoundException, EventDateException, NullEventNameException {

		/* Setup */
		initializeCategoryInfos();
		initializeEventInfos();

		/* Call */
		EventInfo event = eventService.createEvent(EXISTENT_EVENT_NAME2, getFutureDate(),
				persistentCategoryInfos.get(0).getCategoryId());
		EventInfo finded = eventInfoDao.find(event.getEventId());

		/* Assertion */
		assertEquals(event, finded);

	}

	/**
	 * PR-IT-022
	 */
	@Test(expected = NullEventNameException.class)
	public void testCreateEventWithNullName()
			throws InstanceNotFoundException, EventDateException, NullEventNameException {
		/* Setup */
		initializeCategoryInfos();
		initializeEventInfos();

		/* Call */
		eventService.createEvent(null, getFutureDate(), persistentCategoryInfos.get(0).getCategoryId());

		/* Assertion */
		/* NullEventNameException expected */
	}

	/**
	 * PR-IT-023
	 */
	@Test(expected = EventDateException.class)
	public void testCreateEventWithNullDate()
			throws InstanceNotFoundException, EventDateException, NullEventNameException {

		/* Setup */
		initializeCategoryInfos();
		initializeEventInfos();

		/* Call */
		eventService.createEvent(EXISTENT_EVENT_NAME0, null, persistentCategoryInfos.get(0).getCategoryId());

		/* Assertion */
		/* EventDateException expected */
	}

	/**
	 * PR-IT-024
	 */
	@Test(expected = EventDateException.class)
	public void testCreateEventWithPastDate()
			throws InstanceNotFoundException, EventDateException, NullEventNameException {

		/* Setup */
		initializeCategoryInfos();
		initializeEventInfos();

		/* Call */
		eventService.createEvent(EXISTENT_EVENT_NAME0, getPastDate(), persistentCategoryInfos.get(0).getCategoryId());

		/* Assertion */
		/* EventDateException expected */
	}

	/**
	 * PR-IT-025
	 */
	@Test(expected = InstanceNotFoundException.class)
	public void testCreateEventWithNonExistentCategoryInfoId()
			throws InstanceNotFoundException, EventDateException, NullEventNameException {

		/* Setup */
		initializeCategoryInfos();
		initializeEventInfos();

		/* Call */
		eventService.createEvent(EXISTENT_EVENT_NAME0, getFutureDate(), NON_EXISTENT_ID);

		/* Assertion */
		/* InstanceNotFoundException expected */
	}

	/*************************************************************************/
	/*************************************************************************/
	/*************************************************************************/

	/**
	 * PR-IT-026
	 */
	@Test(expected = InstanceNotFoundException.class)
	public void testFindEventByNonExistentId() throws InstanceNotFoundException {

		/* Setup */
		initializeCategoryInfos();
		initializeEventInfos();

		/* Call */
		eventService.findEvent(NON_EXISTENT_ID);

		/* Assertion */
		/* InstanceNotFoundException expected */
	}

	/**
	 * PR-IT-027
	 */
	@Test
	public void testFindEventById() throws InstanceNotFoundException {

		/* Setup */
		initializeCategoryInfos();
		initializeEventInfos();

		/* Call */
		EventInfo event = eventService.findEvent(persistentEventInfos.get(0).getEventId());

		/* Assertion */
		assertEquals(event, persistentEventInfos.get(0));

	}

	/*************************************************************************/
	/*************************************************************************/
	/*************************************************************************/

	/**
	 * PR-UN-028
	 */
	@Test
	public void findEventsFilteringWithStartIndexAndCount1()
			throws InstanceNotFoundException, StartIndexOrCountException, EventDateException, NullEventNameException {

		/* Setup */
		initializeCategoryInfos();
		List<EventInfo> eventInfos = new ArrayList<>();
		eventInfos.add(eventService.createEvent(EXISTENT_EVENT_NAME0, getFutureDate(),
				persistentCategoryInfos.get(0).getCategoryId()));
		eventInfos.add(eventService.createEvent(EXISTENT_EVENT_NAME1, getFutureDate(),
				persistentCategoryInfos.get(0).getCategoryId()));
		eventInfos.add(eventService.createEvent(EXISTENT_EVENT_NAME2, getFutureDate(),
				persistentCategoryInfos.get(0).getCategoryId()));
		eventInfos.add(eventService.createEvent(EXISTENT_EVENT_NAME3, getFutureDate(),
				persistentCategoryInfos.get(0).getCategoryId()));
		/* Call */
		EventInfoBlock eventInfoBlock = eventService.findEvents(null, persistentCategoryInfos.get(0).getCategoryId(),
				true, 0, 3);

		/* Assertion */
		assertFalse(!eventInfoBlock.getExistMoreEvents());

	}

	/**
	 * PR-UN-029
	 */
	@Test
	public void findEventsFilteringWithStartIndexAndCount2()
			throws InstanceNotFoundException, StartIndexOrCountException, EventDateException, NullEventNameException {

		/* Setup */
		initializeCategoryInfos();
		List<EventInfo> eventInfos = new ArrayList<>();
		eventInfos.add(eventService.createEvent(EXISTENT_EVENT_NAME0, getFutureDate(),
				persistentCategoryInfos.get(0).getCategoryId()));
		eventInfos.add(eventService.createEvent(EXISTENT_EVENT_NAME1, getFutureDate(),
				persistentCategoryInfos.get(1).getCategoryId()));
		eventInfos.add(eventService.createEvent(EXISTENT_EVENT_NAME2, getFutureDate(),
				persistentCategoryInfos.get(0).getCategoryId()));
		eventInfos.add(eventService.createEvent(EXISTENT_EVENT_NAME3, getFutureDate(),
				persistentCategoryInfos.get(1).getCategoryId()));

		/* Call */
		EventInfoBlock eventInfoBlock = eventService.findEvents(null, persistentCategoryInfos.get(0).getCategoryId(),
				true, 0, 2);

		/* Assertion */
		assertFalse(eventInfoBlock.getExistMoreEvents());
	}

	/**
	 * PR-UN-030
	 */
	@Test(expected = StartIndexOrCountException.class)
	public void findEventsWithANegativeStartIdexOrCount()
			throws InstanceNotFoundException, StartIndexOrCountException, EventDateException, NullEventNameException {

		/* Setup */
		initializeCategoryInfos();
		List<EventInfo> eventInfos = new ArrayList<>();
		eventInfos.add(eventService.createEvent(EXISTENT_EVENT_NAME0, getFutureDate(),
				persistentCategoryInfos.get(0).getCategoryId()));
		eventInfos.add(eventService.createEvent(EXISTENT_EVENT_NAME1, getFutureDate(),
				persistentCategoryInfos.get(0).getCategoryId()));
		eventInfos.add(eventService.createEvent(EXISTENT_EVENT_NAME2, getFutureDate(),
				persistentCategoryInfos.get(0).getCategoryId()));
		eventInfos.add(eventService.createEvent(EXISTENT_EVENT_NAME3, getFutureDate(),
				persistentCategoryInfos.get(0).getCategoryId()));

		/* Call */
		eventService.findEvents(null, persistentCategoryInfos.get(0).getCategoryId(), true, -1, 0);

		/* Assertion */
		/* StartIndexOrCountException expected */

	}

	/*************************************************************************/
	/*************************************************************************/
	/*************************************************************************/

	/**
	 * PR-IT-031
	 */
	@Test
	public void testAddBetType()
			throws InstanceNotFoundException, NoAssignedTypeOptionsException, DuplicatedResultTypeOptionsException {

		/* Setup */
		initializeCategoryInfos();
		initializeEventInfos();
		BetType betType = new BetType(BETTYPE_QUESTION, false, persistentEventInfos.get(2));
		List<TypeOption> typeOptions = new ArrayList<>();
		typeOptions.add(new TypeOption(TYPEOPTION_ODD, TYPEOPTION_RESULT1, betType));

		/* Call */
		eventService.addBetType(persistentEventInfos.get(2).getEventId(), betType, typeOptions);

		EventInfo event = eventInfoDao.find(persistentEventInfos.get(2).getEventId());

		/* Assertion */
		/*
		 * Usamos for porque es un SET y no se pueden obtener directamente los
		 * objetos de la colección, necesitamos iterar
		 */
		for (BetType betTypeEvent : event.getBetTypes()) {
			assertEquals(betType, betTypeEvent);
			for (TypeOption typeOption : betTypeEvent.getTypeOptions())
				assertEquals(typeOption, typeOptions.get(0));
		}
	}

	/**
	 * PR-IT-032
	 */
	@Test(expected = InstanceNotFoundException.class)
	public void testAddBetTypeWithNonExistentEnventInfoId()
			throws InstanceNotFoundException, NoAssignedTypeOptionsException, DuplicatedResultTypeOptionsException {

		/* Setup */
		initializeCategoryInfos();
		initializeEventInfos();
		BetType betType = new BetType(BETTYPE_QUESTION, false, persistentEventInfos.get(2));
		List<TypeOption> typeOptions = new ArrayList<>();
		typeOptions.add(new TypeOption(TYPEOPTION_ODD, TYPEOPTION_RESULT1, betType));

		/* Call */
		eventService.addBetType(NON_EXISTENT_ID, betType, typeOptions);

		/* Assertion */
		/* InstanceNotFoundException expected */

	}

	/**
	 * PR-IT-033
	 */
	@Test(expected = NoAssignedTypeOptionsException.class)
	public void testAddBetTypeWithEmptyTypeOptions()
			throws InstanceNotFoundException, NoAssignedTypeOptionsException, DuplicatedResultTypeOptionsException {

		/* Setup */
		initializeCategoryInfos();
		initializeEventInfos();
		BetType betType = new BetType(BETTYPE_QUESTION, false, persistentEventInfos.get(2));
		List<TypeOption> typeOptions = new ArrayList<>();

		/* Call */
		eventService.addBetType(persistentEventInfos.get(2).getEventId(), betType, typeOptions);

		/* Assertion */
		/* NoAssignedTypeOptionsException expected */

	}

	/**
	 * PR-IT-034
	 */
	@Test(expected = DuplicatedResultTypeOptionsException.class)
	public void testAddBetTypeWithDuplicatedResultInTypeOptions()
			throws InstanceNotFoundException, NoAssignedTypeOptionsException, DuplicatedResultTypeOptionsException {

		/* Setup */
		initializeCategoryInfos();
		initializeEventInfos();
		BetType betType = new BetType(BETTYPE_QUESTION, false, persistentEventInfos.get(2));
		List<TypeOption> typeOptions = new ArrayList<>();
		typeOptions.add(new TypeOption(TYPEOPTION_ODD, TYPEOPTION_RESULT1, betType));
		typeOptions.add(new TypeOption(TYPEOPTION_ODD, TYPEOPTION_RESULT1, betType));

		/* Call */
		eventService.addBetType(persistentEventInfos.get(2).getEventId(), betType, typeOptions);

		/* Assertion */
		/* DuplicatedResultTypeOptionsException expected */

	}

	/*************************************************************************/
	/*************************************************************************/
	/*************************************************************************/

	/**
	 * PR-IT-035
	 */
	@Test(expected = InstanceNotFoundException.class)
	public void testPickWinnersWithNonExistentBetTypeId()
			throws InstanceNotFoundException, TypeNotMultipleException, EventDateException, NullEventNameException {

		/* Setup */
		initializeCategoryInfos();
		EventInfo event = eventService.createEvent(EXISTENT_CATEGORY_NAME0, getFutureDate(),
				persistentCategoryInfos.get(0).getCategoryId());
		BetType betType = new BetType(BETTYPE_QUESTION, true, event);
		betTypeDao.save(betType);
		List<TypeOption> options = new ArrayList<TypeOption>();
		TypeOption option1 = new TypeOption(TYPEOPTION_ODD, TYPEOPTION_RESULT1, betType);
		options.add(option1);
		typeOptionDao.save(option1);
		TypeOption option2 = new TypeOption(TYPEOPTION_ODD, TYPEOPTION_RESULT2, betType);
		options.add(option2);
		typeOptionDao.save(option2);

		/* Call */
		eventService.pickWinners(getTypeOptionsIds(options), NON_EXISTENT_ID);

		/* Assertion */
		/* InstanceNotFoundException expected */

	}

	/**
	 * PR-IT-036
	 */
	@Test
	public void testPickWinnersWithTypeOptionsNullList()
			throws InstanceNotFoundException, TypeNotMultipleException, EventDateException, NullEventNameException {

		/* Setup */
		initializeCategoryInfos();
		EventInfo event = eventService.createEvent(EXISTENT_CATEGORY_NAME0, getFutureDate(),
				persistentCategoryInfos.get(0).getCategoryId());
		BetType betType = new BetType(BETTYPE_QUESTION, true, event);
		betTypeDao.save(betType);
		List<TypeOption> options = new ArrayList<TypeOption>();
		TypeOption option1 = new TypeOption(TYPEOPTION_ODD, TYPEOPTION_RESULT1, betType);
		options.add(option1);
		typeOptionDao.save(option1);
		TypeOption option2 = new TypeOption(TYPEOPTION_ODD, TYPEOPTION_RESULT2, betType);
		options.add(option2);
		typeOptionDao.save(option2);

		/* Call */
		eventService.pickWinners(null, betType.getTypeId());

		/* Assertion */
		for (TypeOption typeOption : betType.getTypeOptions())
			assertTrue(!typeOption.getIsWinner());

	}

	/**
	 * PR-IT-037
	 */
	@Test
	public void testPickWinnersWithTypeOptionsEmptyList()
			throws InstanceNotFoundException, TypeNotMultipleException, EventDateException, NullEventNameException {

		/* Setup */
		initializeCategoryInfos();
		EventInfo event = eventService.createEvent(EXISTENT_CATEGORY_NAME0, getFutureDate(),
				persistentCategoryInfos.get(0).getCategoryId());
		BetType betType = new BetType(BETTYPE_QUESTION, true, event);
		betTypeDao.save(betType);
		List<TypeOption> options = new ArrayList<TypeOption>();
		TypeOption option1 = new TypeOption(TYPEOPTION_ODD, TYPEOPTION_RESULT1, betType);
		options.add(option1);
		typeOptionDao.save(option1);
		TypeOption option2 = new TypeOption(TYPEOPTION_ODD, TYPEOPTION_RESULT2, betType);
		options.add(option2);
		typeOptionDao.save(option2);

		/* Call */
		eventService.pickWinners(new ArrayList<Long>(), betType.getTypeId());

		/* Assertion */
		for (TypeOption typeOption : betType.getTypeOptions())
			assertTrue(!typeOption.getIsWinner());
	}

	/**
	 * PR-IT-038
	 */
	@Test(expected = InstanceNotFoundException.class)
	public void testPickWinnersWithNonExistentTypeOptions()
			throws InstanceNotFoundException, TypeNotMultipleException, EventDateException, NullEventNameException {

		/* Setup */
		initializeCategoryInfos();
		/* Setup */
		initializeCategoryInfos();
		EventInfo event = eventService.createEvent(EXISTENT_CATEGORY_NAME0, getFutureDate(),
				persistentCategoryInfos.get(0).getCategoryId());
		BetType betType = new BetType(BETTYPE_QUESTION, true, event);
		betTypeDao.save(betType);
		List<TypeOption> options = new ArrayList<TypeOption>();
		TypeOption option1 = new TypeOption(TYPEOPTION_ODD, TYPEOPTION_RESULT1, betType);
		options.add(option1);
		typeOptionDao.save(option1);
		TypeOption option2 = new TypeOption(TYPEOPTION_ODD, TYPEOPTION_RESULT2, betType);
		options.add(option2);
		typeOptionDao.save(option2);

		/* Call */
		eventService.pickWinners(getNonExistentTypeOptions(), betType.getTypeId());

		/* Assertion */
		/* InstanceNotFoundException expected */

	}

	/**
	 * PR-IT-039
	 */
	@Test(expected = TypeNotMultipleException.class)
	public void testPickWinnersTypeNotMultiple()
			throws InstanceNotFoundException, TypeNotMultipleException, EventDateException, NullEventNameException {

		/* Setup */
		initializeCategoryInfos();
		EventInfo event = eventService.createEvent(EXISTENT_CATEGORY_NAME0, getFutureDate(),
				persistentCategoryInfos.get(0).getCategoryId());
		BetType betType = new BetType(BETTYPE_QUESTION, false, event);
		betTypeDao.save(betType);
		List<TypeOption> options = new ArrayList<TypeOption>();
		TypeOption option1 = new TypeOption(TYPEOPTION_ODD, TYPEOPTION_RESULT1, betType);
		options.add(option1);
		typeOptionDao.save(option1);
		TypeOption option2 = new TypeOption(TYPEOPTION_ODD, TYPEOPTION_RESULT2, betType);
		options.add(option2);
		typeOptionDao.save(option2);

		/* Call */
		eventService.pickWinners(getTypeOptionsIds(options), betType.getTypeId());

		/* Assertion */
		/* TypeNotMultipleException expected */
	}

	/**
	 * PR-IT-040
	 */
	@Test
	public void testPickWinners()
			throws InstanceNotFoundException, TypeNotMultipleException, EventDateException, NullEventNameException {

		/* Setup */
		initializeCategoryInfos();
		EventInfo event = eventService.createEvent(EXISTENT_CATEGORY_NAME0, getFutureDate(),
				persistentCategoryInfos.get(0).getCategoryId());
		BetType betType = new BetType(BETTYPE_QUESTION, false, event);
		betTypeDao.save(betType);
		List<TypeOption> options = new ArrayList<TypeOption>();
		TypeOption option1 = new TypeOption(TYPEOPTION_ODD, TYPEOPTION_RESULT1, betType);
		options.add(option1);
		typeOptionDao.save(option1);
		TypeOption option2 = new TypeOption(TYPEOPTION_ODD, TYPEOPTION_RESULT2, betType);
		options.add(option2);
		typeOptionDao.save(option2);

		// Pick option1 to winner
		List<Long> winners = new ArrayList<>();
		winners.add(new Long(option1.getOptionId()));

		/* Call */
		eventService.pickWinners(winners, betType.getTypeId());

		/* Assertion */
		for (TypeOption typeOption : betType.getTypeOptions())
			if (typeOption.getOptionId() == option1.getOptionId())
				assertTrue(typeOption.getIsWinner());
			else
				assertTrue(!typeOption.getIsWinner());

	}

	/**
	 * PR-IT-041
	 */
	@Test
	public void testFindAllCategories() throws InstanceNotFoundException {

		/* Setup */
		initializeCategoryInfos();

		/* Call */
		List<CategoryInfo> results = eventService.findAllCategories();

		/* Assertion */
		assertEquals(results, persistentCategoryInfos);

	}

	/**
	 * PR-IT-042
	 */
	@Test
	public void testEventDateException() throws InstanceNotFoundException, EventDateException, NullEventNameException {

		/* Setup */
		initializeCategoryInfos();

		/* Call */
		try {
			eventService.createEvent(EXISTENT_EVENT_NAME0, null, persistentCategoryInfos.get(0).getCategoryId());
		} catch (EventDateException ex) {
			assertEquals(EXISTENT_EVENT_NAME0, ex.getEventName());
		}

	}

}
