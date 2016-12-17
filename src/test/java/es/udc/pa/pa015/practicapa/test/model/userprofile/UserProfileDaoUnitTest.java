package es.udc.pa.pa015.practicapa.test.model.userprofile;

import static es.udc.pa.pa015.practicapa.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa015.practicapa.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;

import es.udc.pa.pa015.practicapa.model.eventService.DuplicatedResultTypeOptionsException;
import es.udc.pa.pa015.practicapa.model.eventService.NoAssignedTypeOptionsException;
import es.udc.pa.pa015.practicapa.model.userprofile.UserProfile;
import es.udc.pa.pa015.practicapa.model.userprofile.UserProfileDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE,
    SPRING_CONFIG_TEST_FILE })
@Transactional
public class UserProfileDaoUnitTest {

  private final String EXISTENT_LOGIN_NAME = "user";
  private final String NON_EXISTENT_LOGIN_NAME = "user2";

  UserProfile userProfile;

  @Autowired
  private UserProfileDao userProfileDao;

  @Autowired
  private SessionFactory sessionFactory;

  private void initializeUser() {
    userProfile = new UserProfile(EXISTENT_LOGIN_NAME, "pass", "nombre",
        "apellido", "user@gmail.com");
    sessionFactory.getCurrentSession().saveOrUpdate(userProfile);
  }

  /**
   * PR-UN-013.
   * @throws InstanceNotFoundException
   *           This method thrown out when the event doesn't exist
   */
  @Test(expected = InstanceNotFoundException.class)
  public void testFindByLoginNameWithANonExistentName()
      throws InstanceNotFoundException {
    /* Setup */
    initializeUser();
    /* Call */
    userProfileDao.findByLoginName(NON_EXISTENT_LOGIN_NAME);
    /* Assertion */
    /* InstanceNotFoundException expected */
  }

  /**
   * PR-UN-014.
   * @throws InstanceNotFoundException
   *           This method thrown out when the event doesn't exist
   */
  @Test
  public void testFindByLoginNameWithAExistentName()
      throws InstanceNotFoundException {
    /* Setup */
    initializeUser();
    /* Call */
    UserProfile foundUser = userProfileDao.findByLoginName(EXISTENT_LOGIN_NAME);
    /* Assertion */
    assertEquals(userProfile, foundUser);
  }

}
