package es.udc.pa.pa015.practicapa.web.pages.search;

import es.udc.pa.pa015.practicapa.model.betinfo.BetInfo;
import es.udc.pa.pa015.practicapa.model.betservice.BetInfoBlock;
import es.udc.pa.pa015.practicapa.model.betservice.BetService;
import es.udc.pa.pa015.practicapa.model.userservice.UserService;
import es.udc.pa.pa015.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Class of the betHistory page.
 */
public class BetHistory {

  /** Bets_per_page. */
  private static final int BETS_PER_PAGE = 10;

  /** startIndex. */
  private int startIndex = 0;

  /** userSession. */
  @SessionState(create = false)
  private UserSession userSession;

  /** userService. */
  @Inject
  private UserService userService;

  /** betInfoBlock. */
  private BetInfoBlock betInfoBlock;

  /** betInfo. */
  private BetInfo betInfo;

  /** betService. */
  @Inject
  private BetService betService;

  /** Locale. */
  @Inject
  private Locale locale;

  /**
   * Get bets.
   * @return list of betInfo
   */
  public final List<BetInfo> getBets() {
    return betInfoBlock.getBets();
  }

  /**
   * Get betInfo.
   * @return betInfo
   */
  public final BetInfo getBetInfo() {
    return betInfo;
  }

  /**
   * Set betInfo.
   * @param bet betInfo
   */
  public final void setBetInfo(final BetInfo bet) {
    this.betInfo = bet;
  }

  /**
   * Get format.
   * @return Format
   */
  public final Format getFormat() {
    return NumberFormat.getInstance(locale);
  }

  /**
   * Get dateFormat.
   * @return DateFormat
   */
  public final DateFormat getDateFormat() {
    return DateFormat.getDateInstance(DateFormat.SHORT, locale);
  }

  /**
   * This method get the previous link context.
   * @return Object
   */
  public final Object[] getPreviousLinkContext() {

    if (startIndex - BETS_PER_PAGE >= 0) {
      return new Object[] {startIndex - BETS_PER_PAGE };
    } else {
      return null;
    }

  }

  /**
   * This method get the next link context.
   * @return Object
   */
  public final Object[] getNextLinkContext() {

    if (betInfoBlock.isExistMoreBets()) {
      return new Object[] {startIndex + BETS_PER_PAGE };
    } else {
      return null;
    }

  }

  /**
   * onPassivate.
   * @return Object
   */
  final Object[] onPassivate() {
    return new Object[] {startIndex };
  }

  /**
   * onActivate.
   * @param startIndexParam first element of the list
   * @throws InstanceNotFoundException
   *                  thrown out when the user doesn't exist
   */
  final void onActivate(final int startIndexParam)
                          throws InstanceNotFoundException {
    this.startIndex = startIndexParam;
    betInfoBlock = betService.findBetsByUserId(userSession.getUserProfileId(),
        startIndex, BETS_PER_PAGE);
  }

}
