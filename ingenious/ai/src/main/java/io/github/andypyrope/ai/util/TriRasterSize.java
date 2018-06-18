package io.github.andypyrope.ai.util;

public class TriRasterSize implements RasterSize {

   private final int _width;
   private final int _height;
   private final int _depth;

   /**
    * Create a custom size.
    *
    * @param width  The width.
    * @param height the height.
    * @param depth  The depth.
    */
   public TriRasterSize(int width, int height, int depth) {
      _width = width;
      _height = height;
      _depth = depth;
   }

   @Override
   public RasterSize atLeast(final int value) {
      return new TriRasterSize(
            Math.max(_width, value),
            Math.max(_height, value),
            Math.max(_depth, value));
   }

   @Override
   public RasterSize plus(final int size) {
      return new TriRasterSize(_width + size, _height + size, _depth + size);
   }

   @Override
   public RasterSize minus(final RasterSize other) {
      return new TriRasterSize(
            _width - other.getWidth(),
            _height - other.getHeight(),
            _depth - other.getDepth());
   }

   @Override
   public int getWidth() {
      return _width;
   }

   @Override
   public int getHeight() {
      return _height;
   }

   @Override
   public int getDepth() {
      return _depth;
   }

   @Override
   public int getPixelCount() {
      return _width * _height * _depth;
   }

   @Override
   public boolean differsFrom(final RasterSize other) {
      return _width != other.getWidth() || _height != other.getHeight()
            || _depth != other.getDepth();
   }

   @Override
   public boolean isInvalid() {
      return _width <= 0 || _height <= 0 || _depth <= 0;
   }

   @Override
   public String toString() {
      return _width + "x" + _height + "x" + _depth;
   }
}
