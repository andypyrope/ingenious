package io.github.andypyrope.platform.dna;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ByteDnaTest {

   private static final int LENGTH = 100;

   @Test
   void testConstructor() {
      final Dna dna = new ByteDna(LENGTH);
      dna.randomize();
   }

   @Test
   void testCopulation() {
      for (int i = 0; i < 10; i++) {
         assertNotNull(tryToCopulate(new ByteDna(LENGTH), new ByteDna(LENGTH)));
      }
   }

   @Test
   void testCopulationMismatch() {
      assertNull(tryToCopulate(new ByteDna(LENGTH), new ByteDna(LENGTH + 1)));
   }

   @Test
   void testMutationAndReading() {
      final Dna dna = new ByteDna(LENGTH);
      // All bits become 1
      dna.mutate(1.0);

      assertEquals(255, dna.read());
      assertEquals(17, dna.read(-50, 18));
      assertEquals(-1, dna.readSigned());
      assertTrue(dna.readDouble() > 0.996);

      // All bits become 0
      dna.mutate(1.0);

      assertEquals(0, dna.read());
      assertEquals(0, dna.readSigned());
      assertEquals(0.0, dna.readDouble());
   }

   @Test
   void testSize() {
      assertEquals(LENGTH, new ByteDna(LENGTH).size());
   }

   private Dna tryToCopulate(final ByteDna first, final ByteDna second) {
      try {
         return new ByteDna(first, second);
      } catch (DnaLengthMismatchException e) {
         return null;
      }
   }

}
