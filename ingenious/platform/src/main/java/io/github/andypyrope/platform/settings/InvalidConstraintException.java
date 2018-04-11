package io.github.andypyrope.platform.settings;

/**
 * An exception thrown when the constraints of a setting are invalid.
 */
public class InvalidConstraintException extends RuntimeException {
   private static final long serialVersionUID = 8407002104385959734L;

   /**
    * @param message The exception message.
    */
   public InvalidConstraintException(final String message) {
      super(message);
   }
}
