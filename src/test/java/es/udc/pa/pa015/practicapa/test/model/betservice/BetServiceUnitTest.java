package es.udc.pa.pa015.practicapa.test.model.betservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import es.udc.pa.pa015.practicapa.model.betinfo.BetInfo;
import es.udc.pa.pa015.practicapa.model.betinfo.BetInfoDao;
import es.udc.pa.pa015.practicapa.model.betservice.BetInfoBlock;
import es.udc.pa.pa015.practicapa.model.betservice.BetServiceImpl;
import es.udc.pa.pa015.practicapa.model.betservice.NegativeAmountException;
import es.udc.pa.pa015.practicapa.model.bettype.BetType;
import es.udc.pa.pa015.practicapa.model.categoryinfo.CategoryInfo;
import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfo;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOption;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOptionDao;
import es.udc.pa.pa015.practicapa.model.userprofile.UserProfile;
import es.udc.pa.pa015.practicapa.model.userprofile.UserProfileDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class BetServiceUnitTest {

  @Mock
  private UserProfileDao userProfileDaoMock;

  @Mock
  private TypeOptionDao typeOptionDaoMock;

  @Mock
  private BetInfoDao betInfoDaoMock;

  @InjectMocks
  private BetServiceImpl betService = new BetServiceImpl();

  UserProfile userProfileDemo;
  CategoryInfo categoryInfoDemo;
  EventInfo eventInfoDemo;
  BetType betTypeDemo;
  TypeOption typeOptionDemo1;
  TypeOption typeOptionDemo2;
  TypeOption typeOptionDemo3;
  BetInfo betInfoDemo1;
  BetInfo betInfoDemo2;
  BetInfo betInfoDemo3;

  private void initializeUserProfile() {
    userProfileDemo = new UserProfile("user", "demo", "Demo", "User",
        "user@demo.com");
  }

  private void initializeCategoryInfo() {
    categoryInfoDemo = new CategoryInfo("Fútbol");
  }

  private void initializeEventInfo() {
    Calendar date = Calendar.getInstance();
    date.add(Calendar.DATE, 5);
    eventInfoDemo = new EventInfo("Deportivo de la Coruña - Celta de Vigo",
        date, categoryInfoDemo);
  }

  private void initializeBetType() {
    betTypeDemo = new BetType("¿Quién ganará?", false, eventInfoDemo);
  }

  private void initializeTypeOptions() {
    typeOptionDemo1 = new TypeOption(5.00, "Deportivo de la Coruña",
        betTypeDemo);
    typeOptionDemo2 = new TypeOption(3.70, "Empate", betTypeDemo);
    typeOptionDemo3 = new TypeOption(1.75, "Celta de Vigo", betTypeDemo);
  }

  private void initializeBets() {
    betInfoDemo1 = new BetInfo(Calendar.getInstance(), 10.00, userProfileDemo,
        typeOptionDemo1);
    betInfoDemo2 = new BetInfo(Calendar.getInstance(), 10.00, userProfileDemo,
        typeOptionDemo2);
    betInfoDemo3 = new BetInfo(Calendar.getInstance(), 10.00, userProfileDemo,
        typeOptionDemo3);
  }

  /*
   * PR-UN-032
   */
  @Test
  public void createBet() throws InstanceNotFoundException, NegativeAmountException {

    initializeUserProfile();
    initializeCategoryInfo();
    initializeEventInfo();
    initializeBetType();
    initializeTypeOptions();

    when(userProfileDaoMock.find(1L)).thenReturn(userProfileDemo);
    when(typeOptionDaoMock.find(1L)).thenReturn(typeOptionDemo1);

    betService.createBet(1L, 1L, 10.00);

    verify(userProfileDaoMock).find(1L);
    verify(typeOptionDaoMock).find(1L);

  }

  /*
   * PR-UN-033
   */
  @SuppressWarnings("unchecked")
  @Test(expected = InstanceNotFoundException.class)
  public void createBetNonExistingUser() throws InstanceNotFoundException, NegativeAmountException {

    when(userProfileDaoMock.find(-1L)).thenThrow(
        InstanceNotFoundException.class);

    betService.createBet(-1L, 1L, 10.00);

  }

  /*
   * PR-UN-034
   */
  @SuppressWarnings("unchecked")
  @Test(expected = InstanceNotFoundException.class)
  public void createBetNonExistingTypeOption()
      throws InstanceNotFoundException, NegativeAmountException {

    initializeUserProfile();

    when(userProfileDaoMock.find(1L)).thenReturn(userProfileDemo);
    when(typeOptionDaoMock.find(-1L)).thenThrow(
        InstanceNotFoundException.class);

    betService.createBet(1L, -1L, 10.00);

  }

  /*
   * PR-UN-035
   */
  @Test
  public void findAllBetsByUserId() throws InstanceNotFoundException {

    initializeUserProfile();
    initializeCategoryInfo();
    initializeEventInfo();
    initializeBetType();
    initializeTypeOptions();
    initializeBets();

    List<BetInfo> bets = new ArrayList<>();
    bets.add(betInfoDemo1);
    bets.add(betInfoDemo2);
    bets.add(betInfoDemo3);

    when(betInfoDaoMock.findBetsByUserId(1L, 0, 4)).thenReturn(bets);

    BetInfoBlock betsBlock = betService.findBetsByUserId(1L, 0, 3);

    assertEquals(bets, betsBlock.getBets());
    assertFalse(betsBlock.isExistMoreBets());

    verify(userProfileDaoMock).find(1L);
    verify(betInfoDaoMock).findBetsByUserId(1L, 0, 4);

  }

  /*
   * PR-UN-036
   */
  @Test
  public void findSomeBetsByUserId() throws InstanceNotFoundException {

    initializeUserProfile();
    initializeCategoryInfo();
    initializeEventInfo();
    initializeBetType();
    initializeTypeOptions();
    initializeBets();

    List<BetInfo> bets = new ArrayList<>();
    bets.add(betInfoDemo1);
    bets.add(betInfoDemo2);
    bets.add(betInfoDemo3);

    when(betInfoDaoMock.findBetsByUserId(1L, 0, 3)).thenReturn(bets);

    BetInfoBlock betsBlock = betService.findBetsByUserId(1L, 0, 2);

    bets.remove(betInfoDemo3);

    assertEquals(bets, betsBlock.getBets());
    assertTrue(betsBlock.isExistMoreBets());

    verify(userProfileDaoMock).find(1L);
    verify(betInfoDaoMock).findBetsByUserId(1L, 0, 3);

  }

  /*
   * PR-UN-037
   */
  @SuppressWarnings("unchecked")
  @Test(expected = InstanceNotFoundException.class)
  public void findBetsByNonExistingUserId() throws InstanceNotFoundException {

    when(userProfileDaoMock.find(-1L)).thenThrow(
        InstanceNotFoundException.class);

    betService.findBetsByUserId(-1L, 0, 10);

  }
}
