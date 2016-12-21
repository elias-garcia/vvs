package es.udc.pa.pa015.practicapa.test.model.typeoption;

import static es.udc.pa.pa015.practicapa.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa015.practicapa.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa015.practicapa.model.bettype.BetType;
import es.udc.pa.pa015.practicapa.model.categoryinfo.CategoryInfo;
import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfo;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOption;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOptionDao;
import es.udc.pa.pa015.practicapa.model.userprofile.UserProfile;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class TypeOptionDaoUnitTest {

	@Autowired
	private TypeOptionDao typeOptionDao;

	@Autowired
	private SessionFactory sessionFactory;

	UserProfile user;
	TypeOption typeOption;
	EventInfo event;
	CategoryInfo category;
	BetType betType;

	private void initializeUser() {
		user = new UserProfile("user", "pass", "nombre", "apellido", "user@gmail.com");
		sessionFactory.getCurrentSession().saveOrUpdate(user);
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

	/**
	 * PR-UN-060
	 */
	@Test
	public void testRemainingReturnedValues() throws InstanceNotFoundException {
		/* Setup */
		initializeUser();
		initializeCategory();
		initializeEvent();
		initializeBetType();
		initializeEventWithBetType();
		initializeTypeOption();

		/* Call */
		TypeOption option = typeOptionDao.find(typeOption.getOptionId());

		/* Assertion */
		assertEquals(typeOption.getOdd(), 0.1, option.getOdd());
	}

}
