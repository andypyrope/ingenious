package io.github.andypyrope.ai;

/**
 * An exception that signifies an invalid operation was attempted. It implies that there
 * is a mistake in the usage of the AI framework.
 */
public class InvalidOperationException extends RuntimeException {
   private static final long serialVersionUID = 310126104593256863L;

   public InvalidOperationException(final String message) {
      super(message);
   }
}
