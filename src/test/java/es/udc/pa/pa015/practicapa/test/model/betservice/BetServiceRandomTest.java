package es.udc.pa.pa015.practicapa.test.model.betservice;

import static es.udc.pa.pa015.practicapa.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa015.practicapa.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;

import es.udc.pa.pa015.practicapa.model.betinfo.BetInfo;
import es.udc.pa.pa015.practicapa.model.betservice.BetService;
import es.udc.pa.pa015.practicapa.model.betservice.NegativeAmountException;
import es.udc.pa.pa015.practicapa.model.bettype.BetType;
import es.udc.pa.pa015.practicapa.model.categoryinfo.CategoryInfo;
import es.udc.pa.pa015.practicapa.model.categoryinfo.CategoryInfoDao;
import es.udc.pa.pa015.practicapa.model.eventService.DuplicatedResultTypeOptionsException;
import es.udc.pa.pa015.practicapa.model.eventService.EventDateException;
import es.udc.pa.pa015.practicapa.model.eventService.EventService;
import es.udc.pa.pa015.practicapa.model.eventService.NoAssignedTypeOptionsException;
import es.udc.pa.pa015.practicapa.model.eventService.NullEventNameException;
import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfo;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOption;
import es.udc.pa.pa015.practicapa.model.userprofile.UserProfile;
import es.udc.pa.pa015.practicapa.model.userservice.UserProfileDetails;
import es.udc.pa.pa015.practicapa.model.userservice.UserService;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.java.quickcheck.Generator;
import net.java.quickcheck.generator.PrimitiveGenerators;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE,
    SPRING_CONFIG_TEST_FILE })
@Transactional
public class BetServiceRandomTest {

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

  TypeOption option1;
  TypeOption option2;
  TypeOption option3;

  BetInfo bet;
  BetInfo bet2;
  BetInfo bet3;

  private void initializeUserProfile() throws DuplicateInstanceException {
    user = userService.registerUser("Teje", "tj11", new UserProfileDetails(
        "Teje", "Valcarcel", "diegoteje11@gmail.com"));
  }

  private void initializeCategoryInfo() {
    category = new CategoryInfo("categoria1");
    categoryInfoDao.save(category);
  }

  private void initializeEventInfo() throws InstanceNotFoundException,
      EventDateException, NullEventNameException {
    Calendar date = Calendar.getInstance();
    date.add(Calendar.DATE, 5);
    event = eventService.createEvent("Barça-Madrid", date, category
        .getCategoryId());
  }

  private void initializeBetType() {
    type = new BetType("¿Quien ganará?", true, event);
  }

  private void initializeTypeOptions() {
    option1 = new TypeOption(1.20, "Cristiano Ronaldo", type);
    option2 = new TypeOption(2.40, "Lionel Messi", type);
  }
  
  /*
   * 
   * PR-RANDOM-IT-001
   * 
   */

  @Test
  @Repeat(10)
  public void testCreateBetAndFindByUserId() throws InstanceNotFoundException,
      DuplicateInstanceException, EventDateException, NullEventNameException,
      NoAssignedTypeOptionsException, DuplicatedResultTypeOptionsException, NegativeAmountException {

    /* Setup */
    initializeUserProfile();
    initializeCategoryInfo();
    initializeEventInfo();
    initializeBetType();

    option1 = new TypeOption(1.20, "Cristiano Ronaldo", type);
    
    List<TypeOption> options = new ArrayList<TypeOption>();
    options.add(option1);

    eventService.addBetType(event.getEventId(), type, options);

    /* Generator */
    Generator<Double> generator = PrimitiveGenerators.doubles(0, Double.MAX_VALUE);
    
    /* Call */
    betService.createBet(user.getUserProfileId(), option1
        .getOptionId(), generator.next());
  }
  
  /*
   * 
   * PR-RANDOM-IT-002
   * 
   */
  @Test(expected = NegativeAmountException.class)
  @Repeat(10)
  public void testCreateBetWithNegativeAmount()
      throws DuplicateInstanceException, InstanceNotFoundException,
      EventDateException, NullEventNameException,
      NoAssignedTypeOptionsException, DuplicatedResultTypeOptionsException, NegativeAmountException {

    /* Setup */
    initializeUserProfile();
    initializeCategoryInfo();
    initializeEventInfo();
    initializeBetType();
    initializeTypeOptions();

    List<TypeOption> options = new ArrayList<TypeOption>();
    options.add(option1);
    options.add(option2);

    eventService.addBetType(event.getEventId(), type, options);

    /* Generator */
    Generator<Double> generator = PrimitiveGenerators.doubles(-Double.MAX_VALUE,0);
    
    /* Call */
    betService.createBet(user.getUserProfileId(), option1
        .getOptionId(), generator.next());
  }

}
