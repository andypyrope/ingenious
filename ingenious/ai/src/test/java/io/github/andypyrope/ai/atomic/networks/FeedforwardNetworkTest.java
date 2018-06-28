package io.github.andypyrope.ai.atomic.networks;

import io.github.andypyrope.ai.activation.ActivationFunction;
import io.github.andypyrope.ai.atomic.AtomicNetwork;
import io.github.andypyrope.ai.testutil.DeterministicRandom;
import io.github.andypyrope.ai.testutil.TestUtil;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FeedforwardNetworkTest {

   private static final int INPUT_SIZE = 2;
   private static final int[] HIDDEN_LAYERS = new int[]{3, 4};
   private static final int OUTPUT_SIZE = 5;

   private ActivationFunction _function;

   @BeforeEach
   void setUp() {
      _function = EasyMock.createMock(ActivationFunction.class);
      EasyMock.expect(_function.getSlope(EasyMock.anyDouble(), EasyMock.anyDouble()))
            .andReturn(1.0).anyTimes();
      EasyMock.expect(_function.getOutput(EasyMock.anyDouble()))
            .andReturn(2.0).anyTimes();
      EasyMock.replay(_function);
   }

   @Test
   void getComplexity() {
      assertEquals(172, makeNetwork().getComplexity());
   }

   @Test
   void calculate() {
      makeNetwork().calculate(new double[]{3, 4});
   }

   @Test
   void adjust() {
      final AtomicNetwork network = makeNetwork();
      network.calculate(new double[]{1, 2});
      network.adjust(new double[]{1, 2, 3, -4, 5});
   }

   @Test
   void getOutput() {
      final AtomicNetwork network = makeNetwork();
      network.calculate(new double[]{3, 4});
      TestUtil.compareDoubleArrays(new double[]{2.0, 2.0, 2.0, 2.0, 2.0},
            network.getOutput());
   }

   @Test
   void getEuclideanDistance() {
      final AtomicNetwork network = makeNetwork();
      network.calculate(new double[]{3, 4});
      TestUtil.compareDoublesLoose(3.87,
            network.getEuclideanDistance(new double[]{1, 2, 3, 4, 5}));
   }

   private AtomicNetwork makeNetwork() {
      return new FeedforwardNetwork(INPUT_SIZE, HIDDEN_LAYERS, OUTPUT_SIZE, _function,
            new DeterministicRandom());
   }
}