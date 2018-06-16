package io.github.andypyrope.ai.atomic.layers;

import io.github.andypyrope.ai.InvalidOperationException;
import io.github.andypyrope.ai.NetworkLayer;
import io.github.andypyrope.ai.NoAdjustmentException;
import io.github.andypyrope.ai.NoCalculationException;
import io.github.andypyrope.ai.atomic.AtomicLayer;
import io.github.andypyrope.ai.data.MismatchException;
import io.github.andypyrope.ai.data.RasterData;
import io.github.andypyrope.ai.testutil.TestUtil;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AtomicLayerBaseTest {

   private static int INPUT_COUNT = 2;
   private static double[] INITIAL_INPUT_GRADIENT = new double[]{0.2, 0.4};
   private static int OUTPUT_COUNT = 3;
   private static double[] INITIAL_OUTPUT = new double[]{0.1, -4.0, 4.0};

   private double[] _input;
   private double[] _outputGradient;

   @Test
   void testGetInputCount() {
      Assertions.assertEquals(INPUT_COUNT, makeLayer().getInputCount());
   }

   @Test
   void testGetInputWidth() {
      Assertions.assertEquals(1, makeLayer().getInputWidth());
   }

   @Test
   void testGetInputHeight() {
      Assertions.assertEquals(1, makeLayer().getInputHeight());
   }

   @Test
   void testGetInputDepth() {
      Assertions.assertEquals(1, makeLayer().getInputDepth());
   }

   @Test
   void testGetOutputCount() {
      Assertions.assertEquals(OUTPUT_COUNT, makeLayer().getOutputCount());
   }

   @Test
   void testGetOutputWidth() {
      Assertions.assertEquals(1, makeLayer().getOutputWidth());
   }

   @Test
   void testGetOutputHeight() {
      Assertions.assertEquals(1, makeLayer().getOutputWidth());
   }

   @Test
   void testGetOutputDepth() {
      Assertions.assertEquals(1, makeLayer().getOutputWidth());
   }

   @Test
   void testCalculateWithInput() {
      final double[] input = new double[2];
      makeLayer().calculate(input);
      Assertions.assertSame(input, _input);
   }

   @Test
   void testCalculateWithInputWithInvalidCount() {
      expectMismatchException(() -> makeLayer().calculate(new double[3]));
   }

   @Test
   void testCalculateWithPreviousLayer() {
      final double[] input = new double[2];
      final NetworkLayer layer = makeLayer();

      final NetworkLayer previous = EasyMock.createMock(NetworkLayer.class);
      previous.validateSize(layer);
      EasyMock.expectLastCall();
      EasyMock.expect(previous.getOutputAsAtomic()).andReturn(input);
      EasyMock.replay(previous);

      layer.setSurroundingLayers(previous, null);
      layer.calculate();
      Assertions.assertSame(input, _input);

      EasyMock.verify(previous);
   }

   @Test
   void testCalculateWithNoPreviousLayer() {
      expectInvalidOperationException(() -> makeLayerWithCalculation().calculate());
   }

   @Test
   void testGetEuclideanDistance() {
      final AtomicLayer layer = new CustomAtomicLayer(2, 2);
      layer.calculate(new double[INPUT_COUNT]);
      TestUtil.compareDoubles(5.0, layer.getEuclideanDistance(new double[]{3.0, 4.0}));
   }

   @Test
   void testGetEuclideanDistanceWithNoCalculation() {
      expectNoCalculationException(() ->
            makeLayer().getEuclideanDistance(new double[OUTPUT_COUNT]));
   }

   @Test
   void testGetEuclideanDistanceWithMismatchingOutput() {
      expectMismatchException(() ->
            makeLayerWithCalculation().getEuclideanDistance(new double[2]));
   }

   @Test
   void testAdjustWithOutput() {
      makeLayerWithCalculation().adjust(new double[3]);
      TestUtil.compareDoubleArrays(INITIAL_OUTPUT, _outputGradient);
   }

   @Test
   void testAdjustWithMismatchingOutput() {
      expectMismatchException(
            () -> makeLayerWithCalculation().adjust(new double[OUTPUT_COUNT + 1]));
   }

   @Test
   void testAdjustWithNoCalculation() {
      expectNoCalculationException(() -> makeLayer().adjust(new double[OUTPUT_COUNT]));
   }

   @Test
   void testAdjustWithNextLayer() {
      final double[] outputGradient = new double[OUTPUT_COUNT];

      final NetworkLayer next = EasyMock.createMock(NetworkLayer.class);
      EasyMock.expect(next.getInputCount()).andReturn(OUTPUT_COUNT);
      EasyMock.expect(next.getInputWidth()).andReturn(1);
      EasyMock.expect(next.getInputHeight()).andReturn(1);
      EasyMock.expect(next.getInputDepth()).andReturn(1);
      EasyMock.expect(next.getInputGradientAsAtomic()).andReturn(outputGradient);
      EasyMock.replay(next);

      final NetworkLayer layer = makeLayerWithCalculation();
      layer.setSurroundingLayers(null, next);
      layer.adjust();
      Assertions.assertSame(outputGradient, _outputGradient);

      EasyMock.verify(next);
   }

   @Test
   void testAdjustWithNoNextLayer() {
      expectInvalidOperationException(() -> makeLayerWithCalculation().adjust());
   }

   @Test
   void testAdjustWithNextLayerWithNoCalculation() {
      final NetworkLayer next = EasyMock.createMock(NetworkLayer.class);
      EasyMock.expect(next.getInputCount()).andReturn(OUTPUT_COUNT);
      EasyMock.expect(next.getInputWidth()).andReturn(1);
      EasyMock.expect(next.getInputHeight()).andReturn(1);
      EasyMock.expect(next.getInputDepth()).andReturn(1);
      EasyMock.replay(next);

      final NetworkLayer layer = makeLayer();
      layer.setSurroundingLayers(null, next);
      expectNoCalculationException(layer::adjust);
      Assertions.assertNull(_outputGradient);

      EasyMock.verify(next);
   }

   @Test
   void testGetOutputAsAtomic() {
      TestUtil.compareDoubleArrays(INITIAL_OUTPUT,
            makeLayerWithCalculation().getOutputAsAtomic());
   }

   @Test
   void testGetInputGradientAsAtomic() {
      TestUtil.compareDoubleArrays(INITIAL_INPUT_GRADIENT,
            makeAdjustedLayer().getInputGradientAsAtomic());
   }

   @Test
   void testGetOutputAsRaster() {
      TestUtil.compareDoubleArrays(INITIAL_OUTPUT,
            atomicRasterToDoubleArray(makeLayerWithCalculation().getOutputAsRaster()));
   }

   @Test
   void testGetInputGradientAsRaster() {
      TestUtil.compareDoubleArrays(INITIAL_INPUT_GRADIENT,
            atomicRasterToDoubleArray(makeAdjustedLayer().getInputGradientAsRaster()));
   }

   @Test
   void testGetOutputWithNoCalculation() {
      expectNoCalculationException(() -> makeLayer().getOutputAsAtomic());
      expectNoCalculationException(() -> makeLayer().getOutputAsRaster());
   }

   @Test
   void testGetInputGradientWithNoAdjustment() {
      expectNoAdjustmentException(() -> makeLayer().getInputGradientAsAtomic());
      expectNoAdjustmentException(() -> makeLayer().getInputGradientAsRaster());
   }

   private double[] atomicRasterToDoubleArray(final RasterData[] data) {
      final double[] result = new double[data.length];
      for (int i = 0; i < result.length; i++) {
         result[i] = data[i].getCell(0, 0, 0);
      }
      return result;
   }

   private AtomicLayer makeAdjustedLayer() {
      final AtomicLayer layer = makeLayerWithCalculation();
      layer.adjust(new double[OUTPUT_COUNT]);
      return layer;
   }

   private AtomicLayer makeLayerWithCalculation() {
      final AtomicLayer layer = makeLayer();
      layer.calculate(new double[INPUT_COUNT]);
      return layer;
   }

   private AtomicLayer makeLayer() {
      return new CustomAtomicLayer();
   }

   private void expectNoAdjustmentException(final Runnable runnable) {
      TestUtil.expectException(NoAdjustmentException.class, runnable);
   }

   private void expectInvalidOperationException(final Runnable runnable) {
      TestUtil.expectException(InvalidOperationException.class, runnable);
   }

   private void expectNoCalculationException(final Runnable runnable) {
      TestUtil.expectException(NoCalculationException.class, runnable);
   }

   private void expectMismatchException(final Runnable runnable) {
      TestUtil.expectException(MismatchException.class, runnable);
   }

   private class CustomAtomicLayer extends AtomicLayerBase {

      CustomAtomicLayer() {
         super(INPUT_COUNT, OUTPUT_COUNT);

         System.arraycopy(INITIAL_INPUT_GRADIENT, 0, _inputGradients, 0, INPUT_COUNT);
         System.arraycopy(INITIAL_OUTPUT, 0, _output, 0, OUTPUT_COUNT);
      }

      CustomAtomicLayer(final int inputCount, final int outputCount) {
         super(inputCount, outputCount);
      }

      @Override
      protected void calculateWithInput(final double[] input) {
         _input = input;
      }

      @Override
      protected void adjustWithGradient(final double[] outputGradient) {
         _outputGradient = outputGradient;
      }

      @Override
      public int getCalculationComplexity() {
         return 0;
      }

      @Override
      public int getAdjustmentComplexity() {
         return 0;
      }
   }
}