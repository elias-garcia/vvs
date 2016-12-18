package es.udc.pa.pa015.practicapa.test.model.eventservice;

import static es.udc.pa.pa015.practicapa.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa015.practicapa.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import es.udc.pa.pa015.practicapa.model.eventService.TypeNotMultipleException;
import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfo;
import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfoDao;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOption;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOptionDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE,
    SPRING_CONFIG_TEST_FILE })
@Transactional
public class EventServiceUnitTest {

  private final String EXISTENT_CATEGORY_NAME0 = "Baloncesto";
  private final String EXISTENT_CATEGORY_NAME1 = "Fútbol";
  private final String EXISTENT_EVENT_NAME0 = "Real Madrid - Barcelona";
  private final String EXISTENT_EVENT_NAME1 = "Real Madrid - Barcelona";
  private final String EXISTENT_EVENT_NAME2 = "Deportivo - Alaves";
  private final String EXISTENT_EVENT_NAME3 = "Unicaja - Fuenlabrada";
  private final long NON_EXISTENT_ID = -1;
  private final long RANDOM_ID = 1;
  private final long TYPEOPTION_ID_WINNER = 1;
  private final long TYPEOPTION_ID_LOSSER = TYPEOPTION_ID_WINNER + 1;
  private static Calendar pastDate = null;
  private static Calendar futureDate = null;
  private static String BETTYPE_QUESTION = "¿Quién ganará el encuentro?";
  private static double TYPEOPTION_ODD = 1.0;
  private static String TYPEOPTION_RESULT1 = "Barcelona";

