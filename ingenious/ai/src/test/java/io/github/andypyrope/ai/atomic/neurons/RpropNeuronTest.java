package io.github.andypyrope.ai.atomic.neurons;

import io.github.andypyrope.ai.activation.LogisticFunction;
import io.github.andypyrope.ai.atomic.FeedforwardNeuronTestUtil;
import io.github.andypyrope.ai.testutil.TestUtil;
import org.junit.jupiter.api.Test;

class RpropNeuronTest {

   @Test
   void testLearningSpeed() {
      TestUtil.compareDoubleArrays(new double[]{0.1212, 0.0069, 0.0003, 0.0002},
            FeedforwardNeuronTestUtil.getNeuronLearningSpeed(
                  new RpropNeuronFactory(new LogisticFunction())));
   }
}
