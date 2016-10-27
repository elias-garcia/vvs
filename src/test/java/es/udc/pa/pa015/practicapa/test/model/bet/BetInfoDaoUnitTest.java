package es.udc.pa.pa015.practicapa.test.model.bet;

import static es.udc.pa.pa015.practicapa.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa015.practicapa.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa015.practicapa.model.betinfo.BetInfo;
import es.udc.pa.pa015.practicapa.model.betinfo.BetInfoDao;
import es.udc.pa.pa015.practicapa.model.bettype.BetType;
import es.udc.pa.pa015.practicapa.model.categoryinfo.CategoryInfo;
import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfo;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOption;
import es.udc.pa.pa015.practicapa.model.userprofile.UserProfile;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class BetInfoDaoUnitTest {

	private final String USER_WITH_BETS_LOGINAME = "user";
	private final String USER_WITHOUT_BETS_LOGINAME = "user2";
	private final int STARTINDEX = 0;
	private final int COUNT = 4;

	@Autowired
	private BetInfoDao betInfoDao;

	@Autowired
	private SessionFactory sessionFactory;

	UserProfile userWithBets;
	UserProfile userNoWithBets;
	TypeOption typeOption;
	EventInfo event;
	CategoryInfo category;
	BetType betType;
	BetInfo bet1;
	BetInfo bet2;

	private void initializeUser() {
		userWithBets = new UserProfile(USER_WITH_BETS_LOGINAME, "pass", "nombre", "apellido", "user@gmail.com");
		userNoWithBets = new UserProfile(USER_WITHOUT_BETS_LOGINAME, "pass", "nombre", "apellido", "user@gmail.com");
		sessionFactory.getCurrentSession().saveOrUpdate(userWithBets);
		sessionFactory.getCurrentSession().saveOrUpdate(userNoWithBets);
	}

	private void initializeCategory() {
		category = new CategoryInfo("category1");
		sessionFactory.getCurrentSession().saveOrUpdate(category);
	}

	private void initializeEvent() {
		Calendar date = new GregorianCalendar(2010, Calendar.FEBRUARY, 22, 23, 11, 44);
		event = new EventInfo("Barça-Madrid", date, category);
		sessionFactory.getCurrentSession().saveOrUpdate(event);
	}

	private void initializeBetType() {
		betType = new BetType("¿Quien ganará?", true, event);
	}

	private void initializeEventWithBetType() {
		event.addBetType(betType);
		betType.setEvent(event);
		sessionFactory.getCurrentSession().saveOrUpdate(betType);
	}

	private void initializeTypeOption() {
		typeOption = new TypeOption(5, "typeOption", betType);
		sessionFactory.getCurrentSession().saveOrUpdate(typeOption);
	}

	private void initializeBetsInfoWithUser() {
		Calendar date = new GregorianCalendar(2010, Calendar.FEBRUARY, 22, 23, 11, 44);
		bet1 = new BetInfo(date, (double) 10, userWithBets, typeOption);
		bet2 = new BetInfo(date, (double) 10, userWithBets, typeOption);
		sessionFactory.getCurrentSession().saveOrUpdate(bet1);
		sessionFactory.getCurrentSession().saveOrUpdate(bet2);

	}

	/**
	 * PR-UN-015
	 * 
	 */
	@Test
	public void testBetsByUserIdWithUserWithBets() {
		/* Setup */
		initializeUser();
		initializeCategory();
		initializeEvent();
		initializeBetType();
		initializeEventWithBetType();
		initializeTypeOption();
		initializeBetsInfoWithUser();

		List<BetInfo> betsCreated = new ArrayList<BetInfo>();

		betsCreated.add(bet1);
		betsCreated.add(bet2);
		List<BetInfo> betsFound = new ArrayList<BetInfo>();

		/* Call */
		betsFound = betInfoDao.findBetsByUserId(userWithBets.getUserProfileId(), STARTINDEX, COUNT);

		/* Assertion */
		assertEquals(betsCreated, betsFound);
	}

	/**
	 * PR-UN-016
	 * 
	 */
	@Test
	public void testBetsByUserIdWithUserNoWithBets() {
		/* Setup */
		initializeUser();

		List<BetInfo> betsFound = new ArrayList<BetInfo>();

		/* Call */
		betsFound = betInfoDao.findBetsByUserId(userNoWithBets.getUserProfileId(), STARTINDEX, COUNT);

		/* Assertion */
		assertTrue(betsFound.isEmpty());
	}
}