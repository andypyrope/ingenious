package io.github.andypyrope.ai;

import io.github.andypyrope.ai.util.RasterSize;

public class InvalidSizeException extends RuntimeException {
   private static final long serialVersionUID = -5736418894238871525L;

   public InvalidSizeException(final RasterSize expectedSize,
         final RasterSize actualSize) {
      super(String.format("Expected dimensions %s but the actual dimensions are %s",
            expectedSize, actualSize));
   }

   public InvalidSizeException(final RasterSize actualSize) {
      super("The size " + actualSize + " is invalid");
   }

   public InvalidSizeException(final String message) {
      super(message);
   }
}
