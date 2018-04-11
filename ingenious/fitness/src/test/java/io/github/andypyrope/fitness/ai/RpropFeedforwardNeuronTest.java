package io.github.andypyrope.fitness.ai;

import io.github.andypyrope.fitness.ai.activation.LogisticFunction;
import io.github.andypyrope.fitness.testutil.TestUtil;
import org.junit.jupiter.api.Test;

class RpropFeedforwardNeuronTest {

   @Test
   void testCalculateGradientAndAdjust() {
      final FeedforwardNeuron[][] neurons = makeNeurons(
            new int[]{1, 10, 1});

      neurons[0][0].setOutput(0.24);

      final double[] input = new double[]{0.74};
      final double[] output = new double[]{0.14};

      FeedforwardNeuronTestUtil.makePass(neurons, input);
      TestUtil.compareDoubles(0.993307,
            neurons[neurons.length - 1][0].getOutput());

      FeedforwardNeuronTestUtil.adjust(neurons, output);
      FeedforwardNeuronTestUtil.makePass(neurons, input);
      TestUtil.compareDoubles(0.984860,
            neurons[neurons.length - 1][0].getOutput());

      for (int i = 0; i < 30; i++) {
         FeedforwardNeuronTestUtil.adjust(neurons, output);
         FeedforwardNeuronTestUtil.makePass(neurons, input);
      }
      TestUtil.compareDoubles(0.081766,
            neurons[neurons.length - 1][0].getOutput());
   }

   private FeedforwardNeuron[][] makeNeurons(int[] count) {
      final RpropFeedforwardNeuron[][] result = new RpropFeedforwardNeuron[count.length][];

      for (int i = result.length - 1; i >= 0; i--) {
         result[i] = new RpropFeedforwardNeuron[count[i]];
         for (int j = 0; j < count[i]; j++) {
            result[i][j] = new RpropFeedforwardNeuron(
                  i == result.length - 1 ? null : result[i + 1],
                  new LogisticFunction());
         }
      }

      return result;
   }
}
