package es.udc.pa.pa015.practicapa.model.betservice;

/**
 * Exception that indicates you can't select multiple options.
 */
@SuppressWarnings("serial")
public class TypeNotMultipleException extends Exception {

  private long typeId;

  /**
   * Constructor of the exception.
   * @param typeId
   *          The id of type
   */
  public TypeNotMultipleException(long typeId) {

    super("Type not multiple exception => " + "typeId = " + typeId);

    this.typeId = typeId;
  }

  public long getTypeId() {
    return typeId;
  }
}
