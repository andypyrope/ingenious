package io.github.andypyrope.ai.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TriRasterSizeTest {

   private static final int WIDTH = 2;
   private static final int HEIGHT = 3;
   private static final int DEPTH = 4;

   @Test
   void testAtLeast() {
      Assertions.assertEquals("2x1x1", new TriRasterSize(2, -1, 1).atLeast(1).toString());
   }

   @Test
   void testPlus() {
      Assertions.assertEquals("3x4x5", makeSize().plus(1).toString());
   }

   @Test
   void testMinus() {
      Assertions.assertEquals("1x2x1",
            makeSize().minus(new TriRasterSize(1, 1, 3)).toString());
   }

   @Test
   void testGetWidth() {
      Assertions.assertEquals(WIDTH, makeSize().getWidth());
   }

   @Test
   void testGetHeight() {
      Assertions.assertEquals(HEIGHT, makeSize().getHeight());
   }

   @Test
   void testGetDepth() {
      Assertions.assertEquals(DEPTH, makeSize().getDepth());
   }

   @Test
   void testGetPixelCount() {
      Assertions.assertEquals(WIDTH * HEIGHT * DEPTH, makeSize().getPixelCount());
   }

   @Test
   void testToString() {
      Assertions.assertEquals("2x3x4", new TriRasterSize(2, 3, 4).toString());
   }

   @Test
   void testDiffersFrom() {
      Assertions.assertTrue(makeSize()
            .differsFrom(new TriRasterSize(WIDTH + 1, HEIGHT, DEPTH)));
      Assertions.assertTrue(makeSize()
            .differsFrom(new TriRasterSize(WIDTH, HEIGHT + 1, DEPTH)));
      Assertions.assertTrue(makeSize()
            .differsFrom(new TriRasterSize(WIDTH, HEIGHT, DEPTH + 1)));

      Assertions.assertFalse(makeSize()
            .differsFrom(new TriRasterSize(WIDTH, HEIGHT, DEPTH)));
   }

   private RasterSize makeSize() {
      return new TriRasterSize(WIDTH, HEIGHT, DEPTH);
   }
}