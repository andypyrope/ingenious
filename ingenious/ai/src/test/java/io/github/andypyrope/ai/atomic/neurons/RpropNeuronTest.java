package io.github.andypyrope.ai.atomic.neurons;

import io.github.andypyrope.ai.activation.LogisticFunction;
import io.github.andypyrope.ai.atomic.FeedforwardNeuronTestUtil;
import io.github.andypyrope.ai.testutil.TestUtil;
import org.junit.jupiter.api.Test;

class RpropNeuronTest {

   private static final AtomicNeuronFactory NEURON_FACTORY =
         new RpropNeuronFactory(new LogisticFunction());

   @Test
   void testLearningSpeed() {
      TestUtil.compareDoubleArrays(new double[]{0.1829, 0.0126, 8.0E-4, 1.0E-4},
            FeedforwardNeuronTestUtil.getNeuronLearningSpeed(NEURON_FACTORY));
   }
}
