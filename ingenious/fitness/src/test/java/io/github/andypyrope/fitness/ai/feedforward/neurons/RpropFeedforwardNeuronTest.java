package io.github.andypyrope.fitness.ai.feedforward.neurons;

import io.github.andypyrope.fitness.ai.activation.LogisticFunction;
import io.github.andypyrope.fitness.ai.feedforward.FeedforwardNeuronTestUtil;
import io.github.andypyrope.fitness.testutil.TestUtil;
import org.junit.jupiter.api.Test;

class RpropFeedforwardNeuronTest {

   private static final FeedforwardNeuronFactory NEURON_FACTORY =
         new RpropFeedforwardNeuronFactory(new LogisticFunction());

   @Test
   void testLearningSpeed() {
      TestUtil.compareDoubleArrays(new double[]{0.563, 0.2477, 0.1295, 0.1663},
            FeedforwardNeuronTestUtil.getNeuronLearningSpeed(NEURON_FACTORY));
   }
}
