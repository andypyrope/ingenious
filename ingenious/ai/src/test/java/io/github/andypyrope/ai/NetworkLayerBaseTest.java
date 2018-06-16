package io.github.andypyrope.ai;

import io.github.andypyrope.ai.data.MismatchException;
import io.github.andypyrope.ai.data.RasterData;
import io.github.andypyrope.ai.testutil.TestUtil;
import org.easymock.EasyMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class NetworkLayerBaseTest {

   private static final int INPUT_COUNT = 2;
   private static final int INPUT_WIDTH = 3;
   private static final int INPUT_HEIGHT = 4;
   private static final int INPUT_DEPTH = 2;

   private static final int OUTPUT_COUNT = 3;
   private static final int OUTPUT_WIDTH = 1;
   private static final int OUTPUT_HEIGHT = 2;
   private static final int OUTPUT_DEPTH = 1;

   private List<Object> _objectsToVerify;

   @BeforeEach
   void setUp() {
      _objectsToVerify = new ArrayList<>();
   }

   @AfterEach
   void tearDown() {
      EasyMock.verify(_objectsToVerify.toArray());
   }

   @Test
   void testGetInputCount() {
      Assertions.assertEquals(INPUT_COUNT, new CustomLayer().getInputCount());
   }

   @Test
   void testGetOutputCount() {
      Assertions.assertEquals(OUTPUT_COUNT, new CustomLayer().getOutputCount());
   }

   @Test
   void testSetSurroundingLayersLonely() {
      final NetworkLayer layer = new CustomLayer();
      layer.setSurroundingLayers(null, null);

      Assertions.assertNull(((CustomLayer) layer)._previousLayer);
      Assertions.assertNull(((CustomLayer) layer)._nextLayer);
   }

   @Test
   void testSetSurroundingLayersInvalidNextLayer() {
      final NetworkLayer layer = new CustomLayer();
      expectMismatchException(() -> layer.setSurroundingLayers(makePreviousLayer(layer),
            makeNextLayer(false, false, false, false)));
      expectMismatchException(() -> layer.setSurroundingLayers(makePreviousLayer(layer),
            makeNextLayer(true, false, false, false)));
      expectMismatchException(() -> layer.setSurroundingLayers(makePreviousLayer(layer),
            makeNextLayer(true, true, false, false)));
      expectMismatchException(() -> layer.setSurroundingLayers(makePreviousLayer(layer),
            makeNextLayer(true, true, true, false)));

      Assertions.assertNull(((CustomLayer) layer)._previousLayer);
      Assertions.assertNull(((CustomLayer) layer)._nextLayer);
   }

   @Test
   void testSetSurroundingLayersValidNextLayer() {
      final NetworkLayer layer = new CustomLayer();
      layer.setSurroundingLayers(makePreviousLayer(layer),
            makeNextLayer(true, true, true, true));

      Assertions.assertNotNull(((CustomLayer) layer)._previousLayer);
      Assertions.assertNotNull(((CustomLayer) layer)._nextLayer);
   }

   @Test
   void validateSize() {
      expectMismatchException(() ->
            new CustomLayer().validateSize(makeNextLayer(false, false, false, false)));
      expectMismatchException(() ->
            new CustomLayer().validateSize(makeNextLayer(true, false, false, false)));
      expectMismatchException(() ->
            new CustomLayer().validateSize(makeNextLayer(true, true, false, false)));
      expectMismatchException(() ->
            new CustomLayer().validateSize(makeNextLayer(true, true, true, false)));

      new CustomLayer().validateSize(makeNextLayer(true, true, true, true));
   }

   private NetworkLayer makeNextLayer(final boolean hasValidCount,
         final boolean hasValidWidth, final boolean hasValidHeight,
         final boolean hasValidDepth) {

      final NetworkLayer nextLayer = EasyMock.createMock(NetworkLayer.class);
      _objectsToVerify.add(nextLayer);

      EasyMock.expect(nextLayer.getInputCount()).andReturn(OUTPUT_COUNT +
            (hasValidCount ? 0 : 1)).atLeastOnce();
      if (!hasValidCount) {
         EasyMock.replay(nextLayer);
         return nextLayer;
      }

      EasyMock.expect(nextLayer.getInputWidth()).andReturn(OUTPUT_WIDTH +
            (hasValidWidth ? 0 : 1)).atLeastOnce();
      EasyMock.expect(nextLayer.getInputHeight()).andReturn(OUTPUT_HEIGHT +
            (hasValidHeight ? 0 : 1)).atLeastOnce();
      EasyMock.expect(nextLayer.getInputDepth()).andReturn(OUTPUT_DEPTH +
            (hasValidDepth ? 0 : 1)).atLeastOnce();

      EasyMock.replay(nextLayer);
      return nextLayer;
   }

   private NetworkLayer makePreviousLayer(final NetworkLayer layer) {
      final NetworkLayer previous = EasyMock.createMock(NetworkLayer.class);
      previous.validateSize(layer);
      EasyMock.replay(previous);
      _objectsToVerify.add(previous);
      return previous;
   }

   private void expectMismatchException(final Runnable runnable) {
      TestUtil.expectException(MismatchException.class, runnable);
   }

   private class CustomLayer extends NetworkLayerBase {

      final int _inputWidth;
      final int _inputHeight;
      final int _inputDepth;

      final int _outputWidth;
      final int _outputHeight;
      final int _outputDepth;

      CustomLayer() {
         this(INPUT_COUNT, OUTPUT_COUNT);
      }

      CustomLayer(final int inputCount, final int outputCount) {
         this(inputCount, INPUT_WIDTH, INPUT_HEIGHT, INPUT_DEPTH,
               outputCount, OUTPUT_WIDTH, OUTPUT_HEIGHT, OUTPUT_DEPTH);
      }

      CustomLayer(final int inputCount, final int inputWidth, final int inputHeight,
            final int inputDepth,
            final int outputCount, final int outputWidth, final int outputHeight,
            final int outputDepth) {
         super(inputCount, outputCount);

         _inputWidth = inputWidth;
         _inputHeight = inputHeight;
         _inputDepth = inputDepth;

         _outputWidth = outputWidth;
         _outputHeight = outputHeight;
         _outputDepth = outputDepth;
      }

      @Override
      public int getCalculationComplexity() {
         return 0;
      }

      @Override
      public int getAdjustmentComplexity() {
         return 0;
      }

      @Override
      public int getInputWidth() {
         return _inputWidth;
      }

      @Override
      public int getInputHeight() {
         return _inputHeight;
      }

      @Override
      public int getInputDepth() {
         return _inputDepth;
      }

      @Override
      public int getOutputWidth() {
         return _outputWidth;
      }

      @Override
      public int getOutputHeight() {
         return _outputHeight;
      }

      @Override
      public int getOutputDepth() {
         return _outputDepth;
      }

      @Override
      public void calculate() {
         // Do nothing
      }

      @Override
      public void adjust() throws NoCalculationException, InvalidOperationException {
         // Do nothing
      }

      @Override
      public double[] getOutputAsAtomic()
            throws NoCalculationException, InvalidOperationException {
         return new double[0];
      }

      @Override
      public double[] getInputGradientAsAtomic()
            throws NoAdjustmentException, InvalidOperationException {
         return new double[0];
      }

      @Override
      public RasterData[] getOutputAsRaster() throws NoCalculationException {
         return new RasterData[0];
      }

      @Override
      public RasterData[] getInputGradientAsRaster() throws NoAdjustmentException {
         return new RasterData[0];
      }
   }
}