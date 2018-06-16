package io.github.andypyrope.ai.raster.layers;

import io.github.andypyrope.ai.data.CustomRasterData;
import io.github.andypyrope.ai.data.RasterData;
import io.github.andypyrope.ai.raster.RasterLayer;
import io.github.andypyrope.ai.testutil.DeterministicRandom;
import io.github.andypyrope.ai.testutil.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Random;

class RasterBiasLayerTest {
   private static final int COUNT = 2;
   private static final int WIDTH = 2;
   private static final int HEIGHT = 2;
   private static final int DEPTH = 2;
   private static final RasterData[] INPUT = new RasterData[COUNT];
   private static final RasterData[] TARGET_OUTPUT = new RasterData[COUNT];

   private static final Random STATIC_RANDOM = new DeterministicRandom();

   @BeforeAll
   static void setUpAll() {
      for (int i = 0; i < COUNT; i++) {
         INPUT[i] = makeDummyData();
         TARGET_OUTPUT[i] = makeDummyData();
      }
   }

   private static RasterData makeDummyData() {
      final RasterData result = new CustomRasterData(WIDTH, HEIGHT, DEPTH);
      for (int x = 0; x < WIDTH; x++) {
         for (int y = 0; y < HEIGHT; y++) {
            for (int z = 0; z < DEPTH; z++) {
               result.setCell(x, y, z, STATIC_RANDOM.nextDouble());
            }
         }
      }
      return result;
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
      TestUtil.compareDoubles(2.29,
            TestUtil.getEuclideanDistance(secondLayer, TARGET_OUTPUT));

      train(firstLayer, secondLayer);
      TestUtil.compareDoubles(1.47,
            TestUtil.getEuclideanDistance(secondLayer, TARGET_OUTPUT));

      train(firstLayer, secondLayer);
      TestUtil.compareDoubles(1.47,
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
      return new RasterBiasLayer(COUNT, WIDTH, HEIGHT, DEPTH, new DeterministicRandom());
   }
}