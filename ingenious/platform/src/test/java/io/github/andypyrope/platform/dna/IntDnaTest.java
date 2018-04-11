package io.github.andypyrope.platform.dna;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntDnaTest {

   private static final int LENGTH = 100;

   @Test
   void testConstructor() {
      final Dna dna = new IntDna(LENGTH);
      dna.randomize();
   }

   @Test
   void testCopulation() {
      for (int i = 0; i < 10; i++) {
         assertNotNull(tryToCopulate(new IntDna(LENGTH), new IntDna(LENGTH)));
      }
   }

   @Test
   void testCopulationMismatch() {
      assertNull(tryToCopulate(new IntDna(LENGTH), new IntDna(LENGTH + 1)));
   }

   @Test
   void testMutationAndReading() {
      final Dna dna = new IntDna(LENGTH);
      // All bits become 1
      dna.mutate(1.0);

      assertEquals(Integer.MAX_VALUE, dna.read());
      assertEquals(17, dna.read(-5000000, 18));
      assertEquals(-1, dna.readSigned());
      assertTrue(dna.readDouble() > 0.999999999);

      assertEquals(2, dna.readArray(2).length);
      assertEquals(Integer.MAX_VALUE, dna.readArray(2)[0]);

      // All bits become 0
      dna.mutate(1.0);

      assertEquals(0, dna.read());
      assertEquals(0, dna.readSigned());
      assertEquals(0.0, dna.readDouble());

      assertEquals(2, dna.readMatrix(2, 3).length);
      assertEquals(3, dna.readMatrix(2, 3)[0].length);
      assertEquals(0, dna.readMatrix(2, 3)[0][0]);

      assertEquals(4, dna.readCube(4, 2, 3).length);
      assertEquals(2, dna.readCube(4, 2, 3)[0].length);
      assertEquals(3, dna.readCube(4, 2, 3)[0][0].length);
      assertEquals(0, dna.readCube(4, 2, 3)[0][0][0]);

      assertEquals(LENGTH + 1, dna.readArray(LENGTH + 1).length);
   }

   @Test
   void testSize() {
      assertEquals(LENGTH, new IntDna(LENGTH).size());
   }

   private Dna tryToCopulate(final IntDna first, final IntDna second) {
      try {
         return new IntDna(first, second);
      } catch (DnaLengthMismatchException e) {
         return null;
      }
   }

}
