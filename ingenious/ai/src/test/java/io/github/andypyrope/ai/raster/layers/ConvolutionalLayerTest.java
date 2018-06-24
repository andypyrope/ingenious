package io.github.andypyrope.ai.raster.layers;

import io.github.andypyrope.ai.data.CustomRasterData;
import io.github.andypyrope.ai.data.RasterData;
import io.github.andypyrope.ai.raster.RasterLayer;
import io.github.andypyrope.ai.testutil.DeterministicRandom;
import io.github.andypyrope.ai.testutil.TestUtil;
import io.github.andypyrope.ai.util.StandardVector;
import io.github.andypyrope.ai.util.Vector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

class ConvolutionalLayerTest {

   private static final int INPUT_COUNT = 3;
   private static final Vector INPUT_SIZE = new StandardVector(2, 3, 3);
   private static final RasterData[] INPUT = new RasterData[INPUT_COUNT];

   private static final int FILTER_COUNT = 2;
   private static final Vector FILTER_SIZE = new StandardVector(1, 2, 3);

   private static final int OUTPUT_COUNT = 6;
   private static final Vector OUTPUT_SIZE = new StandardVector(2, 2, 1);
   private static final RasterData[] TARGET_OUTPUT = new RasterData[OUTPUT_COUNT];

   private static Random _random;

   @BeforeAll
   static void setUpAll() {
      _random = new DeterministicRandom();
      for (int i = 0; i < INPUT_COUNT; i++) {
         INPUT[i] = makeDummyData(INPUT_SIZE);
      }

      for (int i = 0; i < OUTPUT_COUNT; i++) {
         TARGET_OUTPUT[i] = makeDummyData(OUTPUT_SIZE);
      }
   }

   private static RasterData makeDummyData(final Vector size) {
      final RasterData result = new CustomRasterData(size);
      result.forEach((x, y, z) -> result.setCell(x, y, z, _random.nextDouble()));
      return result;
   }

   @BeforeEach
   void setUp() {
      _random = new DeterministicRandom();
   }

   @Test
   void testGetCalculationComplexity() {
      Assertions.assertEquals(144, makeLayer().getCalculationComplexity());
   }

   @Test
   void testGetAdjustmentComplexity() {
      Assertions.assertEquals(2 * 144, makeLayer().getAdjustmentComplexity());
   }

   @Test
   void testLearning() {
      final RasterLayer layer = makeLayer();
      layer.calculate(INPUT);
      TestUtil.compareDoublesLoose(1.83,
            TestUtil.getEuclideanDistance(layer, TARGET_OUTPUT));

      train(layer);
      TestUtil.compareDoublesLoose(1.775,
            TestUtil.getEuclideanDistance(layer, TARGET_OUTPUT));

      train(layer);
      TestUtil.compareDoublesLoose(1.77,
            TestUtil.getEuclideanDistance(layer, TARGET_OUTPUT));
   }

   private void train(final RasterLayer layer) {
      for (int i = 0; i < 5; i++) {
         layer.adjust(TARGET_OUTPUT);
         layer.calculate(INPUT);
      }
   }

   private RasterLayer makeLayer() {
      return new ConvolutionalLayer(INPUT_COUNT, INPUT_SIZE, FILTER_COUNT, FILTER_SIZE,
            new DeterministicRandom());
   }
}