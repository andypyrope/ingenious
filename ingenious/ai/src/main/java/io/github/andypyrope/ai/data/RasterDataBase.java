package io.github.andypyrope.ai.data;

import io.github.andypyrope.ai.util.TriCoordinateConsumer;

import java.util.Random;

abstract class RasterDataBase implements RasterData {

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
   public void verifyDimensions(final int width, final int height, final int depth)
         throws MismatchException {

      if (getWidth() != width || getHeight() != height || getDepth() != depth) {
         throw new MismatchException(width, height, depth, getWidth(),
               getHeight(), getDepth());
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
      for (int z = 0; z < getDepth(); z++) {
         for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
               consumer.accept(x, y, z);
            }
         }
      }
   }
}
