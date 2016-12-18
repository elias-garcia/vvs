package es.udc.pa.pa015.practicapa.web.pages.user;

import es.udc.pa.pa015.practicapa.model.betinfo.BetInfo;
import es.udc.pa.pa015.practicapa.model.betservice.BetInfoBlock;
import es.udc.pa.pa015.practicapa.model.betservice.BetService;
import es.udc.pa.pa015.practicapa.model.bettype.BetType;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOption;
import es.udc.pa.pa015.practicapa.model.userservice.UserService;
import es.udc.pa.pa015.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa015.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa015.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;

import java.util.Locale;

/**
 * Class of betHistory page.
 */
@AuthenticationPolicy(AuthenticationPolicyType.NON_ADMIN_AUTHENTICATED_USERS)
public class BetHistory {

  /** Bets per page. */
  private static final int BETS_PER_PAGE = 10;

  /** Number of first element in the list. */
  private int startIndex = 0;

  /** userSession. */
  @Property
  @SessionState(create = false)
  private UserSession userSession;

  /** userService. */
  @Inject
  private UserService userService;

  /** betInfoBlock. */
  @Property
  private BetInfoBlock betInfoBlock;

  /** betInfo. */
  @Property
  private BetInfo betInfo;

  /** betType. */
  @Property
  private BetType betType;

  /** typeOption. */
  @Property
  private TypeOption typeOption;

  /** betService. */
  @Inject
  private BetService betService;

  /** Locale. */
  @Inject
  private Locale locale;

  /**
   * Get dateFormat.
   * @return dateFormat
   */
  public final DateFormat getDateFormat() {
    return DateFormat.getDateInstance(DateFormat.SHORT, locale);
  }

  /**
   * Get timeFormat.
   * @return timeFormat
   */
  public final DateFormat getTimeFormat() {
    return DateFormat.getTimeInstance(DateFormat.SHORT, locale);
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
   * Get format.
   * @return numberFormat
   */
  public final Format getFormat() {
    return NumberFormat.getInstance(locale);
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
   * Get gain.
   * @return bet gain
   */
  public final Double getGain() {
    return betInfo.getAmount() * betInfo.getOption().getOdd();
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
   * @param startIndexParam startIndex
   * @throws InstanceNotFoundException
   *                       thrown out when the bet doesn't exist
   */
  final void onActivate(final int startIndexParam)
                              throws InstanceNotFoundException {
    this.startIndex = startIndexParam;
    betInfoBlock = betService.findBetsByUserId(userSession.getUserProfileId(),
        startIndex, BETS_PER_PAGE);
  }

}
