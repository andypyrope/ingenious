package io.github.andypyrope.ai;

public class MismatchException extends RuntimeException {
   private static final long serialVersionUID = -5736418894238871525L;

//   public MismatchException(final int expectedWidth, final int expectedHeight,
//         final int expectedDepth, final int actualWidth, final int actualHeight,
//         final int actualDepth, final String dataId) {
//
//      super(String.format(
//            "Expected raster data with ID '%s' to have dimensions %dx%dx%d but its " +
//                  "actual dimensions are %dx%dx%d",
//            dataId, expectedWidth, expectedHeight, expectedDepth, actualWidth,
//            actualHeight, actualDepth));
//   }

   public MismatchException(final String message) {
      super(message);
   }
}
