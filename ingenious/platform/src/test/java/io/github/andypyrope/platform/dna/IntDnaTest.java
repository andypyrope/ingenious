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

      // All bits become 0
      dna.mutate(1.0);

      assertEquals(0, dna.read());
      assertEquals(0, dna.readSigned());
      assertEquals(0.0, dna.readDouble());
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
