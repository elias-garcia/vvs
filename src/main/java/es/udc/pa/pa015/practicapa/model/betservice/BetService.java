package es.udc.pa.pa015.practicapa.model.betservice;

import es.udc.pa.pa015.practicapa.model.betinfo.BetInfo;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * Interface of the bet service.
 */
public interface BetService {

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
   */
  public BetInfo createBet(Long userId, Long typeOptionId, double amount)
      throws InstanceNotFoundException;

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
  public BetInfoBlock findBetsByUserId(Long userId, int startindex, int count)
      throws InstanceNotFoundException;

}
