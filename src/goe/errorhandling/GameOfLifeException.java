package goe.errorhandling;

public class GameOfLifeException extends RuntimeException {

	private static final long serialVersionUID = 1447193586461862265L;

	public GameOfLifeException() {
		super();
	}

	public GameOfLifeException(String s) {
		super(s);
	}
}
