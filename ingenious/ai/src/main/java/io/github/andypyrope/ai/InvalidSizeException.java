package io.github.andypyrope.ai;

import io.github.andypyrope.ai.util.Vector;

public class InvalidSizeException extends RuntimeException {
   private static final long serialVersionUID = -5736418894238871525L;

   public InvalidSizeException(final Vector expectedSize, final Vector actualSize) {
      super(String.format("Expected dimensions %s but the actual dimensions are %s",
            expectedSize, actualSize));
   }

   public InvalidSizeException(final Vector actualSize) {
      super(actualSize + " is not a valid size");
   }

   public InvalidSizeException(final String message) {
      super(message);
   }
}
