package io.github.andypyrope.ai.raster.layers;

import io.github.andypyrope.ai.data.CustomRasterData;
import io.github.andypyrope.ai.data.RasterData;
import io.github.andypyrope.ai.testutil.DeterministicRandom;
import io.github.andypyrope.ai.testutil.TestUtil;
import io.github.andypyrope.ai.util.StandardVector;
import io.github.andypyrope.ai.util.Vector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

class RasterBiasLayerTest {
   private static final int COUNT = 2;
   private static final Vector SIZE = new StandardVector(2, 2, 2);
   private static final RasterData[] INPUT = new RasterData[COUNT];
   private static final RasterData[] TARGET_OUTPUT = new RasterData[COUNT];

   private static Random _random;

   @BeforeAll
   static void setUpAll() {
      _random = new DeterministicRandom();
      for (int i = 0; i < COUNT; i++) {
         INPUT[i] = makeDummyData();
         TARGET_OUTPUT[i] = makeDummyData();
      }
   }

   private static RasterData makeDummyData() {
      final RasterData result = new CustomRasterData(SIZE);
      for (int x = 0; x < SIZE.getX(); x++) {
         for (int y = 0; y < SIZE.getY(); y++) {
            for (int z = 0; z < SIZE.getZ(); z++) {
               result.setCell(x, y, z, _random.nextDouble());
            }
         }
      }
      return result;
   }

   @BeforeEach
   void setUp() {
      _random = new DeterministicRandom();
   }

   @Test
   void testGetCalculationComplexity() {
      Assertions.assertEquals(16, makeLayer().getCalculationComplexity());
   }

   @Test
   void testGetAdjustmentComplexity() {
      Assertions.assertEquals(16, makeLayer().getAdjustmentComplexity());
   }

   @Test
   void testLearning() {
      final RasterLayer firstLayer = makeLayer();
      final RasterLayer secondLayer = makeLayer();
      firstLayer.setSurroundingLayers(null, secondLayer);
      secondLayer.setSurroundingLayers(firstLayer, null);

      firstLayer.calculate(INPUT);
      secondLayer.calculate();
      TestUtil.compareDoublesLoose(2.29,
            TestUtil.getEuclideanDistance(secondLayer, TARGET_OUTPUT));

      train(firstLayer, secondLayer);
      TestUtil.compareDoublesLoose(1.47,
            TestUtil.getEuclideanDistance(secondLayer, TARGET_OUTPUT));

      train(firstLayer, secondLayer);
      TestUtil.compareDoublesLoose(1.47,
            TestUtil.getEuclideanDistance(secondLayer, TARGET_OUTPUT));
   }

   private void train(final RasterLayer firstLayer, final RasterLayer secondLayer) {
      for (int i = 0; i < 5; i++) {
         secondLayer.adjust(TARGET_OUTPUT);
         firstLayer.adjust();
         firstLayer.calculate(INPUT);
         secondLayer.calculate();
      }
   }

   private RasterLayer makeLayer() {
      return new RasterBiasLayer(COUNT, SIZE, new DeterministicRandom());
   }
}