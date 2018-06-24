package io.github.andypyrope.ai.data;

import io.github.andypyrope.ai.InvalidSizeException;
import io.github.andypyrope.ai.util.CoordinateConsumer;
import io.github.andypyrope.ai.util.Vector;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

abstract class RasterDataBase implements RasterData {

   final int _width;
   final int _height;
   final int _depth;
   private final Vector _size;

   RasterDataBase(final Vector size) {
      size.validateAsSize();
      _size = size;
      _width = (int) _size.getX();
      _height = (int) _size.getY();
      _depth = (int) _size.getZ();
   }

   @Override
   public Vector getSize() {
      return _size;
   }

   @Override
   public double getCell(final Vector position) {
      final AtomicReference<Double> sum = new AtomicReference<>(0.0);
      position.traverseAround((x, y, z, coefficient) ->
            sum.set(sum.get() + getCell(x, y, z) * coefficient));

      return sum.get();
   }

   @Override
   public void setAll(final double value) {
      forEach((x, y, z) -> setCell(x, y, z, value));
   }

   @Override
   public void addTo(final int x, final int y, final int z, final double value) {
      setCell(x, y, z, getCell(x, y, z) + value);
   }

   @Override
   public void addTo(final Vector position, final double value) {
      position.traverseAround((x, y, z, coefficient) ->
            addTo(x, y, z, value * coefficient));
   }

   @Override
   public void multiply(final int x, final int y, final int z, final double multiplier) {
      setCell(x, y, z, getCell(x, y, z) * multiplier);
   }

   @Override
   public void verifyDimensions(final Vector size) throws InvalidSizeException {
      if (_size.differsFrom(size)) {
         throw new InvalidSizeException(size, _size);
      }
   }

   @Override
   public void randomize(final Random random) {
      forEach((x, y, k) -> setCell(x, y, k, random.nextDouble()));
   }

   @Override
   public void clear() {
      forEach((x, y, k) -> setCell(x, y, k, 0.0));
   }

   @Override
   public void forEach(final CoordinateConsumer consumer) {
      _size.forEach(consumer);
   }

   @Override
   public double getSum() {
      double sum = 0.0;
      for (int z = 0; z < _depth; z++) {
         for (int y = 0; y < _height; y++) {
            for (int x = 0; x < _width; x++) {
               sum += getCell(x, y, z);
            }
         }
      }
      return sum;
   }
}
