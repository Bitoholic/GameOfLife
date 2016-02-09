package model;

/**
 * An exception class which is used when a pattern is invalid. This class extends {@link Exception}
 *
 * @author Ali Arfan
 * @author  Kent Erlend Bratteng Knudsen
 * @author  Stian Tornholm Grimsgaard
 */
public class PatternFormatException extends Exception {

	private static final long serialVersionUID = 15042016L;
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>PatternFormatException</i></b>
	 * <pre>{@code}public PatternFormatException()</pre>
	 * <p>Constructs a  new patternFromat Exception with no message.</p>
	 * </blockquote>
	 * 
	 */
	public PatternFormatException() {
		super();
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>PatternFormatException</i></b>
	 * <pre>{@code}public PatternFormatException({@link String} message)</pre>
	 * <p>Constructs a  new patternFromat Exception with a message.</p>
	 * @param message - the String  message usually defines why exception is thrown. 
	 * </blockquote>
	 * 
	 */
	public PatternFormatException(String message) {
		super(message);
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>PatternFormatException</i></b>
	 * <pre>{@code}public PatternFormatException({@link String} message,{@link Throwable} cause)</pre>
	 * <p>Constructs a  new patternFromat Exception with a message, and the cause of why the exception was thrown .</p>
	 * @param message - the String  message usually defines why the exception was thrown. 
	 * @param cause - the cause of  exception, null is permitted. 
	 * </blockquote>
	 * 
	 */
	public PatternFormatException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>PatternFormatException</i></b>
	 * <pre>{@code}public PatternFormatException({@link Throwable} cause)</pre>
	 * <p>Constructs a new patternFromat Exception with the  cause of why the exception was thrown.</p>
	 * @param cause - the cause of  exception, null is permitted. 
	 * </blockquote>
	 * 
	 */
	public PatternFormatException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>PatternFormatException</i></b>
	 * <pre>{@code}public PatternFormatException({@link String} message,{@link Throwable} cause,{@link Boolean} enableSuppression),{@link Boolean} writeableStackTrace</pre>
	 * <p>Constructs a new patternFromat Exception with the  cause of why the exception was thrown.</p>
	 * @param message - the String  message usually defines why the exception was thrown.
	 * @param cause - the cause of  exception, null is permitted. 
	 * @param enableSuppression - if suppression is enabled or not. True if enabled, else its false.
	 * @param writableStackTrace -  if stacktrace is writable or not.True if writable, else its false.
	 * 
	 * </blockquote>
	 */
	public PatternFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}