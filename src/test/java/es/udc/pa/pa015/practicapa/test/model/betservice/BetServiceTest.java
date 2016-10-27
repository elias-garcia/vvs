package es.udc.pa.pa015.practicapa.test.model.betservice;

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

import es.udc.pa.pa015.practicapa.model.betinfo.BetInfo;
import es.udc.pa.pa015.practicapa.model.betservice.BetInfoBlock;
import es.udc.pa.pa015.practicapa.model.betservice.BetService;
import es.udc.pa.pa015.practicapa.model.bettype.BetType;
import es.udc.pa.pa015.practicapa.model.categoryinfo.CategoryInfo;
import es.udc.pa.pa015.practicapa.model.categoryinfo.CategoryInfoDao;
import es.udc.pa.pa015.practicapa.model.eventService.EventDateException;
import es.udc.pa.pa015.practicapa.model.eventService.EventService;
import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfo;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOption;
import es.udc.pa.pa015.practicapa.model.userprofile.UserProfile;
import es.udc.pa.pa015.practicapa.model.userservice.UserProfileDetails;
import es.udc.pa.pa015.practicapa.model.userservice.UserService;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class BetServiceTest {

	private final long NON_EXISTENT_USER_ID = -1;
	private final long NON_EXISTENT_OPTION_ID = -1;

	@Autowired
	private BetService betService;

	@Autowired
	private EventService eventService;

	@Autowired
	private UserService userService;

	@Autowired
	private CategoryInfoDao categoryInfoDao;
	
	UserProfile user;
	CategoryInfo category;
	EventInfo event;
	BetType type;
	TypeOption option1,option2,option3;
	BetInfo bet,bet2,bet3;

	private void initializeUserProfile() throws DuplicateInstanceException {
		user = userService.registerUser("Teje", "tj11", new UserProfileDetails("Teje", "Valcarcel", "diegoteje11@gmail.com"));
	}

	private void initializeCategoryInfo() {
		category = new CategoryInfo("categoria1");
		categoryInfoDao.save(category);
	}

	private void initializeEventInfo() throws InstanceNotFoundException, EventDateException {
		Calendar date = Calendar.getInstance();
		date.add(Calendar.DATE, 5);
		event = eventService.createEvent("Barça-Madrid", date, category.getCategoryId());
	}

	private void initializeBetType() {
		type = new BetType("¿Quien ganará?", true, event);
	}

	private void initializeTypeOptions() {
		option1 = new TypeOption(1.20, "Cristiano Ronaldo", type);
		option2 = new TypeOption(2.40, "Lionel Messi", type);
	}

	private void initializeBets() throws InstanceNotFoundException {
		bet = betService.createBet(user.getUserProfileId(), option1.getOptionId(), 10);
		bet2 = betService.createBet(user.getUserProfileId(), option1.getOptionId(), 10);
		bet3 = betService.createBet(user.getUserProfileId(), option1.getOptionId(), 10);
	}
	/*
	 * 
	 * PR-IT-001
	 * 
	 */

	@Test
	public void testCreateBetAndFindByUserId()
			throws InstanceNotFoundException, DuplicateInstanceException, EventDateException {
		
		/*Setup*/
		initializeUserProfile();
		initializeCategoryInfo();
		initializeEventInfo();
		initializeBetType();
		initializeTypeOptions();

		List<TypeOption> options = new ArrayList<TypeOption>();
		options.add(option1);
		options.add(option2);

		eventService.addBetType(event.getEventId(), type, options);

		/*Call*/
		BetInfo bet = betService.createBet(user.getUserProfileId(), option1.getOptionId(), 10);

		BetInfoBlock finded = betService.findBetsByUserId(user.getUserProfileId(), 0, 10);

		/*Assertion*/
		assertEquals(bet, finded.getBets().get(0));
	}

	/*
	 * 
	 * PR-IT-002
	 * 
	 */
	@Test(expected = InstanceNotFoundException.class)
	public void testCreateBetWithInvalidUserId()
			throws DuplicateInstanceException, InstanceNotFoundException, EventDateException {

		/*Setup*/
		initializeCategoryInfo();
		initializeEventInfo();
		initializeBetType();

		initializeTypeOptions();

		List<TypeOption> options = new ArrayList<TypeOption>();
		options.add(option1);
		options.add(option2);

		/*Call*/
		eventService.addBetType(event.getEventId(), type, options);

		betService.createBet(NON_EXISTENT_USER_ID, option1.getOptionId(), 10);
	}

	/*
	 * 
	 * PR-IT-003
	 * 
	 */
	@Test(expected = InstanceNotFoundException.class)
	public void testCreateBetWithInvalidOptionId()
			throws DuplicateInstanceException, InstanceNotFoundException, EventDateException {
		/*Setup*/
		initializeUserProfile();

		/*Call*/
		betService.createBet(user.getUserProfileId(), NON_EXISTENT_OPTION_ID, 10);
	}

	/*
	 * 
	 * PR-IT-004
	 * 
	 */
	@Test
	public void testFindBetsByUserId() throws InstanceNotFoundException, DuplicateInstanceException, EventDateException {
		/*Setup*/
		initializeUserProfile();
		initializeCategoryInfo();
		initializeEventInfo();
		initializeBetType();
		initializeTypeOptions();

		List<TypeOption> options = new ArrayList<TypeOption>();
		options.add(option1);
		options.add(option2);

		eventService.addBetType(event.getEventId(), type, options);
		
		initializeBets();
		
		List<BetInfo> bets = new ArrayList<BetInfo>();

		bets.add(bet);
		bets.add(bet2);
		bets.add(bet3);
		
		/*Call*/
		BetInfoBlock finded = betService.findBetsByUserId(user.getUserProfileId(), 0, 10);
		
		/*Assertion*/
		assertEquals(bets, finded.getBets());
		assertFalse(finded.isExistMoreBets());

	}
	
	/*
	 * 
	 * PR-IT-005
	 * 
	 */
	@Test
	public void testFindPartBetsByUserId() throws InstanceNotFoundException, DuplicateInstanceException, EventDateException {

		/*Setup*/
		initializeUserProfile();
		initializeCategoryInfo();
		initializeEventInfo();
		initializeBetType();
		initializeTypeOptions();

		List<TypeOption> options = new ArrayList<TypeOption>();
		options.add(option1);
		options.add(option2);

		eventService.addBetType(event.getEventId(), type, options);
		
		initializeBets();
		
		List<BetInfo> bets = new ArrayList<BetInfo>();

		bets.add(bet);
		bets.add(bet2);
		bets.add(bet3);
		
		/*Call*/
		BetInfoBlock betsBlock = betService.findBetsByUserId(user.getUserProfileId(), 0, 2);

		bets.remove(bet3);

		/*Assertion*/
		assertEquals(bets, betsBlock.getBets());
		assertTrue(betsBlock.isExistMoreBets());

	}
	
	/*
	 * 
	 * PR-IT-006
	 * 
	 */

	@Test(expected = InstanceNotFoundException.class)
	public void testFindBetsByNonExistentUserId() throws InstanceNotFoundException {

		/*Call*/
		betService.findBetsByUserId(NON_EXISTENT_USER_ID, 0, 10);
	}

}
