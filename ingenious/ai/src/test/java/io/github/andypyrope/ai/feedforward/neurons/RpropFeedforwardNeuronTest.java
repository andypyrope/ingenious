package io.github.andypyrope.ai.feedforward.neurons;

import io.github.andypyrope.ai.activation.LogisticFunction;
import io.github.andypyrope.ai.feedforward.FeedforwardNeuronTestUtil;
import io.github.andypyrope.ai.testutil.TestUtil;
import org.junit.jupiter.api.Test;

class RpropFeedforwardNeuronTest {

   private static final FeedforwardNeuronFactory NEURON_FACTORY =
         new RpropFeedforwardNeuronFactory(new LogisticFunction());

   @Test
   void testLearningSpeed() {
      TestUtil.compareDoubleArrays(new double[]{0.1829, 0.0126, 8.0E-4, 1.0E-4},
            FeedforwardNeuronTestUtil.getNeuronLearningSpeed(NEURON_FACTORY));
   }
}
