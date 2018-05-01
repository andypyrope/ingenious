package io.github.andypyrope.ai.atomic.neurons;

import io.github.andypyrope.ai.activation.LogisticFunction;
import io.github.andypyrope.ai.atomic.FeedforwardNeuronTestUtil;
import io.github.andypyrope.ai.testutil.TestUtil;
import org.junit.jupiter.api.Test;

class BackpropNeuronTest {

   private static final AtomicNeuronFactory NEURON_FACTORY =
         new BackpropNeuronFactory(new LogisticFunction(), 0.34);

   @Test
   void testLearningSpeed() {
      TestUtil.compareDoubleArrays(new double[]{0.1829, 0.0395, 0.0087, 0.0019},
            FeedforwardNeuronTestUtil.getNeuronLearningSpeed(NEURON_FACTORY));
   }

   @Test
   void testLearningSpeed2() {
      TestUtil.compareDoubleArrays(new double[]{0.1829, 0.0117, 0.0, 0.0},
            FeedforwardNeuronTestUtil.getNeuronLearningSpeed(
                  new BackpropNeuronFactory(new LogisticFunction(), 5.34)));
   }
}
