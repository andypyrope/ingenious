package io.github.andypyrope.platform.dna;

import java.util.concurrent.ThreadLocalRandom;

public class ByteDna implements Dna {

   public static final int MIN_INT_VALUE = 0;
   public static final int MAX_INT_VALUE = 255;
   public static final int INT_BYTE_COUNT = 1;

   private static final int SIGNED_TO_UNSIGNED_BYTE_MASK = 0x000000FF;

   private int _currentPosition = 0;
   private final byte[] _data;

   public ByteDna(int size) {
      _data = new byte[size];
   }

   /**
    * Combines two DNA instances to produce a third one.
    * 
    * @param parent1 The first parent
    * @param parent2 The second parent
    * @throws DnaLengthMismatchException
    */
   public ByteDna(ByteDna parent1, ByteDna parent2)
            throws DnaLengthMismatchException {
      if (parent1._data.length != parent2._data.length) {
         throw new DnaLengthMismatchException(
            String.format(
               "Cannot copulate byte DNA with length %d and byte DNA with length %d",
               parent1._data.length,
               parent2._data.length));
      }
      final int length = parent1._data.length;

      _data = new byte[length];
      final int cutPosition = ThreadLocalRandom.current().nextInt(0, length);

      System.arraycopy(parent1._data, 0, _data, 0, cutPosition);
      System.arraycopy(parent2._data,
         cutPosition,
         _data,
         cutPosition,
         length - cutPosition);
   }

   public void mutate(double mutationProbability) {
      for (int i = 0; i < _data.length; i++) {
         for (int j = 0; j < Byte.SIZE; j++) {
            if (Math.random() < mutationProbability) {
               // Flip the j-th bit of the i-th element
               _data[i] ^= 1 << j;
            }
         }
      }
   }

   public void resetReader() {
      _currentPosition = 0;
   }

   public int read() {
      return readSigned() & SIGNED_TO_UNSIGNED_BYTE_MASK;
   }

   public int readSigned() {
      if (_currentPosition == _data.length) {
         resetReader();
      }
      return ((int) _data[_currentPosition++]);
   }

   public double readDouble() {
      return ((double) read()) / MAX_INT_VALUE;
   }

   public int[] readArray(int length) {
      final int[] result = new int[length];
      for (int i = 0; i < length; i++) {
         result[i] = read();
      }
      return result;
   }

   public int[][] readMatrix(int height, int width) {
      final int[][] result = new int[height][];

      for (int i = 0; i < height; i++) {
         result[i] = readArray(width);
      }
      return result;
   }

   public int[][][] readCube(int depth, int height, int width) {
      final int[][][] result = new int[depth][][];

      for (int i = 0; i < depth; i++) {
         result[i] = readMatrix(height, width);
      }
      return result;
   }

   public int size() {
      return _data.length;
   }
}
