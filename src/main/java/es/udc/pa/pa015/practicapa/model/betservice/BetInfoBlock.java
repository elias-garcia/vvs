package es.udc.pa.pa015.practicapa.model.betservice;

import es.udc.pa.pa015.practicapa.model.betinfo.BetInfo;

import java.util.List;

/**
 * BetInfo Block.
 */
public class BetInfoBlock {

  private List<BetInfo> bets;
  private boolean existMoreBets;

  /**
   * Constructor of the BetInfoBlock.
   * @param bets
   *          List of bets
   * @param existMoreBets
   *          Indicates if there are more bets to list
   */
  public BetInfoBlock(List<BetInfo> bets, boolean existMoreBets) {
    this.bets = bets;
    this.existMoreBets = existMoreBets;
  }

  public List<BetInfo> getBets() {
    return bets;
  }

  public boolean isExistMoreBets() {
    return existMoreBets;
  }
}
