package io.github.andypyrope.ai.atomic.neurons;

import io.github.andypyrope.ai.activation.LogisticFunction;
import io.github.andypyrope.ai.atomic.FeedforwardNeuronTestUtil;
import io.github.andypyrope.ai.testutil.TestUtil;
import org.junit.jupiter.api.Test;

class BackpropNeuronTest {

   @Test
   void testLearningSpeed() {
      TestUtil.compareDoubleArrays(new double[]{0.1212, 0.0295, 0.0073, 0.0018},
            FeedforwardNeuronTestUtil.getNeuronLearningSpeed(
                  new BackpropNeuronFactory(new LogisticFunction(), 0.34)));
   }

   @Test
   void testLearningSpeed2() {
      TestUtil.compareDoubleArrays(new double[]{0.1212, 0.0438, 0.0046, 0.0005},
            FeedforwardNeuronTestUtil.getNeuronLearningSpeed(
                  new BackpropNeuronFactory(new LogisticFunction(), 5.34)));
   }
}
