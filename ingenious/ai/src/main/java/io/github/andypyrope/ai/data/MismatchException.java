package io.github.andypyrope.ai.data;

import io.github.andypyrope.ai.util.RasterSize;

public class MismatchException extends RuntimeException {
   private static final long serialVersionUID = -5736418894238871525L;

   public MismatchException(final RasterSize expectedSize, final RasterSize actualSize) {
      super(String.format("Expected dimensions %s but the actual dimensions are %s",
            expectedSize, actualSize));
   }

   public MismatchException(final String message) {
      super(message);
   }
}
