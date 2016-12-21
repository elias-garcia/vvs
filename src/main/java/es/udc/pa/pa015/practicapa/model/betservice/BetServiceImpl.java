package es.udc.pa.pa015.practicapa.model.betservice;

import es.udc.pa.pa015.practicapa.model.betinfo.BetInfo;
import es.udc.pa.pa015.practicapa.model.betinfo.BetInfoDao;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOption;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOptionDao;
import es.udc.pa.pa015.practicapa.model.userprofile.UserProfile;
import es.udc.pa.pa015.practicapa.model.userprofile.UserProfileDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;

/**
 * This class implements the betService.
 */
@Service("BetService")
@Transactional
public class BetServiceImpl implements BetService {

  /** BetInfoDao. */
  @Autowired
  private BetInfoDao betInfoDao;

  /** TypeOptionDao. */
  @Autowired
  private TypeOptionDao typeOptionDao;

  /** UserProfileDao. */
  @Autowired
  private UserProfileDao userProfileDao;

  /**
   * This method create a bet.
   * @param userId
   *          The id of the user that do the bet
   * @param typeOptionId
   *          The type option to add to the bet
   * @param amount
   *          The amount of the bet
   * @return The betInfo created
   * @throws InstanceNotFoundException
   *           It thrown out when the userId or typeOptionId don't exist
   * @throws NegativeAmountException
   *           It thrown out when the amount is negative
   */
  public final BetInfo createBet(final Long userId,
                                 final Long typeOptionId, final double amount)
      throws InstanceNotFoundException, NegativeAmountException {
    
    if (amount < 0) {
      throw new NegativeAmountException(amount);
    }
    UserProfile currentUser = userProfileDao.find(userId);
    TypeOption currentOption = typeOptionDao.find(typeOptionId);

    BetInfo newBet = new BetInfo(Calendar.getInstance(), amount, currentUser,
        currentOption);

    betInfoDao.save(newBet);

    return newBet;
  }

  /**
   * This method find bets by an user id.
   * @param userId
   *          The user id to search
   * @param startindex
   *          Number of element where start the list
   * @param count
   *          Number of elements
   * @return The betInfoBlock
   * @throws InstanceNotFoundException
   *           It thrown out when the userId doesn't exist
   */
  @Transactional(readOnly = true)
  public final BetInfoBlock findBetsByUserId(final Long userId,
                                         final int startindex, final int count)
      throws InstanceNotFoundException {

    userProfileDao.find(userId);

    /*
     * Find count+1 bets to determine if there exist more bets above the
     * specified range.
     */
    List<BetInfo> bets = betInfoDao.findBetsByUserId(userId, startindex, count
        + 1);

    boolean existMoreBets = bets.size() == (count + 1);

    /*
     * Remove the last bet from the returned list if there exist more bets above
     * the specified range.
     */
    if (existMoreBets) {
      bets.remove(bets.size() - 1);
    }

    /* Return BetInfoBlock. */
    return new BetInfoBlock(bets, existMoreBets);
  }

}
