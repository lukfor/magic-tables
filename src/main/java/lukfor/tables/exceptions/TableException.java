package lukfor.tables.exceptions;

public class TableException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TableException(String message) {
		super(message);
	}

	public TableException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
