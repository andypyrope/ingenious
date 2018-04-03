package io.github.andypyrope.platform.dna;

import java.util.concurrent.ThreadLocalRandom;

/**
 * DNA with reasonable precision and speed/RAM usage.
 */
public class IntDna implements Dna {

   private static final int MASK_AFTER_SHIFT = 0x7FFFFFFF;

   private int _currentPosition = 0;
   private final int[] _data;

   public IntDna(int size) {
      _data = new int[size];
   }

   /**
    * Combines two DNA instances to produce a third one.
    * 
    * @param parent1 The first parent
    * @param parent2 The second parent
    * @throws DnaLengthMismatchException
    */
   public IntDna(IntDna parent1, IntDna parent2)
      throws DnaLengthMismatchException {
      if (parent1._data.length != parent2._data.length) {
         throw new DnaLengthMismatchException(
            String.format(
               "Cannot copulate int DNA with length %d and int DNA with length %d",
               parent1._data.length,
               parent2._data.length));
      }
      final int length = parent1._data.length;

      _data = new int[length];
      final int cutPosition = ThreadLocalRandom.current().nextInt(0, length);

      final boolean dataShouldBeSwapped = ThreadLocalRandom.current()
            .nextBoolean();
      final IntDna leftParent = dataShouldBeSwapped ? parent2 : parent1;
      final IntDna rightParent = dataShouldBeSwapped ? parent1 : parent2;
      System.arraycopy(leftParent._data, 0, _data, 0, cutPosition);
      System.arraycopy(rightParent._data,
         cutPosition,
         _data,
         cutPosition,
         length - cutPosition);
   }

   @Override
   public void mutate(double mutationProbability) {
      ThreadLocalRandom generator = ThreadLocalRandom.current();
      for (int i = 0; i < _data.length; i++) {
         for (int j = 0; j < Integer.SIZE; j++) {
            if (generator.nextDouble() < mutationProbability) {
               // Flip the j-th bit of the i-th element
               _data[i] ^= 1 << j;
            }
         }
      }
   }

   @Override
   public void randomize() {
      ThreadLocalRandom generator = ThreadLocalRandom.current();
      for (int i = 0; i < _data.length; i++) {
         _data[i] = generator.nextInt();
      }
   }

   @Override
   public void resetReader() {
      _currentPosition = 0;
   }

   /**
    * Reads the next element as it would an unsigned int, but made 2 times
    * smaller.
    */
   @Override
   public int read() {
      return (readSigned() >> 1) & MASK_AFTER_SHIFT;
   }

   @Override
   public int read(int from, int to) {
      return (int) Math.floor((to - from) * readDouble() + from);
   }

   @Override
   public int readSigned() {
      if (_currentPosition == _data.length) {
         resetReader();
      }
      return _data[_currentPosition++];
   }

   @Override
   public double readDouble() {
      return (((double) read()) / (1L << 31));
   }

   @Override
   public int[] readArray(int length) {
      final int[] result = new int[length];
      for (int i = 0; i < length; i++) {
         result[i] = read();
      }
      return result;
   }

   @Override
   public int[][] readMatrix(int height, int width) {
      final int[][] result = new int[height][];

      for (int i = 0; i < height; i++) {
         result[i] = readArray(width);
      }
      return result;
   }

   @Override
   public int[][][] readCube(int depth, int height, int width) {
      final int[][][] result = new int[depth][][];

      for (int i = 0; i < depth; i++) {
         result[i] = readMatrix(height, width);
      }
      return result;
   }

   @Override
   public int size() {
      return _data.length;
   }
}
