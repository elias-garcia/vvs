package es.udc.pa.pa015.practicapa.test.experiments;

import es.udc.pa.pa015.practicapa.model.userprofile.UserProfile;
import es.udc.pa.pa015.practicapa.model.userservice.util.PasswordEncrypter;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class SessionExperiments {

  /**
   * Main method.
   * @param args
   *          args passed
   */
  public static void main(String[] args) {

    Session session = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = null;
    try {
      tx = session.beginTransaction();

      // Register user.
      UserProfile userProfile = new UserProfile("sessionUser", PasswordEncrypter
          .crypt("userPassword"), "name", "lastName", "user@udc.es");
      session.saveOrUpdate(userProfile);
      Long userId = userProfile.getUserProfileId();
      System.out.println("User with userId '" + userId + "' has been created");
      System.out.println(userProfile);

      // Find user.
      userProfile = (UserProfile) session.get(UserProfile.class, userId);
      if (userProfile != null) {
        System.out.println("User with userId '" + userId
            + "' has been retrieved");
        System.out.println(userProfile);
      } else {
        System.out.println("User with userId '" + userId
            + "' has not been found");
      }

      // ... proceed in the same way for other entities / methods /use cases

      tx.commit();

    } catch (Exception e) {
      e.printStackTrace();
      if (tx != null) {
        tx.rollback();
      }
    } finally {
      session.close();
    }

    HibernateUtil.shutdown();

  }
}
