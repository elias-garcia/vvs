package es.udc.pa.pa015.practicapa.model.userprofile;

import es.udc.pojo.modelutil.dao.GenericDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * UserProfile dao.
 */
public interface UserProfileDao extends GenericDao<UserProfile, Long> {

  /**
   * Returns an UserProfile by login name (not user identifier).
   * @param loginName
   *          the user identifier
   * @return the UserProfile
   * @throws InstanceNotFoundException
   *            thrown out when the loginName doesn't exist
   */
  UserProfile findByLoginName(String loginName)
      throws InstanceNotFoundException;
}
