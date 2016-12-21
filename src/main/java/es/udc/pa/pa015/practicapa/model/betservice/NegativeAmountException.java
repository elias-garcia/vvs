package es.udc.pa.pa015.practicapa.model.betservice;

/**
 * Exception that indicates when the amount is negative.
 */
@SuppressWarnings("serial")
public class NegativeAmountException extends Exception {

	/**
	 * Constructor of the exception.
	 * 
	 * @param amountParam
	 *            The amount
	 */
	public NegativeAmountException(final double amountParam) {

		super("Can't create a bet with => " + "amount = " + amountParam);
	}

}
