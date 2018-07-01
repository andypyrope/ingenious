package io.github.andypyrope.ai.util;

import io.github.andypyrope.ai.InvalidSizeException;

public class StandardVector implements Vector {

   public static final Vector UNIT = new StandardVector(1, 1, 1);
   public static final Vector ZERO = new StandardVector(0, 0, 0);
   private static final Vector DEFAULT_PADDING = ZERO;
   private static final Vector DEFAULT_STRIDE = UNIT;

   private static final double EPSILON = 0.0000000001;
   private final double _xPos;
   private final double _yPos;
   private final double _zPos;

   /**
    * Create a vector with integer coordinates.
    *
    * @param xPos The width or X position.
    * @param yPos the height or Y position.
    * @param zPos The depth or Z position.
    */
   public StandardVector(int xPos, int yPos, int zPos) {
      _xPos = xPos;
      _yPos = yPos;
      _zPos = zPos;
   }

   /**
    * Create a vector with decimal coordinates.
    *
    * @param xPos The width or X position.
    * @param yPos the height or Y position.
    * @param zPos The depth or Z position.
    */
   public StandardVector(double xPos, double yPos, double zPos) {
      _xPos = xPos;
      _yPos = yPos;
      _zPos = zPos;
   }

   @Override
   public Vector plus(final double size) {
      return new StandardVector(_xPos + size, _yPos + size, _zPos + size);
   }

   @Override
   public Vector plus(final int x, final int y, final int z) {
      return new StandardVector(_xPos + x, _yPos + y, _zPos + z);
   }

   @Override
   public Vector plus(final Vector other) {
      return new StandardVector(
            _xPos + other.getX(),
            _yPos + other.getY(),
            _zPos + other.getZ());
   }

   @Override
   public Vector minus(final int x, final int y, final int z) {
      return new StandardVector(_xPos - x, _yPos - y, _zPos - z);
   }

   @Override
   public Vector minus(final Vector other) {
      return new StandardVector(
            _xPos - other.getX(),
            _yPos - other.getY(),
            _zPos - other.getZ());
   }

   @Override
   public Vector multipliedBy(final int multiplier) {
      return new StandardVector(
            _xPos * multiplier,
            _yPos * multiplier,
            _zPos * multiplier);
   }

   @Override
   public Vector dividedBy(final Vector other) {
      return new StandardVector(
            _xPos / other.getX(),
            _yPos / other.getY(),
            _zPos / other.getZ());
   }

   @Override
   public double getX() {
      return _xPos;
   }

   @Override
   public double getY() {
      return _yPos;
   }

   @Override
   public double getZ() {
      return _zPos;
   }

   @Override
   public int getPixelCount() {
      validateAsSize();
      return (int) (_xPos * _yPos * _zPos);
   }

   @Override
   public boolean differsFrom(final Vector other) {
      return _xPos != other.getX() || _yPos != other.getY() || _zPos != other.getZ();
   }

   @Override
   public void validateAsSize() {
      if (minimum(_xPos, _yPos, _zPos) < 1 || isAnyNonInteger(_xPos, _yPos, _zPos)) {
         throw new InvalidSizeException(this);
      }
   }

   @Override
   public boolean isValidIndex() {
      return !isAnyNonInteger(_xPos, _yPos, _zPos) && getMin() + EPSILON > 0.0;
   }

   @Override
   public Vector getScanSize(final Vector windowSize, final Vector padding,
         final Vector stride) {

      return this
            .plus((padding == null ? DEFAULT_PADDING : padding).multipliedBy(2))
            .minus(windowSize.minus(StandardVector.UNIT))
            .dividedBy(stride == null ? DEFAULT_STRIDE : stride);
   }

   @Override
   public void forEach(final CoordinateConsumer consumer) throws InvalidSizeException {
      validateAsSize();
      final int toX = (int) (_xPos + 0.5);
      final int toY = (int) (_yPos + 0.5);
      final int toZ = (int) (_zPos + 0.5);

      for (int z = 0; z < toZ; z++) {
         for (int y = 0; y < toY; y++) {
            for (int x = 0; x < toX; x++) {
               consumer.accept(x, y, z);
            }
         }
      }
   }

   @Override
   public void slideWindow(final Vector windowSize,
         final Vector padding, final Vector stride, VectorCoordinateConsumer consumer) {

      final Vector actualPadding = padding == null ? DEFAULT_PADDING : padding;
      final Vector actualStride = stride == null ? DEFAULT_STRIDE : stride;

      final Vector from = StandardVector.ZERO.minus(actualPadding);
      // Since the <= operator does not always work on doubles, adding a tiny number is
      // used to simulate it
      final Vector to = plus(actualPadding).minus(windowSize).plus(EPSILON);

      int actualX;
      int actualY;
      int actualZ = 0;
      for (double z = from.getZ(); z < to.getZ(); z += actualStride.getZ()) {
         actualY = 0;
         for (double y = from.getY(); y < to.getY(); y += actualStride.getY()) {
            actualX = 0;
            for (double x = from.getX(); x < to.getX(); x += actualStride.getX()) {
               consumer.accept(new StandardVector(x, y, z),
                     actualX, actualY, actualZ);
               actualX++;
            }
            actualY++;
         }
         actualZ++;
      }
   }

   @Override
   public void traverseAround(final CoordinateWithCoefficientConsumer consumer) {
      final int x = (int) _xPos;
      final int y = (int) _yPos;
      final int z = (int) _zPos;
      if (isValidIndex()) {
         consumer.accept(x, y, z, 1.0);
         return;
      }

      // Coefficients
      final Vector far = minus(x, y, z);
      final Vector close = StandardVector.UNIT.minus(far);

      // Only go to the far cells if their corresponding coefficient is not too close to 0
      final int toDeltaX = (int) Math.ceil(far.getX() - EPSILON);
      final int toDeltaY = (int) Math.ceil(far.getY() - EPSILON);
      final int toDeltaZ = (int) Math.ceil(far.getZ() - EPSILON);

      for (int deltaZ = 0; deltaZ <= toDeltaZ; deltaZ++) {
         for (int deltaY = 0; deltaY <= toDeltaY; deltaY++) {
            for (int deltaX = 0; deltaX <= toDeltaX; deltaX++) {
               consumer.accept(x + deltaX, y + deltaY, z + deltaZ, 1.0 *
                     (deltaX == 0 ? close : far).getX() *
                     (deltaY == 0 ? close : far).getY() *
                     (deltaZ == 0 ? close : far).getZ());
            }
         }
      }
   }

   @Override
   public String toString() {
      return String.format("(%.2f, %.2f, %.2f)", _xPos, _yPos, _zPos);
   }

   private double minimum(final double... values) {
      double minimum = values[0];
      for (int i = 1; i < values.length; i++) {
         if (values[i] < minimum) {
            minimum = values[i];
         }
      }
      return minimum;
   }

   private double getMin() {
      return Math.min(Math.min(_xPos, _yPos), _zPos);
   }

   private boolean isAnyNonInteger(final double... values) {
      for (final double value : values) {
         if (isNonInteger(value)) {
            return true;
         }
      }
      return false;
   }

   private boolean isNonInteger(final double value) {
      return areDifferent((int) (value), value);
   }

   private boolean areDifferent(final double value1, final double value2) {
      return Math.abs(value1 - value2) > EPSILON;
   }
}