  List<CategoryInfo> categoryInfos = new ArrayList<>();
  List<EventInfo> eventInfos = new ArrayList<>();
  List<BetType> betTypes = new ArrayList<>();
  List<TypeOption> typeOptionsList = new ArrayList<>();
  Set<TypeOption> typeOptionsSet = new HashSet<>();

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
    categoryInfos.add(new CategoryInfo(EXISTENT_CATEGORY_NAME0));
    categoryInfos.add(new CategoryInfo(EXISTENT_CATEGORY_NAME1));
  }

  private void initializeEventInfos() {
    eventInfos.add(new EventInfo(EXISTENT_EVENT_NAME0, getPastDate(),
        categoryInfos.get(0)));
    eventInfos.add(new EventInfo(EXISTENT_EVENT_NAME1, getPastDate(),
        categoryInfos.get(1)));
    eventInfos.add(new EventInfo(EXISTENT_EVENT_NAME2, getFutureDate(),
        categoryInfos.get(0)));
    eventInfos.add(new EventInfo(EXISTENT_EVENT_NAME3, getFutureDate(),
        categoryInfos.get(1)));
  }

  private void initializeBetTypes() {
    betTypes.add(new BetType(BETTYPE_QUESTION, false, eventInfos.get(2)));
    betTypes.add(new BetType(BETTYPE_QUESTION, true, eventInfos.get(2)));
    /*
     * Para evitar el NullPointerException que se genera cuando se ejecuta
     * TypeNotMultipleException(type.getTypeId()) ya que ese id se recupera de
     * BBDD, le asignamos en memoria un valor aleatorio
     */
    betTypes.get(0).setTypeId(RANDOM_ID);
  }

  private void initializeTypeOptionsList() {
    typeOptionsList.add(new TypeOption(TYPEOPTION_ODD, TYPEOPTION_RESULT1,
        betTypes.get(0)));
  }

  private void initializeRepeatTypeOptions() {
    typeOptionsList.add(new TypeOption(TYPEOPTION_ODD, TYPEOPTION_RESULT1,
        betTypes.get(0)));
    typeOptionsList.add(new TypeOption(TYPEOPTION_ODD, TYPEOPTION_RESULT1,
        betTypes.get(0)));
  }

  private void initializeTypeOptionsWinnerSet() {
    TypeOption typeOption1 = new TypeOption(TYPEOPTION_ODD, TYPEOPTION_RESULT1,
        betTypes.get(0));
    typeOption1.setOptionId(TYPEOPTION_ID_WINNER);
    typeOptionsSet.add(typeOption1);
    betTypes.get(0).setTypeOptions(typeOptionsSet);
  }

  private void initializeTypeOptionsLosserSet() {
    TypeOption typeOption1 = new TypeOption(TYPEOPTION_ODD, TYPEOPTION_RESULT1,
        betTypes.get(0));
    typeOption1.setOptionId(TYPEOPTION_ID_LOSSER);
    typeOptionsSet.add(typeOption1);
    betTypes.get(0).setTypeOptions(typeOptionsSet);
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
   * PR-UN-017
   */
  @Test
  public void testCreateEvent() throws InstanceNotFoundException,
      EventDateException, NullEventNameException {

    /* Setup */
    initializeCategoryInfos();
    initializeEventInfos();

    /* Mock behavior */
    when(categoryInfoDaoMock.find(1L)).thenReturn(categoryInfos.get(0));

    /* Call */
    eventService.createEvent(EXISTENT_EVENT_NAME2, getFutureDate(), 1L);

    /* Assertion */

    verify(categoryInfoDaoMock).find(1L);
  }

  /**
   * PR-UN-018
   */
  @SuppressWarnings("unchecked")
  @Test(expected = NullEventNameException.class)
  public void testCreateEventWithNullName() throws InstanceNotFoundException,
      EventDateException, NullEventNameException {
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
   */
  @SuppressWarnings("unchecked")
  @Test(expected = EventDateException.class)
  public void testCreateEventWithNullDate() throws InstanceNotFoundException,
      EventDateException, NullEventNameException {

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
   */
  @SuppressWarnings("unchecked")
  @Test(expected = EventDateException.class)
  public void testCreateEventWithPastDate() throws InstanceNotFoundException,
      EventDateException, NullEventNameException {

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
   */
  @SuppressWarnings("unchecked")
  @Test(expected = InstanceNotFoundException.class)
  public void testCreateEventWithNonExistentCategoryInfoId()
      throws InstanceNotFoundException, EventDateException,
      NullEventNameException {

    /* Setup */
    initializeCategoryInfos();
    initializeEventInfos();

    /* Mock behavior */
    when(categoryInfoDaoMock.find(NON_EXISTENT_ID)).thenThrow(
        InstanceNotFoundException.class);

    /* Call */
    eventService.createEvent(EXISTENT_EVENT_NAME0, getFutureDate(),
        NON_EXISTENT_ID);

    /* Assertion */
    /* InstanceNotFoundException expected */

    verify(categoryInfoDaoMock).find(NON_EXISTENT_ID);
  }

  /*************************************************************************/
  /*************************************************************************/
  /*************************************************************************/

  /**
   * PR-UN-022
   */
  @SuppressWarnings("unchecked")
  @Test(expected = InstanceNotFoundException.class)
  public void testFindEventByNonExistentId() throws InstanceNotFoundException {

    /* Setup */
    initializeCategoryInfos();
    initializeEventInfos();

    /* Mock behavior */
    when(eventInfoDaoMock.find(NON_EXISTENT_ID)).thenThrow(
        InstanceNotFoundException.class);

    /* Call */
    eventService.findEvent(NON_EXISTENT_ID);

    /* Assertion */
    /* InstanceNotFoundException expected */

    verify(eventInfoDaoMock).find(NON_EXISTENT_ID);
  }

  /**
   * PR-UN-023
   */
  @Test
  public void testFindEventById() throws InstanceNotFoundException {

    /* Setup */
    initializeCategoryInfos();
    initializeEventInfos();

    /* Mock behavior */
    when(eventInfoDaoMock.find(1L)).thenReturn(eventInfos.get(0));

    /* Call */
    EventInfo event = eventService.findEvent(1L);

    /* Assertion */
    assertEquals(event, eventInfos.get(0));

    verify(eventInfoDaoMock).find(1L);
  }

  /*************************************************************************/
  /*************************************************************************/
  /*************************************************************************/

  /**
   * PR-UN-024
   */
  @Test
  public void findEventsFilteringWithStartIndexAndCount1()
      throws InstanceNotFoundException, StartIndexOrCountException {

    /* Setup */
    initializeCategoryInfos();
    initializeEventInfos();

    /* Mock behavior */
    when(eventInfoDaoMock.findEvents(null, null, true, 0, 5)).thenReturn(
        eventInfos);

    /* Call */
    EventInfoBlock eventInfoBlock = eventService.findEvents(null, null, true, 0,
        4);

    /* Assertion */
    assertEquals(eventInfoBlock.getEvents(), eventInfos);
    assertFalse(eventInfoBlock.getExistMoreEvents());

    verify(eventInfoDaoMock).findEvents(null, null, true, 0, 5);

  }

  /**
   * PR-UN-025
   */
  @Test
  public void findEventsFilteringWithStartIndexAndCount2()
      throws InstanceNotFoundException, StartIndexOrCountException {

    /* Setup */
    initializeCategoryInfos();
    initializeEventInfos();
    eventInfos.remove(3);
    eventInfos.remove(2);

    /* Mock behavior */
    when(eventInfoDaoMock.findEvents(null, null, true, 0, 3)).thenReturn(
        eventInfos);

    /* Call */
    EventInfoBlock eventInfoBlock = eventService.findEvents(null, null, true, 0,
        2);

    /* Assertion */
    assertEquals(eventInfoBlock.getEvents(), eventInfos);
    assertFalse(eventInfoBlock.getExistMoreEvents());

    verify(eventInfoDaoMock).findEvents(null, null, true, 0, 3);
  }

  /**
   * PR-UN-026
   */
  @SuppressWarnings("unchecked")
  @Test(expected = StartIndexOrCountException.class)
  public void findEventsWithANegativeStartIdexOrCount()
      throws InstanceNotFoundException, StartIndexOrCountException {

    /* Setup */
    initializeCategoryInfos();
    initializeEventInfos();

    /* Mock behavior */
    when(eventInfoDaoMock.findEvents(null, null, true, -1, 0)).thenThrow(
        StartIndexOrCountException.class);

    /* Call */
    eventService.findEvents(null, null, true, -1, 0);

    /* Assertion */
    /* StartIndexOrCountException expected */

    verify(eventInfoDaoMock.findEvents(null, null, true, -1, 0));
  }

  /*************************************************************************/
  /*************************************************************************/
  /*************************************************************************/

  /**
   * PR-UN-027
   */
  @Test
  public void testAddBetType() throws InstanceNotFoundException,
      NoAssignedTypeOptionsException, DuplicatedResultTypeOptionsException {

    /* Setup */
    initializeCategoryInfos();
    initializeEventInfos();
    initializeBetTypes();
    initializeTypeOptionsList();

    /* Mock behavior */
    when(eventInfoDaoMock.find(1L)).thenReturn(eventInfos.get(0));
    doNothing().when(betTypeDaoMock).save(betTypes.get(0));
    doNothing().when(typeOptionDaoMock).save(typeOptionsList.get(0));

    /* Call */
    eventService.addBetType(1L, betTypes.get(0), typeOptionsList);

    /* Assertion */

    verify(eventInfoDaoMock).find(1L);
    verify(betTypeDaoMock).save(betTypes.get(0));
    verify(typeOptionDaoMock).save(typeOptionsList.get(0));
  }

  /**
   * PR-UN-028
   */
  @SuppressWarnings("unchecked")
  @Test(expected = InstanceNotFoundException.class)
  public void testAddBetTypeWithNonExistentEnventInfoId()
      throws InstanceNotFoundException, NoAssignedTypeOptionsException,
      DuplicatedResultTypeOptionsException {

    /* Setup */
    initializeCategoryInfos();
    initializeEventInfos();
    initializeBetTypes();
    initializeTypeOptionsList();

    /* Mock behavior */
    when(eventInfoDaoMock.find(NON_EXISTENT_ID)).thenThrow(
        InstanceNotFoundException.class);

    /* Call */
    eventService.addBetType(NON_EXISTENT_ID, betTypes.get(0), typeOptionsList);

    /* Assertion */

    verify(eventInfoDaoMock.find(NON_EXISTENT_ID));
  }

  /**
   * PR-UN-029
   */
  @Test(expected = NoAssignedTypeOptionsException.class)
  public void testAddBetTypeWithEmptyTypeOptions()
      throws InstanceNotFoundException, NoAssignedTypeOptionsException,
      DuplicatedResultTypeOptionsException {

    /* Setup */
    initializeCategoryInfos();
    initializeEventInfos();
    initializeBetTypes();
    initializeTypeOptionsList();

    /* Mock behavior */

    /* Call */
    eventService.addBetType(1L, betTypes.get(0), null);

    /* Assertion */
    /* NoAssignedTypeOptionsException expected */

  }

  /**
   * PR-UN-030
   */
  @Test(expected = DuplicatedResultTypeOptionsException.class)
  public void testAddBetTypeWithDuplicatedResultInTypeOptions()
      throws InstanceNotFoundException, NoAssignedTypeOptionsException,
      DuplicatedResultTypeOptionsException {

    /* Setup */
    initializeCategoryInfos();
    initializeEventInfos();
    initializeBetTypes();
    initializeRepeatTypeOptions();

    /* Mock behavior */
    when(eventInfoDaoMock.find(1L)).thenReturn(eventInfos.get(0));
    doNothing().when(betTypeDaoMock).save(betTypes.get(0));

    /* Call */
    eventService.addBetType(1L, betTypes.get(0), typeOptionsList);

    /* Assertion */
    /* DuplicatedResultTypeOptionsException expected */

    verify(eventInfoDaoMock.find(NON_EXISTENT_ID));
    verify(betTypeDaoMock).save(betTypes.get(0));
  }

  /*************************************************************************/
  /*************************************************************************/
  /*************************************************************************/

  /**
   * PR-UN-051
   */
  @SuppressWarnings("unchecked")
  @Test(expected = InstanceNotFoundException.class)
  public void testPickWinnersWithNonExistentBetTypeId()
      throws InstanceNotFoundException, TypeNotMultipleException {

    /* Setup */
    initializeCategoryInfos();
    initializeEventInfos();
    initializeBetTypes();
    initializeTypeOptionsList();

    /* Mock behavior */
    when(betTypeDaoMock.find(NON_EXISTENT_ID)).thenThrow(
        InstanceNotFoundException.class);

    /* Call */
    eventService.pickWinners(getTypeOptionsIds(typeOptionsList),
        NON_EXISTENT_ID);

    /* Assertion */
    /* InstanceNotFoundException expected */

    verify(betTypeDaoMock).find(NON_EXISTENT_ID);
  }

  /**
   * PR-UN-052
   */
  @Test
  public void testPickWinnersWithTypeOptionsNullList()
      throws InstanceNotFoundException, TypeNotMultipleException {

    /* Setup */
    initializeCategoryInfos();
    initializeEventInfos();
    initializeBetTypes();
    initializeTypeOptionsList();

    /* Mock behavior */
    when(betTypeDaoMock.find(1L)).thenReturn(betTypes.get(0));

    /* Call */
    eventService.pickWinners(null, 1L);

    /* Assertion */
    for (TypeOption typeOption : betTypes.get(0).getTypeOptions())
      assertTrue(!typeOption.getIsWinner());
    assertTrue(betTypes.get(0).getPickedWinners());

    verify(betTypeDaoMock).find(1L);
  }

  /**
   * PR-UN-053
   */
  @Test
  public void testPickWinnersWithTypeOptionsEmptyList()
      throws InstanceNotFoundException, TypeNotMultipleException {

    /* Setup */
    initializeCategoryInfos();
    initializeEventInfos();
    initializeBetTypes();
    initializeTypeOptionsList();

    /* Mock behavior */
    when(betTypeDaoMock.find(1L)).thenReturn(betTypes.get(0));

    /* Call */
    eventService.pickWinners(new ArrayList<Long>(), 1L);

    /* Assertion */
    for (TypeOption typeOption : betTypes.get(0).getTypeOptions())
      assertTrue(!typeOption.getIsWinner());
    assertTrue(betTypes.get(0).getPickedWinners());

    verify(betTypeDaoMock).find(1L);
  }

  /**
   * PR-UN-054
   */
  @SuppressWarnings("unchecked")
  @Test(expected = InstanceNotFoundException.class)
  public void testPickWinnersWithNonExistentTypeOptions()
      throws InstanceNotFoundException, TypeNotMultipleException {

    /* Setup */
    initializeCategoryInfos();
    initializeEventInfos();
    initializeBetTypes();
    initializeTypeOptionsList();

    /* Mock behavior */
    when(betTypeDaoMock.find(1L)).thenReturn(betTypes.get(0));
    when(typeOptionDaoMock.find(NON_EXISTENT_ID)).thenThrow(
        InstanceNotFoundException.class);

    /* Call */
    eventService.pickWinners(getNonExistentTypeOptions(), 1L);

    /* Assertion */
    /* InstanceNotFoundException expected */

    verify(betTypeDaoMock).find(1L);
    verify(typeOptionDaoMock).find(NON_EXISTENT_ID);
  }

  /**
   * PR-UN-055
   */
  @Test(expected = TypeNotMultipleException.class)
  public void testPickWinnersTypeNotMultiple() throws InstanceNotFoundException,
      TypeNotMultipleException {

    /* Setup */
    initializeCategoryInfos();
    initializeEventInfos();
    initializeBetTypes();
    initializeRepeatTypeOptions();

    /* Mock behavior */
    when(betTypeDaoMock.find(1L)).thenReturn(betTypes.get(0));
    when(typeOptionDaoMock.find(1L)).thenReturn(typeOptionsList.get(0),
        typeOptionsList.get(1));

    /* Call */
    eventService.pickWinners(getTypeOptionsIds(typeOptionsList), 1L);

    /* Assertion */
    /* TypeNotMultipleException expected */

    verify(betTypeDaoMock).find(1L);
    verify(typeOptionDaoMock).find(1L);
  }

  /**
   * PR-UN-056
   */
  @Test
  public void testPickWinners1() throws InstanceNotFoundException,
      TypeNotMultipleException {

    /* Setup */
    initializeCategoryInfos();
    initializeEventInfos();
    initializeBetTypes();
    initializeTypeOptionsWinnerSet();

    // Pick winners
    List<Long> winners = new ArrayList<>();
    winners.add(new Long(TYPEOPTION_ID_WINNER));

    List<TypeOption> typeOptionsList = new ArrayList<>();
    for (TypeOption typeOption : typeOptionsSet)
      typeOptionsList.add(typeOption);

    /* Mock behavior */
    when(betTypeDaoMock.find(1L)).thenReturn(betTypes.get(0));
    when(typeOptionDaoMock.find(1L)).thenReturn(typeOptionsList.get(0));
    doNothing().when(betTypeDaoMock).save(betTypes.get(0));
    doNothing().when(typeOptionDaoMock).save(typeOptionsList.get(0));

    /* Call */
    eventService.pickWinners(winners, 1L);

    /* Assertion */
    assertTrue(typeOptionsList.get(0).getIsWinner());

    verify(betTypeDaoMock).find(1L);
    verify(typeOptionDaoMock).find(1L);
    verify(betTypeDaoMock).save(betTypes.get(0));
    verify(typeOptionDaoMock).save(typeOptionsList.get(0));
  }

  /**
   * PR-UN-057
   */
  @Test
  public void testPickWinners2() throws InstanceNotFoundException,
      TypeNotMultipleException {

    /* Setup */
    initializeCategoryInfos();
    initializeEventInfos();
    initializeBetTypes();
    initializeTypeOptionsLosserSet();

    // Pick winners
    List<Long> winners = new ArrayList<>();
    winners.add(new Long(TYPEOPTION_ID_WINNER));

    List<TypeOption> typeOptionsList = new ArrayList<>();
    for (TypeOption typeOption : typeOptionsSet)
      typeOptionsList.add(typeOption);

    /* Mock behavior */
    when(betTypeDaoMock.find(1L)).thenReturn(betTypes.get(0));
    when(typeOptionDaoMock.find(1L)).thenReturn(typeOptionsList.get(0));
    doNothing().when(betTypeDaoMock).save(betTypes.get(0));
    doNothing().when(typeOptionDaoMock).save(typeOptionsList.get(0));

    /* Call */
    eventService.pickWinners(winners, 1L);

    /* Assertion */
    assertTrue(!typeOptionsList.get(0).getIsWinner());

    verify(betTypeDaoMock).find(1L);
    verify(typeOptionDaoMock).find(1L);
    verify(betTypeDaoMock).save(betTypes.get(0));
    verify(typeOptionDaoMock).save(typeOptionsList.get(0));
  }

  /*************************************************************************/
  /*************************************************************************/
  /*************************************************************************/

  /**
   * PR-UN-031
   */
  @Test
  public void testFindAllCategories() throws InstanceNotFoundException {

    /* Setup */
    initializeCategoryInfos();

    /* Mock behavior */
    when(categoryInfoDaoMock.findAllCategories()).thenReturn(categoryInfos);

    /* Call */
    List<CategoryInfo> results = eventService.findAllCategories();

    /* Assertion */
    assertEquals(results, categoryInfos);

    verify(categoryInfoDaoMock).findAllCategories();
  }

}
