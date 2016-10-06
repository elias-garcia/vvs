package es.udc.pa.pa015.practicapa.test.model.betservice;

import static es.udc.pa.pa015.practicapa.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa015.practicapa.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;
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
import es.udc.pa.pa015.practicapa.model.betservice.TypeNotMultipleException;
import es.udc.pa.pa015.practicapa.model.bettype.BetType;
import es.udc.pa.pa015.practicapa.model.bettype.BetTypeDao;
import es.udc.pa.pa015.practicapa.model.categoryinfo.CategoryInfo;
import es.udc.pa.pa015.practicapa.model.categoryinfo.CategoryInfoDao;
import es.udc.pa.pa015.practicapa.model.eventService.EventDateException;
import es.udc.pa.pa015.practicapa.model.eventService.EventService;
import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfo;
import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfoDao;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOption;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOptionDao;
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
	private EventInfoDao eventInfoDao;
	
	@Autowired
	private CategoryInfoDao categoryInfoDao;
	
	@Autowired
	private BetTypeDao betTypeDao;
	
	@Autowired
	private TypeOptionDao typeOptionDao;
	
	@Test
	public void testCreateBetAndFindByUserId() 
			throws InstanceNotFoundException, DuplicateInstanceException, EventDateException {
		
		Calendar betDateAfter = Calendar.getInstance();
		betDateAfter.add(Calendar.WEEK_OF_YEAR, 1);
		
		UserProfile user = userService.registerUser("Teje", "tj11",
				new UserProfileDetails("Teje", "Valcarcel", "diegoteje11@gmail.com"));
		
		CategoryInfo category = new CategoryInfo("categoria1");
		categoryInfoDao.save(category);
		
		EventInfo event = eventService.createEvent("Barça-Madrid", betDateAfter,
				category.getCategoryId());
		
		BetType type = new BetType("¿Quien ganará?", true, event);
		
		List<TypeOption> options = new ArrayList<TypeOption>();
		TypeOption option1 = new TypeOption(1.20, "Cristiano Ronaldo", type);
		options.add(option1);
		TypeOption option2 = new TypeOption(2.40, "Lionel Messi", type);
		options.add(option2);

		eventService.addBetType(event.getEventId(), type, options);
		
		BetInfo bet = betService.createBet(user.getUserProfileId(), option1.getOptionId(), 10);
		
		BetInfoBlock finded = betService.findBetsByUserId(user.getUserProfileId(), 0, 10);
		
		assertEquals(bet, finded.getBets().get(0));
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testCreateBetWithInvalidUserId()
			throws DuplicateInstanceException, InstanceNotFoundException, EventDateException {
		
		Calendar betDateAfter = Calendar.getInstance();
		betDateAfter.add(Calendar.WEEK_OF_YEAR, 1);
		
		CategoryInfo category = new CategoryInfo("categoria1");
		categoryInfoDao.save(category);
		
		EventInfo event = eventService.createEvent("Barça-Madrid", betDateAfter,
				category.getCategoryId());
		
		BetType type = new BetType("¿Quien ganará?", true, event);
		
		List<TypeOption> options = new ArrayList<TypeOption>();
		TypeOption option1 = new TypeOption(1.20, "Cristiano Ronaldo", type);
		options.add(option1);
		TypeOption option2 = new TypeOption(2.40, "Lionel Messi", type);
		options.add(option2);

		eventService.addBetType(event.getEventId(), type, options);
		
		betService.createBet(NON_EXISTENT_USER_ID, option1.getOptionId(), 10);
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void testCreateBetWithInvalidOptionId()
			throws DuplicateInstanceException, InstanceNotFoundException, EventDateException {
		
		Calendar betDateAfter = Calendar.getInstance();
		betDateAfter.add(Calendar.WEEK_OF_YEAR, 1);
		
		UserProfile user = userService.registerUser("Teje", "tj11",
				new UserProfileDetails("Teje", "Valcarcel", "diegoteje11@gmail.com"));
		
		betService.createBet(user.getUserProfileId(), NON_EXISTENT_OPTION_ID, 10);
	}
	
	@Test
	public void testFindBetsByUserIdEmpty() 
			throws InstanceNotFoundException, DuplicateInstanceException {
		
		UserProfile user2 = userService.registerUser("Elias", "Elias10", new
				UserProfileDetails("Elias", "Garcia", "elias.g_10@gmail.com"));
		
		BetInfoBlock bets = betService.findBetsByUserId(user2.getUserProfileId(), 0, 10);
		
		assertEquals(0, bets.getBets().size());
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testFindBetsByNonExistentUserId()
			throws InstanceNotFoundException {

		betService.findBetsByUserId(NON_EXISTENT_USER_ID, 0, 10);
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
		
		BetType type = new BetType("¿Quien ganará?", false, event);
		
		List<TypeOption> options = new ArrayList<TypeOption>();
		TypeOption option1 = new TypeOption(1.20, "Barça", type);
		options.add(option1);
		TypeOption option2 = new TypeOption(10, "Real Madrid", type);
		options.add(option2);
		
		eventService.addBetType(event.getEventId(), type, options);
		
		//betService.changeOptionStatus(option1.getOptionId(), true);
		
		assertTrue(option1.getIsWinner());
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testChangeNonExistentOptionStatus() 
			throws InstanceNotFoundException, TypeNotMultipleException {
		
		//betService.changeOptionStatus(NON_EXISTENT_OPTION_ID, true);
		
	}

	@Test
	public void testChangeMultipleOptionStatus() 
			throws InstanceNotFoundException, TypeNotMultipleException, EventDateException {
	
		Calendar dateBefore = Calendar.getInstance();
		dateBefore.add(Calendar.WEEK_OF_YEAR, -1);
		
		CategoryInfo category = new CategoryInfo("categoria1");
		categoryInfoDao.save(category);
		
		EventInfo event = new EventInfo("Barça-Madrid", dateBefore, category);
		eventInfoDao.save(event);
		
		BetType type = new BetType("¿Quien ganará?", true, event);
		
		List<TypeOption> options = new ArrayList<TypeOption>();
		TypeOption option1 = new TypeOption(1.20, "Barça", type);
		options.add(option1);
		TypeOption option2 = new TypeOption(10, "Real Madrid", type);
		options.add(option2);
		
		eventService.addBetType(event.getEventId(), type, options);
		
		//betService.changeOptionStatus(option1.getOptionId(), true);
		//betService.changeOptionStatus(option2.getOptionId(), true);
	}
	
	@Test(expected = TypeNotMultipleException.class)
	public void testChangeNotMultipleOptionStatus() 
			throws InstanceNotFoundException, TypeNotMultipleException, EventDateException {
	
		Calendar dateBefore = Calendar.getInstance();
		dateBefore.add(Calendar.WEEK_OF_YEAR, -1);
		
		CategoryInfo category = new CategoryInfo("categoria1");
		categoryInfoDao.save(category);
		
		EventInfo event = new EventInfo("Barça-Madrid", dateBefore, category);
		eventInfoDao.save(event);
		
		BetType type = new BetType("¿Quien ganará?", false, event);
		
		List<TypeOption> options = new ArrayList<TypeOption>();
		TypeOption option1 = new TypeOption(1.20, "Barça", type);
		options.add(option1);
		TypeOption option2 = new TypeOption(10, "Real Madrid", type);
		options.add(option2);
		
		eventService.addBetType(event.getEventId(), type, options);
		
		//betService.changeOptionStatus(option1.getOptionId(), true);
		//betService.changeOptionStatus(option2.getOptionId(), true);
	}
}
