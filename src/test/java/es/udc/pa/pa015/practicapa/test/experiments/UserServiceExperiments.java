package es.udc.pa.pa015.practicapa.test.experiments;

import static es.udc.pa.pa015.practicapa.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa015.practicapa.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;

import es.udc.pa.pa015.practicapa.model.userprofile.UserProfile;
import es.udc.pa.pa015.practicapa.model.userservice.IncorrectPasswordException;
import es.udc.pa.pa015.practicapa.model.userservice.UserProfileDetails;
import es.udc.pa.pa015.practicapa.model.userservice.UserService;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserServiceExperiments {

  /**
   * Main method.
   * @param args
   *          args passed
   */
  @SuppressWarnings("resource")
  public static void main(String[] args) {

    /* Get service object. */
    ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[] {
        SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE });
    UserService userService = ctx.getBean(UserService.class);

    try {
      // Register user.
      UserProfile userProfile = userService.registerUser("serviceUser",
          "userPassword", new UserProfileDetails("name", "lastName",
              "user@udc.es"));
      System.out.println("User with userId '" + userProfile.getUserProfileId()
          + "' has been created");
      System.out.println(userProfile);

      // Find user.
      userProfile = userService.login("serviceUser", "userPassword", false);
      System.out.println("User with userId '" + userProfile.getUserProfileId()
          + "' has been retrieved");
      System.out.println(userProfile);

      // ... proceed in the same way for other entities / methods / use
      // cases

    } catch (IncorrectPasswordException | InstanceNotFoundException
        | DuplicateInstanceException e) {
      e.printStackTrace();
    }

  }

}
