package es.udc.pa.pa015.practicapa.model.betservice;

import es.udc.pa.pa015.practicapa.model.betinfo.BetInfo;

import java.util.List;

/**
 * BetInfo Block.
 */
public class BetInfoBlock {

  /** Bets. */
  private List<BetInfo> bets;

  /** ExistMoreBets. */
  private boolean existMoreBets;

  /**
   * Constructor of the BetInfoBlock.
   * @param betsParam
   *          List of bets
   * @param existMoreBetsParam
   *          Indicates if there are more bets to list
   */
  public BetInfoBlock(final List<BetInfo> betsParam,
                      final boolean existMoreBetsParam) {
    this.bets = betsParam;
    this.existMoreBets = existMoreBetsParam;
  }

  /**
   * Get bets.
   * @return list of bets
   */
  public final List<BetInfo> getBets() {
    return bets;
  }

  /**
   * Indicates if exist more bets.
   * @return boolean
   */
  public final boolean isExistMoreBets() {
    return existMoreBets;
  }
}
