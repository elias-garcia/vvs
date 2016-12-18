package es.udc.pa.pa015.practicapa.model.betservice;

/**
 * Exception that indicates you can't select multiple options.
 */
@SuppressWarnings("serial")
public class TypeNotMultipleException extends Exception {

	/**
	 * Constructor of the exception.
	 * 
	 * @param typeIdParam
	 *            The id of type
	 */
	public TypeNotMultipleException(final long typeIdParam) {

		super("Type not multiple exception => " + "typeId = " + typeIdParam);
	}

}
