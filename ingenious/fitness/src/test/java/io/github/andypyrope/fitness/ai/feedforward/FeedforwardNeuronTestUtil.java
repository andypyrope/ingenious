package io.github.andypyrope.fitness.ai.feedforward;

import io.github.andypyrope.fitness.ai.feedforward.neurons.FeedforwardNeuron;
import io.github.andypyrope.fitness.ai.feedforward.neurons.FeedforwardNeuronFactory;
import org.easymock.EasyMock;

/**
 * Utility class for {@link FeedforwardNeuron} tests.
 */
public class FeedforwardNeuronTestUtil {

   public static double[] getNeuronLearningSpeed(FeedforwardNeuronFactory factory) {
      final int passes = 3;
      final int iterationsPerPass = 10;

      final double[] result = new double[passes + 1];

      final FeedforwardNeuron[][] neurons = factory
            .makeAllNeurons(1, new int[]{1, 2, 3, 2, 1}, 1);
      final FeedforwardNeuron outputNeuron = neurons[neurons.length - 1][0];

      final double[] input = new double[]{0.74};
      final double[] output = new double[]{0.14};

      makePass(neurons, input);

      result[0] =
            Math.round(Math.abs(output[0] - outputNeuron.getOutput()) * 10000) / 10000.0;

      for (int i = 1; i <= passes; i++) {
         for (int j = 0; j < iterationsPerPass; j++) {
            adjust(neurons, output);
            makePass(neurons, input);
         }
         result[i] = Math.round(Math.abs(output[0] - outputNeuron.getOutput()) * 10000) /
               10000.0;
      }
      return result;
   }

   private static void makePass(FeedforwardNeuron[][] layers, double[] input) {
      for (int i = 0; i < layers.length; i++) {
         for (int j = 0; j < layers[i].length; j++) {
            layers[i][j].setNetInput(i == 0 ? input[j] : 0.0);
         }
      }

      for (final FeedforwardNeuron[] layer : layers) {
         for (FeedforwardNeuron neuron : layer) {
            neuron.propagate();
         }
      }
   }

   private static void adjust(FeedforwardNeuron[][] layers, double[] output) {
      for (int i = 0; i < layers[layers.length - 1].length; i++) {
         layers[layers.length - 1][i].adjust(output[i]);
      }

      for (int i = layers.length - 2; i >= 0; i--) {
         for (FeedforwardNeuron neuron : layers[i]) {
            neuron.adjust();
         }
      }
   }

   static FeedforwardNeuronFactory makeFactoryMock(FeedforwardNeuron[][] layers) {

      final int[] hiddenSizes = new int[layers.length - 2];
      for (int i = 1; i < layers.length - 1; i++) {
         hiddenSizes[i - 1] = layers[i].length;
      }

      final FeedforwardNeuronFactory result =
            EasyMock.createMock(FeedforwardNeuronFactory.class);

      EasyMock.expect(result.makeAllNeurons(EasyMock.eq(layers[0].length),
            EasyMock.aryEq(hiddenSizes), EasyMock.eq(layers[layers.length - 1].length))
      ).andReturn(layers);

      EasyMock.replay(result);
      return result;
   }
}
