package io.github.andypyrope.ai.data;

public class MismatchException extends RuntimeException {
   private static final long serialVersionUID = -5736418894238871525L;

   public MismatchException(final int expectedWidth, final int expectedHeight,
         final int expectedDepth, final int actualWidth, final int actualHeight,
         final int actualDepth) {

      super(String.format(
            "Expected dimensions %dx%dx%d but the actual dimensions are %dx%dx%d",
            expectedWidth, expectedHeight, expectedDepth, actualWidth,
            actualHeight, actualDepth));
   }

   public MismatchException(final String message) {
      super(message);
   }
}
