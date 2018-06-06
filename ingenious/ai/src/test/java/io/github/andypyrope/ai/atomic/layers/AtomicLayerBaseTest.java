package io.github.andypyrope.ai.atomic.layers;

import io.github.andypyrope.ai.*;
import io.github.andypyrope.ai.atomic.AtomicLayer;
import io.github.andypyrope.ai.data.RasterData;
import io.github.andypyrope.ai.testutil.TestUtil;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AtomicLayerBaseTest {

   private static int INPUT_SIZE = 2;
   private static double[] INITIAL_INPUT_GRADIENT = new double[]{0.2, 0.4};
   private static int OUTPUT_SIZE = 3;
   private static double[] INITIAL_OUTPUT = new double[]{0.1, -4.0, 4.0};

   private double[] _input;
   private double[] _outputGradient;

   @Test
   void testSetSurroundingLayersMismatchingPreviousLayer() {
      final NetworkLayer previous = EasyMock.createMock(NetworkLayer.class);
      EasyMock.expect(previous.getOutputSize()).andReturn(1).times(2);
      final NetworkLayer next = EasyMock.createMock(NetworkLayer.class);
      EasyMock.replay(previous, next);

      expectMismatchException(() -> makeLayer().setSurroundingLayers(previous, next));
      EasyMock.verify(previous, next);
   }

   @Test
   void testSetSurroundingLayersMismatchingNextLayer() {
      final NetworkLayer previous = EasyMock.createMock(NetworkLayer.class);
      EasyMock.expect(previous.getOutputSize()).andReturn(2);
      final NetworkLayer next = EasyMock.createMock(NetworkLayer.class);
      EasyMock.expect(next.getInputSize()).andReturn(2).times(2);
      EasyMock.replay(previous, next);

      expectMismatchException(() -> makeLayer().setSurroundingLayers(previous, next));
      EasyMock.verify(previous, next);
   }

   @Test
   void testGetInputSize() {
      Assertions.assertEquals(2, makeLayer().getInputSize());
   }

   @Test
   void testGetOutputSize() {
      Assertions.assertEquals(3, makeLayer().getOutputSize());
   }

   @Test
   void testCalculateWithInput() {
      final double[] input = new double[2];
      makeLayer().calculate(input);
      Assertions.assertSame(input, _input);
   }

   @Test
   void testCalculateWithMismatchingInput() {
      expectMismatchException(() -> makeLayer().calculate(new double[3]));
   }

   @Test
   void testCalculateWithPreviousLayer() {
      final double[] input = new double[2];

      final NetworkLayer previous = EasyMock.createMock(NetworkLayer.class);
      EasyMock.expect(previous.getOutputSize()).andReturn(input.length);
      EasyMock.expect(previous.getOutputAsAtomic()).andReturn(input);
      EasyMock.replay(previous);

      final NetworkLayer layer = makeLayer();
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
      layer.calculate(new double[INPUT_SIZE]);
      TestUtil.compareDoubles(5.0, layer.getEuclideanDistance(new double[]{3.0, 4.0}));
   }

   @Test
   void testGetEuclideanDistanceWithNoCalculation() {
      expectNoCalculationException(() ->
            makeLayer().getEuclideanDistance(new double[OUTPUT_SIZE]));
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
      expectMismatchException(() -> makeLayerWithCalculation().adjust(new double[2]));
   }

   @Test
   void testAdjustWithNoCalculation() {
      expectNoCalculationException(() -> makeLayer().adjust(new double[OUTPUT_SIZE]));
   }

   @Test
   void testAdjustWithNextLayer() {
      final double[] outputGradient = new double[3];

      final NetworkLayer next = EasyMock.createMock(NetworkLayer.class);
      EasyMock.expect(next.getInputSize()).andReturn(outputGradient.length);
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
      final double[] outputGradient = new double[3];

      final NetworkLayer next = EasyMock.createMock(NetworkLayer.class);
      EasyMock.expect(next.getInputSize()).andReturn(outputGradient.length);
      EasyMock.replay(next);

      final NetworkLayer layer = makeLayer();
      layer.setSurroundingLayers(null, next);
      expectNoCalculationException(layer::adjust);
      Assertions.assertNotSame(outputGradient, _outputGradient);

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
      layer.adjust(new double[OUTPUT_SIZE]);
      return layer;
   }

   private AtomicLayer makeLayerWithCalculation() {
      final AtomicLayer layer = makeLayer();
      layer.calculate(new double[INPUT_SIZE]);
      return layer;
   }

   private AtomicLayer makeLayer() {
      return new CustomAtomicLayer();
   }

   private void expectNoAdjustmentException(final Runnable runnable) {
      expectException(NoAdjustmentException.class, runnable);
   }

   private void expectInvalidOperationException(final Runnable runnable) {
      expectException(InvalidOperationException.class, runnable);
   }

   private void expectNoCalculationException(final Runnable runnable) {
      expectException(NoCalculationException.class, runnable);
   }

   private void expectMismatchException(final Runnable runnable) {
      expectException(MismatchException.class, runnable);
   }

   private void expectException(final Class<? extends Exception> exceptionClass,
         final Runnable runnable) {
      Exception exception = null;
      try {
         runnable.run();
      } catch (final Exception e) {
         exception = e;
      }
      Assertions.assertNotNull(exception, "An exception has been thrown");
      Assertions.assertSame(exceptionClass, exception.getClass(),
            String.format("The caught exception is of type '%s'",
                  exceptionClass.getSimpleName()));
   }

   private class CustomAtomicLayer extends AtomicLayerBase {

      CustomAtomicLayer() {
         super(INPUT_SIZE, OUTPUT_SIZE);

         System.arraycopy(INITIAL_INPUT_GRADIENT, 0, _inputGradient, 0, INPUT_SIZE);
         System.arraycopy(INITIAL_OUTPUT, 0, _output, 0, OUTPUT_SIZE);
      }

      CustomAtomicLayer(final int inputSize, final int outputSize) {
         super(inputSize, outputSize);
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