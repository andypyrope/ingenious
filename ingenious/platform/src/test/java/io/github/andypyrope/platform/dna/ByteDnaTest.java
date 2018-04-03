package io.github.andypyrope.platform.dna;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ByteDnaTest {

   private static final int LENGTH = 100;

   @Test
   public void testConstructor() {
      final Dna dna = new ByteDna(LENGTH);
      dna.randomize();
   }

   @Test
   public void testCopulation() {
      for (int i = 0; i < 10; i++) {
         assertNotNull(tryToCopulate(new ByteDna(LENGTH), new ByteDna(LENGTH)));
      }
   }

   @Test
   public void testCopulationMismatch() {
      assertNull(tryToCopulate(new ByteDna(LENGTH), new ByteDna(LENGTH + 1)));
   }

   @Test
   public void testMutationAndReading() {
      final ByteDna dna = new ByteDna(LENGTH);
      // All bits become 1
      dna.mutate(1.0);

      assertEquals(255, dna.read());
      assertEquals(17, dna.read(-50, 18));
      assertEquals(-1, dna.readSigned());
      assertTrue(dna.readDouble() > 0.996);

      assertEquals(2, dna.readArray(2).length);
      assertEquals(255, dna.readArray(2)[0]);

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
   public void testSize() {
      assertEquals(LENGTH, new ByteDna(LENGTH).size());
   }

   private ByteDna tryToCopulate(final ByteDna first, final ByteDna second) {
      try {
         return new ByteDna(first, second);
      } catch (DnaLengthMismatchException e) {
         return null;
      }
   }

}
