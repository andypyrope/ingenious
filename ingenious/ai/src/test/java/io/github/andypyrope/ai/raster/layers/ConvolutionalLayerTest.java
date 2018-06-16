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
import java.util.concurrent.atomic.AtomicReference;

class ConvolutionalLayerTest {

   private static final int INPUT_COUNT = 3;
   private static final int INPUT_WIDTH = 2;
   private static final int INPUT_HEIGHT = 3;
   private static final int INPUT_DEPTH = 3;
   private static final RasterData[] INPUT = new RasterData[INPUT_COUNT];

   private static final int FILTER_COUNT = 2;
   private static final int FILTER_WIDTH = 1;
   private static final int FILTER_HEIGHT = 2;
   private static final int FILTER_DEPTH = 3;

   private static final int OUTPUT_COUNT = 6;
   private static final int OUTPUT_WIDTH = 2;
   private static final int OUTPUT_HEIGHT = 2;
   private static final int OUTPUT_DEPTH = 1;
   private static final RasterData[] ACTUAL_OUTPUT = new RasterData[OUTPUT_COUNT];
   private static final RasterData[] TARGET_OUTPUT = new RasterData[OUTPUT_COUNT];

   private static final Random STATIC_RANDOM = new DeterministicRandom();

   @BeforeAll
   static void setUpAll() {
      for (int i = 0; i < INPUT_COUNT; i++) {
         INPUT[i] = makeDummyData(INPUT_WIDTH, INPUT_HEIGHT, INPUT_DEPTH);
      }

      ACTUAL_OUTPUT[0] = makeOutputRasterData(new double[][][]{new double[][]{
            new double[]{0.041, 0.049},
            new double[]{0.049, 0.049},
      }});
      ACTUAL_OUTPUT[1] = makeOutputRasterData(new double[][][]{new double[][]{
            new double[]{0.049, 0.058},
            new double[]{0.058, 0.058},
      }});
      ACTUAL_OUTPUT[2] = makeOutputRasterData(new double[][][]{new double[][]{
            new double[]{0.049, 0.049},
            new double[]{0.049, 0.049},
      }});
      ACTUAL_OUTPUT[3] = makeOutputRasterData(new double[][][]{new double[][]{
            new double[]{0.058, 0.058},
            new double[]{0.058, 0.058},
      }});
      ACTUAL_OUTPUT[4] = makeOutputRasterData(new double[][][]{new double[][]{
            new double[]{0.049, 0.049},
            new double[]{0.049, 0.049},
      }});
      ACTUAL_OUTPUT[5] = makeOutputRasterData(new double[][][]{new double[][]{
            new double[]{0.058, 0.058},
            new double[]{0.058, 0.058},
      }});

      for (int i = 0; i < OUTPUT_COUNT; i++) {
         TARGET_OUTPUT[i] = makeDummyData(OUTPUT_WIDTH, OUTPUT_HEIGHT, OUTPUT_DEPTH);
      }
   }

   private static RasterData makeOutputRasterData(final double[][][] numeric) {
      final RasterData result = new CustomRasterData(OUTPUT_WIDTH, OUTPUT_HEIGHT,
            OUTPUT_DEPTH);
      for (int x = 0; x < OUTPUT_WIDTH; x++) {
         for (int y = 0; y < OUTPUT_HEIGHT; y++) {
            for (int z = 0; z < OUTPUT_DEPTH; z++) {
               result.setCell(x, y, z, numeric[z][y][x]);
            }
         }
      }
      return result;
   }

   private static RasterData makeDummyData(final int width, final int height,
         final int depth) {

      final RasterData result = new CustomRasterData(width, height, depth);
      for (int x = 0; x < width; x++) {
         for (int y = 0; y < height; y++) {
            for (int z = 0; z < depth; z++) {
               result.setCell(x, y, z, STATIC_RANDOM.nextDouble());
            }
         }
      }
      return result;
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
   void testCalculation() {
      final RasterLayer layer = makeLayer();
      layer.calculate(INPUT);
      TestUtil.compareRasterDataArrays(ACTUAL_OUTPUT, layer.getOutputAsRaster());
   }

   @Test
   void testLearning() {
      final RasterLayer layer = makeLayer();
      layer.calculate(INPUT);
      TestUtil.compareDoubles(0.92, getEuclideanDistance(layer));
      train(layer);
      TestUtil.compareDoubles(0.38, getEuclideanDistance(layer));
      train(layer);
      TestUtil.compareDoubles(0.06, getEuclideanDistance(layer));
   }

   private double getEuclideanDistance(final RasterLayer layer) {
      final RasterData[] output = layer.getOutputAsRaster();
      AtomicReference<Double> squaredDistance = new AtomicReference<>(0.0);
      for (int i = 0; i < output.length; i++) {
         final RasterData currentOutput = output[i];
         final RasterData targetOutput = TARGET_OUTPUT[i];
         currentOutput.forEach((x, y, z) -> {
            final double difference = currentOutput.getCell(x, y, z) -
                  targetOutput.getCell(x, y, z);
            squaredDistance.set(squaredDistance.get() + difference * difference);
         });
      }
      return Math.sqrt(squaredDistance.get());
   }

   private void train(final RasterLayer layer) {
      for (int i = 0; i < 5; i++) {
         layer.adjust(TARGET_OUTPUT);
         layer.calculate(INPUT);
      }
   }

   private RasterLayer makeLayer() {
      return new ConvolutionalLayer(
            INPUT_COUNT, INPUT_WIDTH, INPUT_HEIGHT, INPUT_DEPTH,
            FILTER_COUNT, FILTER_WIDTH, FILTER_HEIGHT, FILTER_DEPTH,
            new DeterministicRandom());
   }
}