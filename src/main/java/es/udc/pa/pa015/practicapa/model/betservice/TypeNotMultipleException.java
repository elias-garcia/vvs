package es.udc.pa.pa015.practicapa.model.betservice;

@SuppressWarnings("serial")
public class TypeNotMultipleException extends Exception {

	private long typeId;
	
	public TypeNotMultipleException(long typeId) {
		
		super("Type not multiple exception => " +
	            "typeId = " + typeId);
				
		this.typeId = typeId;
	}
	
	public long getTypeId() {
		return typeId;
	}
}
