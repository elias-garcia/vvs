package es.udc.pa.pa015.practicapa.model.betinfo;

import es.udc.pa.pa015.practicapa.model.typeoption.TypeOption;
import es.udc.pa.pa015.practicapa.model.userprofile.UserProfile;
import org.hibernate.annotations.BatchSize;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This is the bet info class.
 */
@Entity
@BatchSize(size = 10)
public class BetInfo {

  /** The bet id. */
  private Long betId;

  /** The bet date. */
  private Calendar betDate;

  /** The bet amount. */
  private double amount;

  /** User that do the bet. */
  private UserProfile user;

  /** The bet option. */
  private TypeOption option;

  /**
   * This constructor is in blank.
   */
  public BetInfo() {

  }

  /**
   * This constructor initialize bet info.
   * @param betDateParam
   *          The bet date
   * @param amountParam
   *          The bet amount
   * @param userParam
   *          The user that do the bet
   * @param optionParam
   *          The bet option
   */
  public BetInfo(final Calendar betDateParam, final double amountParam,
      final UserProfile userParam, final TypeOption optionParam) {

    /**
     * NOTE: "betId" *must* be left as "null" since its value is automatically
     * generated.
     */

    this.betDate = betDateParam;
    this.amount = amountParam;
    this.user = userParam;
    this.option = optionParam;
  }

  /**
   * Get the bet id.
   * @return bet id
   */
  @Id
  @SequenceGenerator(// It only takes effect for
      name = "BetInfoIdGenerator", // databases providing identifier
      sequenceName = "BetInfoSeq") // generators.
  @GeneratedValue(strategy = GenerationType.AUTO,
                                    generator = "BetInfoIdGenerator")
  public final Long getBetId() {
    return betId;
  }

  /**
   * Set the bet id.
   * @param betIdParam
   *          bet id
   */
  public final void setBetId(final Long betIdParam) {
    this.betId = betIdParam;
  }

  /**
   * Get bet date.
   * @return bet date
   */
  @Temporal(TemporalType.TIMESTAMP)
  public final Calendar getBetDate() {
    return betDate;
  }

  /**
   * Set bet date.
   * @param betDateParam
   *          bet date
   */
  public final void setBetDate(final Calendar betDateParam) {
    this.betDate = betDateParam;
  }

  /**
   * Get bet amount.
   * @return bet amount
   */
  public final double getAmount() {
    return amount;
  }

  /**
   * Set bet amount.
   * @param amountParam
   *          bet amount
   */
  public final void setAmount(final double amountParam) {
    this.amount = amountParam;
  }

  /**
   * Get bet user.
   * @return bet user
   */
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "userId")
  public final UserProfile getUser() {
    return user;
  }

  /**
   * Set bet user.
   * @param userParam
   *          user that do the bet
   */
  public final void setUser(final UserProfile userParam) {
    this.user = userParam;
  }

  /**
   * Get bet option.
   * @return bet option
   */
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "optionId")
  public final TypeOption getOption() {
    return option;
  }

  /**
   * Set bet option.
   * @param optionParam
   *          bet option
   */
  public final void setOption(final TypeOption optionParam) {
    this.option = optionParam;
  }

  /**
   * Transform bet to string.
   */
  @Override
  public final String toString() {
    return "BetInfo [betId=" + betId + ", betDate=" + betDate + ", amount="
        + amount + ", user=" + user.getLoginName() + ", option=" + option
            .getOptionId() + "]";
  }

}
