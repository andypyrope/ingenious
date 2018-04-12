package io.github.andypyrope.platform.dna;

import java.util.concurrent.ThreadLocalRandom;

/**
 * DNA with minimal precision and speed/RAM usage.
 */
public class ByteDna implements Dna {

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
    * @throws DnaLengthMismatchException If the DNA length of the parents does not match
    */
   public ByteDna(ByteDna parent1, ByteDna parent2) throws DnaLengthMismatchException {
      if (parent1._data.length != parent2._data.length) {
         throw new DnaLengthMismatchException(String.format(
               "Cannot copulate byte DNA with length %d and byte DNA with length %d",
               parent1._data.length,
               parent2._data.length));
      }
      final int length = parent1._data.length;

      _data = new byte[length];
      final int cutPosition = ThreadLocalRandom.current().nextInt(0, length);

      final boolean dataShouldBeSwapped = ThreadLocalRandom.current()
            .nextBoolean();
      final ByteDna leftParent = dataShouldBeSwapped ? parent2 : parent1;
      final ByteDna rightParent = dataShouldBeSwapped ? parent1 : parent2;
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
         for (int j = 0; j < Byte.SIZE; j++) {
            if (generator.nextDouble() < mutationProbability) {
               // Flip the j-th bit of the i-th element
               _data[i] ^= 1 << j;
            }
         }
      }
   }

   @Override
   public void randomize() {
      ThreadLocalRandom.current().nextBytes(_data);
   }

   @Override
   public void resetReader() {
      _currentPosition = 0;
   }

   @Override
   public int read() {
      return readSigned() & SIGNED_TO_UNSIGNED_BYTE_MASK;
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
      return ((int) _data[_currentPosition++]);
   }

   @Override
   public double readDouble() {
      return ((double) read()) / (1 << Byte.SIZE);
   }

   @Override
   public int size() {
      return _data.length;
   }
}
