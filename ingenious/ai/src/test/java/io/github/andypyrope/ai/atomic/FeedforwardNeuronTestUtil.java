package io.github.andypyrope.ai.atomic;

import io.github.andypyrope.ai.atomic.neurons.AtomicNeuron;
import io.github.andypyrope.ai.atomic.neurons.AtomicNeuronFactory;
import org.easymock.EasyMock;

/**
 * Utility class for {@link AtomicNeuron} tests.
 */
public class FeedforwardNeuronTestUtil {

   public static double[] getNeuronLearningSpeed(AtomicNeuronFactory factory) {
      final int passes = 3;
      final int iterationsPerPass = 10;

      final double[] result = new double[passes + 1];

      final AtomicNeuron[][] neurons = factory
            .makeAllNeurons(1, new int[]{1, 2, 3, 2, 1}, 1);
      final AtomicNeuron outputNeuron = neurons[neurons.length - 1][0];

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

   private static void makePass(AtomicNeuron[][] layers, double[] input) {
      for (int i = 0; i < layers.length; i++) {
         for (int j = 0; j < layers[i].length; j++) {
            layers[i][j].setNetInput(i == 0 ? input[j] : 0.0);
         }
      }

      for (final AtomicNeuron[] layer : layers) {
         for (AtomicNeuron neuron : layer) {
            neuron.propagate();
         }
      }
   }

   private static void adjust(AtomicNeuron[][] layers, double[] output) {
      for (int i = 0; i < layers[layers.length - 1].length; i++) {
         layers[layers.length - 1][i].adjust(output[i]);
      }

      for (int i = layers.length - 2; i >= 0; i--) {
         for (AtomicNeuron neuron : layers[i]) {
            neuron.adjust();
         }
      }
   }

   static AtomicNeuronFactory makeFactoryMock(AtomicNeuron[][] layers) {

      final int[] hiddenSizes = new int[layers.length - 2];
      for (int i = 1; i < layers.length - 1; i++) {
         hiddenSizes[i - 1] = layers[i].length;
      }

      final AtomicNeuronFactory result =
            EasyMock.createMock(AtomicNeuronFactory.class);

      EasyMock.expect(result.makeAllNeurons(EasyMock.eq(layers[0].length),
            EasyMock.aryEq(hiddenSizes), EasyMock.eq(layers[layers.length - 1].length))
      ).andReturn(layers);

      EasyMock.replay(result);
      return result;
   }
}
