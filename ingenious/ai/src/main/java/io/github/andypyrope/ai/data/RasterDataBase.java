package io.github.andypyrope.ai.data;

import io.github.andypyrope.ai.InvalidSizeException;
import io.github.andypyrope.ai.util.RasterSize;
import io.github.andypyrope.ai.util.TriCoordinateConsumer;

import java.util.Random;

abstract class RasterDataBase implements RasterData {

   private final RasterSize _size;

   RasterDataBase(final RasterSize size) {
      _size = size;
   }

   @Override
   public RasterSize getSize() {
      return _size;
   }

   @Override
   public void setAll(final double value) {
      forEach((x, y, z) -> setCell(x, y, z, value));
   }

   @Override
   public void addTo(final int x, final int y, final int z, final double delta) {
      setCell(x, y, z, getCell(x, y, z) + delta);
   }

   @Override
   public void multiply(final int x, final int y, final int z, final double multiplier) {
      setCell(x, y, z, getCell(x, y, z) * multiplier);
   }

   @Override
   public void verifyDimensions(final RasterSize size) throws InvalidSizeException {
      if (getSize().differsFrom(size)) {
         throw new InvalidSizeException(size, getSize());
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
   public void forEach(final TriCoordinateConsumer consumer) {
      for (int z = 0; z < _size.getDepth(); z++) {
         for (int y = 0; y < _size.getHeight(); y++) {
            for (int x = 0; x < _size.getWidth(); x++) {
               consumer.accept(x, y, z);
            }
         }
      }
   }

   @Override
   public double getSum() {
      double sum = 0.0;
      for (int z = 0; z < _size.getDepth(); z++) {
         for (int y = 0; y < _size.getHeight(); y++) {
            for (int x = 0; x < _size.getWidth(); x++) {
               sum += getCell(x, y, z);
            }
         }
      }
      return sum;
   }
}
