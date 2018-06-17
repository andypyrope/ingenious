package io.github.andypyrope.ai.raster.layers;

import io.github.andypyrope.ai.InvalidOperationException;
import io.github.andypyrope.ai.NetworkLayer;
import io.github.andypyrope.ai.NoAdjustmentException;
import io.github.andypyrope.ai.NoCalculationException;
import io.github.andypyrope.ai.data.CustomRasterData;
import io.github.andypyrope.ai.data.MismatchException;
import io.github.andypyrope.ai.data.RasterData;
import io.github.andypyrope.ai.raster.RasterLayer;
import io.github.andypyrope.ai.testutil.TestUtil;
import io.github.andypyrope.ai.util.RasterSize;
import io.github.andypyrope.ai.util.TriRasterSize;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RasterLayerBaseTest {

   private static final int INPUT_COUNT = 2;
   private static final RasterSize INPUT_SIZE = new TriRasterSize(3, 4, 2);
   private static final RasterData[] INITIAL_INPUT_GRADIENT = new RasterData[INPUT_COUNT];

   private static final int OUTPUT_COUNT = 3;
   private static final RasterSize OUTPUT_SIZE = new TriRasterSize(1, 2, 1);
   private static final RasterData[] INITIAL_OUTPUT = new RasterData[OUTPUT_COUNT];

   private static final RasterSize ATOMIC_SIZE = new TriRasterSize(1, 1, 1);

   private RasterData[] _input;
   private RasterData[] _outputGradient;

   @BeforeEach
   void setUp() {
      _input = new RasterData[INPUT_COUNT];
      for (int i = 0; i < INPUT_COUNT; i++) {
         INITIAL_INPUT_GRADIENT[i] = EasyMock.createMock(RasterData.class);
      }
      _outputGradient = new RasterData[OUTPUT_COUNT];
      for (int i = 0; i < OUTPUT_COUNT; i++) {
         INITIAL_OUTPUT[i] = EasyMock.createMock(RasterData.class);
      }
   }

   @Test
   void testGetInputSize() {
      final RasterSize actualSize = makeLayer().getInputSize();
      if (INPUT_SIZE.differsFrom(actualSize)) {
         throw new MismatchException(INPUT_SIZE, actualSize);
      }
   }

   @Test
   void testGetOutputSize() {
      final RasterSize actualSize = makeLayer().getOutputSize();
      if (OUTPUT_SIZE.differsFrom(actualSize)) {
         throw new MismatchException(OUTPUT_SIZE, actualSize);
      }
   }

   @Test
   void testCalculateWithInput() {
      final RasterData[] input = new RasterData[]{
            makeDataWithValidDimensions(),
            makeDataWithValidDimensions(),
      };
      makeLayer().calculate(input);
      for (int i = 0; i < INPUT_COUNT; i++) {
         Assertions.assertSame(input[i], _input[i]);
      }
   }

   @Test
   void testCalculateWithInputWithInvalidCount() {
      final RasterData[] input = new RasterData[INPUT_COUNT + 1];
      expectMismatchException(() -> makeLayer().calculate(input));
   }

   @Test
   void testCalculateWithPreviousLayer() {
      final RasterData[] input = new RasterData[2];

      final NetworkLayer layer = makeLayer();
      final NetworkLayer previous = EasyMock.createMock(NetworkLayer.class);
      EasyMock.expect(previous.getOutputAsRaster()).andReturn(input);
      previous.validateSize(layer);
      EasyMock.expectLastCall();
      EasyMock.replay(previous);

      layer.setSurroundingLayers(previous, null);
      layer.calculate();
      Assertions.assertSame(input, _input);

      EasyMock.verify(previous);
   }

   @Test
   void testCalculateWithNoPreviousLayer() {
      expectInvalidOperationException(() -> calculate(makeLayer()).calculate());
   }

   @Test
   void testAdjustWithOutput() {
      final RasterData[] targetOutput = new RasterData[]{
            new CustomRasterData(OUTPUT_SIZE),
            new CustomRasterData(OUTPUT_SIZE),
            new CustomRasterData(OUTPUT_SIZE),
      };
      targetOutput[0].setAll(0.0);
      targetOutput[1].setAll(1.0);
      targetOutput[2].setAll(2.0);

      for (int i = 0; i < OUTPUT_COUNT; i++) {
         INITIAL_OUTPUT[i] = new CustomRasterData(OUTPUT_SIZE);
         INITIAL_OUTPUT[i].setAll(1.0);
      }

      for (int i = 0; i < OUTPUT_COUNT; i++) {
         Assertions.assertNull(_outputGradient[i]);
      }
      calculate(makeLayer()).adjust(targetOutput);
      _outputGradient[0].forEach((x, y, z) ->
            Assertions.assertEquals(1.0, _outputGradient[0].getCell(x, y, z)));
      _outputGradient[1].forEach((x, y, z) ->
            Assertions.assertEquals(0.0, _outputGradient[1].getCell(x, y, z)));
      _outputGradient[2].forEach((x, y, z) ->
            Assertions.assertEquals(-1.0, _outputGradient[2].getCell(x, y, z)));
   }

   @Test
   void testAdjustWithMismatchingOutput() {
      expectMismatchException(
            () -> calculate(makeLayer()).adjust(new RasterData[OUTPUT_COUNT + 1]));
   }

   @Test
   void testAdjustWithNoCalculation() {
      expectNoCalculationException(
            () -> makeLayer().adjust(new RasterData[OUTPUT_COUNT]));
   }

   @Test
   void testAdjustWithNextLayer() {
      final RasterData[] outputGradient = new RasterData[OUTPUT_COUNT];

      final NetworkLayer next = makeNextLayer();
      EasyMock.expect(next.getInputGradientAsRaster()).andReturn(outputGradient);
      EasyMock.replay(next);

      final NetworkLayer layer = calculate(makeLayer());
      layer.setSurroundingLayers(null, next);
      layer.adjust();
      Assertions.assertSame(outputGradient, _outputGradient);

      EasyMock.verify(next);
   }

   @Test
   void testAdjustWithNoNextLayer() {
      expectInvalidOperationException(() -> calculate(makeLayer()).adjust());
   }

   @Test
   void testAdjustWithNextLayerWithNoCalculation() {
      final NetworkLayer next = makeNextLayer();
      EasyMock.replay(next);

      final NetworkLayer layer = makeLayer();
      layer.setSurroundingLayers(null, next);
      expectNoCalculationException(layer::adjust);
      Assertions.assertArrayEquals(new RasterData[OUTPUT_COUNT], _outputGradient);

      EasyMock.verify(next);
   }

   @Test
   void testGetOutputAsAtomic() {
      expectMismatchException(() -> calculate(new CustomRasterLayer(
            ATOMIC_SIZE, new TriRasterSize(2, 1, 1))).getOutputAsAtomic());
      expectMismatchException(() -> calculate(new CustomRasterLayer(
            ATOMIC_SIZE, new TriRasterSize(1, 2, 1))).getOutputAsAtomic());
      expectMismatchException(() -> calculate(new CustomRasterLayer(
            ATOMIC_SIZE, new TriRasterSize(1, 1, 2))).getOutputAsAtomic());

      final double[] output = new double[OUTPUT_COUNT];
      for (int i = 0; i < OUTPUT_COUNT; i++) {
         output[i] = (double) i;
         EasyMock.expect(INITIAL_OUTPUT[i].getCell(0, 0, 0)).andReturn(output[i]);
         EasyMock.replay(INITIAL_OUTPUT[i]);
      }
      TestUtil.compareDoubleArrays(output,
            calculate(makeAtomicLayer()).getOutputAsAtomic());
      EasyMock.verify((Object[]) INITIAL_OUTPUT);
   }

   @Test
   void testGetInputGradientAsAtomic() {
      expectMismatchException(() -> adjust(new CustomRasterLayer(
            new TriRasterSize(2, 1, 1), ATOMIC_SIZE)).getInputGradientAsAtomic());
      expectMismatchException(() -> adjust(new CustomRasterLayer(
            new TriRasterSize(1, 2, 1), ATOMIC_SIZE)).getInputGradientAsAtomic());
      expectMismatchException(() -> adjust(new CustomRasterLayer(
            new TriRasterSize(1, 1, 2), ATOMIC_SIZE)).getInputGradientAsAtomic());

      final double[] inputGradient = new double[INPUT_COUNT];
      for (int i = 0; i < INPUT_COUNT; i++) {
         inputGradient[i] = (double) i;
         EasyMock.expect(INITIAL_INPUT_GRADIENT[i].getCell(0, 0, 0))
               .andReturn(inputGradient[i]);
         EasyMock.replay(INITIAL_INPUT_GRADIENT[i]);
      }
      TestUtil.compareDoubleArrays(inputGradient,
            adjust(makeAtomicLayer()).getInputGradientAsAtomic());
      EasyMock.verify((Object[]) INITIAL_INPUT_GRADIENT);
   }

   @Test
   void testGetOutputAsRaster() {
      TestUtil.assertArraySame(INITIAL_OUTPUT,
            calculate(makeLayer()).getOutputAsRaster());
   }

   @Test
   void testGetInputGradientAsRaster() {
      TestUtil.assertArraySame(INITIAL_INPUT_GRADIENT,
            adjust(makeLayer()).getInputGradientAsRaster());
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

   private NetworkLayer makeNextLayer() {
      final NetworkLayer next = EasyMock.createMock(NetworkLayer.class);
      EasyMock.expect(next.getInputCount()).andReturn(OUTPUT_COUNT);
      EasyMock.expect(next.getInputSize()).andReturn(OUTPUT_SIZE);
      return next;
   }

   private RasterLayer adjust(final RasterLayer layer) {
      ((CustomRasterLayer) layer).dummyAdjust();
      return layer;
   }

   private RasterLayer calculate(final RasterLayer layer) {
      ((CustomRasterLayer) layer).dummyCalculate();
      return layer;
   }

   private RasterLayer makeLayer() {
      return new CustomRasterLayer();
   }

   private RasterLayer makeAtomicLayer() {
      return new CustomRasterLayer(ATOMIC_SIZE, ATOMIC_SIZE);
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

   private RasterData makeDataWithValidDimensions() {
      final RasterData result = EasyMock.createMock(RasterData.class);
      result.verifyDimensions(INPUT_SIZE);
      EasyMock.expectLastCall().once();
      EasyMock.replay(result);
      return result;
   }

   private class CustomRasterLayer extends RasterLayerBase {

      CustomRasterLayer() {
         this(INPUT_SIZE, OUTPUT_SIZE);
      }

      CustomRasterLayer(final RasterSize inputSize, final RasterSize outputSize) {
         super(INPUT_COUNT, inputSize, OUTPUT_COUNT, outputSize);
         System.arraycopy(INITIAL_INPUT_GRADIENT, 0, _inputGradients, 0, INPUT_COUNT);
         System.arraycopy(INITIAL_OUTPUT, 0, _output, 0, OUTPUT_COUNT);
      }

      private void dummyCalculate() {
         _hasNoCalculation = false;
      }

      private void dummyAdjust() {
         _hasNoAdjustment = false;
      }

      @Override
      protected void adjustWithGradient(final RasterData[] outputGradient) {
         _outputGradient = outputGradient;
      }

      @Override
      protected void calculateWithInput(final RasterData[] input) {
         _input = input;
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