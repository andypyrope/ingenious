package io.github.andypyrope.fitness.ai.feedforward.neurons;

import io.github.andypyrope.fitness.ai.activation.LogisticFunction;
import io.github.andypyrope.fitness.ai.feedforward.FeedforwardNeuronTestUtil;
import io.github.andypyrope.fitness.testutil.TestUtil;
import org.junit.jupiter.api.Test;

class BackpropFeedforwardNeuronTest {

   private static final FeedforwardNeuronFactory NEURON_FACTORY =
         new BackpropFeedforwardNeuronFactory(new LogisticFunction(), 0.34);

   @Test
   void testLearningSpeed() {
      TestUtil.compareDoubleArrays(new double[]{0.563, 0.4969, 0.431, 0.3705},
            FeedforwardNeuronTestUtil.getNeuronLearningSpeed(NEURON_FACTORY));
   }

   @Test
   void testLearningSpeed2() {
      TestUtil.compareDoubleArrays(new double[]{0.563, 0.0752, 0.026, 0.0113},
            FeedforwardNeuronTestUtil.getNeuronLearningSpeed(
                  new BackpropFeedforwardNeuronFactory(new LogisticFunction(), 5.34)));
   }
}
