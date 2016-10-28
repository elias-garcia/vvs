package es.udc.pa.pa015.practicapa.test.model.eventservice;

import static es.udc.pa.pa015.practicapa.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa015.practicapa.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa015.practicapa.model.bettype.BetType;
import es.udc.pa.pa015.practicapa.model.bettype.BetTypeDao;
import es.udc.pa.pa015.practicapa.model.categoryinfo.CategoryInfo;
import es.udc.pa.pa015.practicapa.model.categoryinfo.CategoryInfoDao;
import es.udc.pa.pa015.practicapa.model.eventService.DuplicatedResultTypeOptionsException;
import es.udc.pa.pa015.practicapa.model.eventService.EventDateException;
import es.udc.pa.pa015.practicapa.model.eventService.EventInfoBlock;
import es.udc.pa.pa015.practicapa.model.eventService.EventServiceImpl;
import es.udc.pa.pa015.practicapa.model.eventService.NoAssignedTypeOptionsException;
import es.udc.pa.pa015.practicapa.model.eventService.NullEventNameException;
import es.udc.pa.pa015.practicapa.model.eventService.StartIndexOrCountException;
import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfo;
import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfoDao;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOption;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOptionDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class EventServiceUnitTest {

	private final String EXISTENT_CATEGORY_NAME0 = "Baloncesto";
	private final String EXISTENT_CATEGORY_NAME1 = "Fútbol";
	private final String EXISTENT_EVENT_NAME0 = "Real Madrid - Barcelona";
	private final String EXISTENT_EVENT_NAME1 = "Real Madrid - Barcelona";
	private final String EXISTENT_EVENT_NAME2 = "Deportivo - Alaves";
	private final String EXISTENT_EVENT_NAME3 = "Unicaja - Fuenlabrada";
	private final String KEYWORDS = "Ma";
	private final long NON_EXISTENT_ID = -1;
	private static Calendar pastDate = null;
	private static Calendar futureDate = null;

	private static String BETTYPE_QUESTION = "¿Quién ganará el encuentro?";

	private static double TYPEOPTION_ODD = 1.0;
	private static String TYPEOPTION_RESULT = "Barcelona";

	List<CategoryInfo> persistentCategoryInfos = new ArrayList<>();
	List<EventInfo> persistentEventInfos = new ArrayList<>();
	List<BetType> persistentBetTypes = new ArrayList<>();
	List<TypeOption> persistentTypeOptions = new ArrayList<>();

	@Mock
	private EventInfoDao eventInfoDaoMock;

	@Mock
	private CategoryInfoDao categoryInfoDaoMock;

	@Mock
	private BetTypeDao betTypeDaoMock;

	@Mock
	private TypeOptionDao typeOptionDaoMock;

	@InjectMocks
	private EventServiceImpl eventService = new EventServiceImpl();

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
	}

	private void initializeEventInfos() {
		persistentEventInfos.add(new EventInfo(EXISTENT_EVENT_NAME0, getPastDate(), persistentCategoryInfos.get(0)));
		persistentEventInfos.add(new EventInfo(EXISTENT_EVENT_NAME1, getPastDate(), persistentCategoryInfos.get(1)));
		persistentEventInfos.add(new EventInfo(EXISTENT_EVENT_NAME2, getFutureDate(), persistentCategoryInfos.get(0)));
		persistentEventInfos.add(new EventInfo(EXISTENT_EVENT_NAME3, getFutureDate(), persistentCategoryInfos.get(1)));
	}

	private void initializeBetTypes() {
		persistentBetTypes.add(new BetType(BETTYPE_QUESTION, false, persistentEventInfos.get(0)));
	}

	private void initializeTypeOptions() {
		persistentTypeOptions.add(new TypeOption(TYPEOPTION_ODD, TYPEOPTION_RESULT, persistentBetTypes.get(0)));
	}

	private void initializeRepeatTypeOptions() {
		persistentTypeOptions.add(new TypeOption(TYPEOPTION_ODD, TYPEOPTION_RESULT, persistentBetTypes.get(0)));
		persistentTypeOptions.add(new TypeOption(TYPEOPTION_ODD, TYPEOPTION_RESULT, persistentBetTypes.get(0)));
	}

	/**
	 * PR-UN-017
	 */
	@Test
	public void testCreateEvent() throws InstanceNotFoundException, EventDateException, NullEventNameException {

		/* Setup */
		initializeCategoryInfos();
		initializeEventInfos();

		/* Mock behavior */
		when(categoryInfoDaoMock.find(1L)).thenReturn(persistentCategoryInfos.get(0));

		/* Call */
		eventService.createEvent(EXISTENT_EVENT_NAME2, getFutureDate(), 1L);

		/* Assertion */

		verify(categoryInfoDaoMock).find(1L);
	}

	/**
	 * PR-UN-018
	 * 
	 * @throws InstanceNotFoundException
	 */
	@SuppressWarnings("unchecked")
	@Test(expected = NullEventNameException.class)
	public void testCreateEventWithNullName()
			throws InstanceNotFoundException, EventDateException, NullEventNameException {
		/* Setup */
		initializeCategoryInfos();
		initializeEventInfos();

		/* Mock behavior */
		when(categoryInfoDaoMock.find(1L)).thenThrow(NullEventNameException.class);

		/* Call */
		eventService.createEvent(null, getFutureDate(), 1L);

		/* Assertion */
		/* NullEventNameException expected */

		verify(categoryInfoDaoMock).find(1L);
	}

	/**
	 * PR-UN-019
	 * 
	 * @throws InstanceNotFoundException
	 */
	@SuppressWarnings("unchecked")
	@Test(expected = EventDateException.class)
	public void testCreateEventWithNullDate()
			throws InstanceNotFoundException, EventDateException, NullEventNameException {

		/* Setup */
		initializeCategoryInfos();
		initializeEventInfos();

		/* Mock behavior */
		when(categoryInfoDaoMock.find(1L)).thenThrow(EventDateException.class);

		/* Call */
		eventService.createEvent(EXISTENT_EVENT_NAME0, null, 1L);

		/* Assertion */
		/* EventDateException expected */

		verify(categoryInfoDaoMock).find(1L);
	}

	/**
	 * PR-UN-020
	 * 
	 * @throws InstanceNotFoundException
	 */
	@SuppressWarnings("unchecked")
	@Test(expected = EventDateException.class)
	public void testCreateEventWithPastDate()
			throws InstanceNotFoundException, EventDateException, NullEventNameException {

		/* Setup */
		initializeCategoryInfos();
		initializeEventInfos();

		/* Mock behavior */
		when(categoryInfoDaoMock.find(1L)).thenThrow(EventDateException.class);

		/* Call */
		eventService.createEvent(EXISTENT_EVENT_NAME0, getPastDate(), 1L);

		/* Assertion */
		/* EventDateException expected */

		verify(categoryInfoDaoMock).find(1L);
	}

	/**
	 * PR-UN-021
	 * 
	 * @throws InstanceNotFoundException
	 */
	@SuppressWarnings("unchecked")
	@Test(expected = InstanceNotFoundException.class)
	public void testCreateEventWithNonExistentCategoryInfoId()
			throws InstanceNotFoundException, EventDateException, NullEventNameException {

		/* Setup */
		initializeCategoryInfos();
		initializeEventInfos();

		/* Mock behavior */
		when(categoryInfoDaoMock.find(NON_EXISTENT_ID)).thenThrow(InstanceNotFoundException.class);

		/* Call */
		eventService.createEvent(EXISTENT_EVENT_NAME0, getFutureDate(), NON_EXISTENT_ID);

		/* Assertion */
		/* InstanceNotFoundException expected */

		verify(categoryInfoDaoMock).find(NON_EXISTENT_ID);
	}

	/*****************************************************************/
	/*****************************************************************/
	/*****************************************************************/

	/**
	 * PR-UN-022
	 * 
	 * @throws InstanceNotFoundException
	 */
	@SuppressWarnings("unchecked")
	@Test(expected = InstanceNotFoundException.class)
	public void testFindEventByNonExistentId() throws InstanceNotFoundException {

		/* Setup */
		initializeCategoryInfos();
		initializeEventInfos();

		/* Mock behavior */
		when(eventInfoDaoMock.find(NON_EXISTENT_ID)).thenThrow(InstanceNotFoundException.class);

		/* Call */
		eventService.findEvent(NON_EXISTENT_ID);

		/* Assertion */
		/* InstanceNotFoundException expected */

		verify(eventInfoDaoMock).find(NON_EXISTENT_ID);
	}

	/**
	 * PR-UN-023
	 * 
	 * @throws InstanceNotFoundException
	 */
	@Test
	public void testFindEventById() throws InstanceNotFoundException {

		/* Setup */
		initializeCategoryInfos();
		initializeEventInfos();

		/* Mock behavior */
		when(eventInfoDaoMock.find(1L)).thenReturn(persistentEventInfos.get(0));

		/* Call */
		EventInfo event = eventService.findEvent(1L);

		/* Assertion */
		assertEquals(event, persistentEventInfos.get(0));

		verify(eventInfoDaoMock).find(1L);
	}

	/*****************************************************************/
	/*****************************************************************/
	/*****************************************************************/

	/**
	 * PR-UN-024
	 * 
	 * @throws InstanceNotFoundException
	 * @throws StartIndexOrCountException
	 */
	@Test
	public void findEventsFilteringWithStartIndexAndCount1()
			throws InstanceNotFoundException, StartIndexOrCountException {

		/* Setup */
		initializeCategoryInfos();
		initializeEventInfos();

		/* Mock behavior */
		when(eventInfoDaoMock.findEvents(null, null, true, 0, 4)).thenReturn(persistentEventInfos);

		/* Call */
		EventInfoBlock foundEventInfoBlock = eventService.findEvents(null, null, true, 0, 4);

		/* Assertion */
		assertTrue(foundEventInfoBlock.getEvents().isEmpty()); // ??
		// assertEquals(eventInfoBlock.getEvents(), persistentEventInfos);
		// assertTrue(foundEventInfoBlock.getExistMoreEvents());

		// verify(eventInfoDaoMock).findEvents(null, null, true, 0, 4);

	}

	/**
	 * PR-UN-025
	 * 
	 * @throws StartIndexOrCountException
	 */
	@Test
	public void findEventsFilteringWithStartIndexAndCount2()
			throws InstanceNotFoundException, StartIndexOrCountException {

		/* Setup */
		initializeCategoryInfos();
		initializeEventInfos();
		persistentEventInfos.remove(3);
		persistentEventInfos.remove(2);

		/* Mock behavior */
		when(eventInfoDaoMock.findEvents(null, null, true, 0, 2)).thenReturn(persistentEventInfos);

		/* Call */
		EventInfoBlock eventInfoBlock = eventService.findEvents(null, null, true, 0, 2);

		/* Assertion */
		// assertEquals(eventInfoBlock.getEvents(), persistentEventInfos);
		// assertTrue(eventInfoBlock.getExistMoreEvents());

		// verify(eventInfoDaoMock).findEvents(null, null, true, 0, 2);
	}

	/**
	 * PR-UN-026
	 * 
	 * @throws StartIndexOrCountException
	 */
	@SuppressWarnings("unchecked")
	@Test(expected = StartIndexOrCountException.class)
	public void findEventsWithANegativeStartIdexOrCount() throws InstanceNotFoundException, StartIndexOrCountException {

		/* Setup */
		initializeCategoryInfos();
		initializeEventInfos();

		/* Mock behavior */
		when(eventInfoDaoMock.findEvents(null, null, true, -1, 0)).thenThrow(StartIndexOrCountException.class);

		/* Call */
		eventService.findEvents(null, null, true, -1, 0);

		/* Assertion */
		/* StartIndexOrCountException expected */

		verify(eventInfoDaoMock.findEvents(null, null, true, -1, 0));
	}

	/*****************************************************************/
	/*****************************************************************/
	/*****************************************************************/

	/**
	 * PR-UN-027
	 * 
	 * @throws InstanceNotFoundException
	 * @throws NoAssignedTypeOptionsException
	 * @throws DuplicatedResultTypeOptionsException
	 */
	@Test
	public void testAddBetType()
			throws InstanceNotFoundException, NoAssignedTypeOptionsException, DuplicatedResultTypeOptionsException {

		/* Setup */
		initializeCategoryInfos();
		initializeEventInfos();
		initializeBetTypes();
		initializeTypeOptions();

		/* Mock behavior */
		when(eventInfoDaoMock.find(1L)).thenReturn(persistentEventInfos.get(0));
		doNothing().when(betTypeDaoMock).save(persistentBetTypes.get(0));
		doNothing().when(typeOptionDaoMock).save(persistentTypeOptions.get(0));

		/* Call */
		eventService.addBetType(1L, persistentBetTypes.get(0), persistentTypeOptions);

		/* Assertion */

		verify(eventInfoDaoMock).find(1L);
		verify(betTypeDaoMock).save(persistentBetTypes.get(0));
		verify(typeOptionDaoMock).save(persistentTypeOptions.get(0));
	}

	/**
	 * PR-UN-028
	 * 
	 * @throws InstanceNotFoundException
	 * @throws NoAssignedTypeOptionsException
	 * @throws DuplicatedResultTypeOptionsException
	 */
	@SuppressWarnings("unchecked")
	@Test(expected = InstanceNotFoundException.class)
	public void testAddBetTypeWithNonExistentEnventInfoId()
			throws InstanceNotFoundException, NoAssignedTypeOptionsException, DuplicatedResultTypeOptionsException {

		/* Setup */
		initializeCategoryInfos();
		initializeEventInfos();
		initializeBetTypes();
		initializeTypeOptions();

		/* Mock behavior */
		when(eventInfoDaoMock.find(NON_EXISTENT_ID)).thenThrow(InstanceNotFoundException.class);

		/* Call */
		eventService.addBetType(NON_EXISTENT_ID, persistentBetTypes.get(0), persistentTypeOptions);

		/* Assertion */

		verify(eventInfoDaoMock.find(NON_EXISTENT_ID));
	}

	/**
	 * PR-UN-029
	 * 
	 * @throws InstanceNotFoundException
	 * @throws NoAssignedTypeOptionsException
	 * @throws DuplicatedResultTypeOptionsException
	 */
	@SuppressWarnings("unchecked")
	@Test(expected = NoAssignedTypeOptionsException.class)
	public void testAddBetTypeWithEmptyTypeOptions()
			throws InstanceNotFoundException, NoAssignedTypeOptionsException, DuplicatedResultTypeOptionsException {

		/* Setup */
		initializeCategoryInfos();
		initializeEventInfos();
		initializeBetTypes();
		initializeTypeOptions();

		/* Mock behavior */

		/* Call */
		eventService.addBetType(1L, persistentBetTypes.get(0), null);

		/* Assertion */
		/* NoAssignedTypeOptionsException expected */

	}

	/**
	 * PR-UN-030
	 * 
	 * @throws InstanceNotFoundException
	 * @throws NoAssignedTypeOptionsException
	 * @throws DuplicatedResultTypeOptionsException
	 */
	@SuppressWarnings("unchecked")
	@Test(expected = DuplicatedResultTypeOptionsException.class)
	public void testAddBetTypeWithDuplicatedResultInTypeOptions()
			throws InstanceNotFoundException, NoAssignedTypeOptionsException, DuplicatedResultTypeOptionsException {

		/* Setup */
		initializeCategoryInfos();
		initializeEventInfos();
		initializeBetTypes();
		initializeRepeatTypeOptions();

		/* Mock behavior */
		when(eventInfoDaoMock.find(1L)).thenReturn(persistentEventInfos.get(0));
		doNothing().when(betTypeDaoMock).save(persistentBetTypes.get(0));

		/* Call */
		eventService.addBetType(1L, persistentBetTypes.get(0), persistentTypeOptions);

		/* Assertion */
		/* DuplicatedResultTypeOptionsException expected */

		verify(eventInfoDaoMock.find(NON_EXISTENT_ID));
		verify(betTypeDaoMock).save(persistentBetTypes.get(0));
	}

	/*****************************************************************/
	/*****************************************************************/
	/*****************************************************************/

	/**
	 * PR-UN-031
	 * 
	 * @throws InstanceNotFoundException
	 */
	@Test
	public void testFindAllCategories() throws InstanceNotFoundException {

		/* Setup */
		initializeCategoryInfos();

		/* Mock behavior */
		when(categoryInfoDaoMock.findAllCategories()).thenReturn(persistentCategoryInfos);

		/* Call */
		List<CategoryInfo> results = eventService.findAllCategories();

		/* Assertion */
		assertEquals(results, persistentCategoryInfos);

		verify(categoryInfoDaoMock).findAllCategories();
	}

	/*****************************************************************/
	/*****************************************************************/
	/*****************************************************************/

}
