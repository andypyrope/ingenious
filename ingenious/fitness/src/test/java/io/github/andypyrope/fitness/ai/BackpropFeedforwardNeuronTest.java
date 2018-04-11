package io.github.andypyrope.fitness.ai;

import io.github.andypyrope.fitness.ai.activation.LogisticFunction;
import io.github.andypyrope.fitness.testutil.TestUtil;
import org.junit.jupiter.api.Test;

class BackpropFeedforwardNeuronTest {

   @Test
   void testCalculateGradientAndAdjust() {
      final FeedforwardNeuron[][] neurons = makeNeurons(
            new int[]{1, 2, 3, 2, 1});

      neurons[0][0].setOutput(0.24);
      neurons[0][0].propagate();

      for (int i = 1; i < neurons.length; i++) {
         for (FeedforwardNeuron neuron : neurons[i]) {
            neuron.updateOutput();
            neuron.propagate();
         }
      }
      TestUtil.compareDoubles(0.859519,
            neurons[neurons.length - 1][0].getOutput());

      FeedforwardNeuronTestUtil.adjust(neurons, new double[]{0.3});
      FeedforwardNeuronTestUtil.makePass(neurons, new double[]{0.24});

      TestUtil.compareDoubles(0.853363,
            neurons[neurons.length - 1][0].getOutput());
   }

   @Test
   void testResetInput() {
      final FeedforwardNeuron[][] neurons = makeNeurons(
            new int[]{1, 1});

      neurons[0][0].setOutput(0.5);
      neurons[0][0].propagate();
      TestUtil.compareDoubles(0.0, neurons[1][0].getOutput());

      neurons[1][0].updateOutput();
      TestUtil.compareDoubles(0.622459, neurons[1][0].getOutput());

      neurons[1][0].updateOutput();
      TestUtil.compareDoubles(0.622459, neurons[1][0].getOutput());

      neurons[1][0].resetNetInput();
      neurons[1][0].updateOutput();
      TestUtil.compareDoubles(0.5, neurons[1][0].getOutput());
   }

   private FeedforwardNeuron[][] makeNeurons(int[] count) {
      final BackpropFeedforwardNeuron[][] result = new BackpropFeedforwardNeuron[count.length][];

      for (int i = result.length - 1; i >= 0; i--) {
         result[i] = new BackpropFeedforwardNeuron[count[i]];
         for (int j = 0; j < count[i]; j++) {
            result[i][j] = new BackpropFeedforwardNeuron(
                  i == result.length - 1 ? null : result[i + 1],
                  new LogisticFunction(),
                  0.34);
         }
      }

      return result;
   }
}
