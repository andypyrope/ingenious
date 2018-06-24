package io.github.andypyrope.ai;

import io.github.andypyrope.ai.data.RasterData;
import io.github.andypyrope.ai.testutil.TestUtil;
import io.github.andypyrope.ai.util.StandardVector;
import io.github.andypyrope.ai.util.Vector;
import org.easymock.EasyMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class NetworkLayerBaseTest {

   private static final int INPUT_COUNT = 2;
   private static final Vector INPUT_SIZE = new StandardVector(3, 4, 2);
   private static final int OUTPUT_COUNT = 3;
   private static final Vector OUTPUT_SIZE = new StandardVector(1, 2, 1);

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
   void testCreation() {
      final Vector inputSize = EasyMock.createMock(Vector.class);
      inputSize.validateAsSize();
      EasyMock.expectLastCall();

      final Vector outputSize = EasyMock.createMock(Vector.class);
      outputSize.validateAsSize();
      EasyMock.expectLastCall();

      EasyMock.replay(inputSize, outputSize);
      new CustomLayer(1, inputSize, 1, outputSize);
      EasyMock.verify(inputSize, outputSize);
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
      expectInvalidSizeException(
            () -> layer.setSurroundingLayers(makePreviousLayer(layer),
            makeNextLayer(false, false)));
      expectInvalidSizeException(
            () -> layer.setSurroundingLayers(makePreviousLayer(layer),
            makeNextLayer(true, false)));

      Assertions.assertNull(((CustomLayer) layer)._previousLayer);
      Assertions.assertNull(((CustomLayer) layer)._nextLayer);
   }

   @Test
   void testSetSurroundingLayersValidNextLayer() {
      final NetworkLayer layer = new CustomLayer();
      layer.setSurroundingLayers(makePreviousLayer(layer),
            makeNextLayer(true, true));

      Assertions.assertNotNull(((CustomLayer) layer)._previousLayer);
      Assertions.assertNotNull(((CustomLayer) layer)._nextLayer);
   }

   @Test
   void testValidateSize() {
      expectInvalidSizeException(() ->
            new CustomLayer().validateSize(makeNextLayer(false, false)));
      expectInvalidSizeException(() ->
            new CustomLayer().validateSize(makeNextLayer(true, false)));

      new CustomLayer().validateSize(makeNextLayer(true, true));
   }

   private NetworkLayer makeNextLayer(final boolean hasValidCount,
         final boolean hasValidSize) {

      final NetworkLayer nextLayer = EasyMock.createMock(NetworkLayer.class);
      _objectsToVerify.add(nextLayer);

      EasyMock.expect(nextLayer.getInputCount()).andReturn(OUTPUT_COUNT +
            (hasValidCount ? 0 : 1)).atLeastOnce();
      if (!hasValidCount) {
         EasyMock.replay(nextLayer);
         return nextLayer;
      }

      EasyMock.expect(nextLayer.getInputSize())
            .andReturn(hasValidSize ? OUTPUT_SIZE : OUTPUT_SIZE.plus(1))
            .atLeastOnce();

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

   private void expectInvalidSizeException(final Runnable runnable) {
      TestUtil.expectException(InvalidSizeException.class, runnable);
   }

   private class CustomLayer extends NetworkLayerBase {

      CustomLayer() {
         this(INPUT_COUNT, OUTPUT_COUNT);
      }

      CustomLayer(final int inputCount, final int outputCount) {
         this(inputCount, INPUT_SIZE, outputCount, OUTPUT_SIZE);
      }

      CustomLayer(final int inputCount, final Vector inputSize,
            final int outputCount, final Vector outputSize) {

         super(inputCount, inputSize, outputCount, outputSize);
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