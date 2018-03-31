package io.github.andypyrope.fitness.ai;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import io.github.andypyrope.fitness.ai.activation.ActivationFunction;
import io.github.andypyrope.fitness.ai.activation.LogisticFunction;

class FeedforwardNeuralNetworkTest {

   @Test
   void testCalculation() {
      final FeedforwardNeuralNetwork network = new FeedforwardNeuralNetwork(
         5,
         new int[] { 3, 10, 1 },
         2,
         new LogisticFunction(),
         0.06);

      final double[] input = new double[] { 0.12, 0.23, 0.64, 0.23, 0.1 };
      final double[] output = new double[] { 0.96, 0.11 };

      // The output nodes have an output value of 0.0 in the beginning

      assertEquals(Math.sqrt(output[0] * output[0] + output[1] * output[1]),
         network.getEucliedanDistance(output));

      network.calculate(input);
      final double initialError = network.getEucliedanDistance(output);

      for (int i = 0; i < 10; i++) {
         network.adjust(output);
         network.calculate(input);
      }

      assertTrue(initialError > network.getEucliedanDistance(output));
   }

   @Test
   void testSimple() {
      final FeedforwardNeuralNetwork network = new FeedforwardNeuralNetwork(
         1,
         new int[] {},
         1,
         new LogisticFunction(),
         0.8);

      final double[] input = new double[] { 1.0 };
      final double[] output = new double[] { 0.0 };

      // The output nodes have an output value of 0.0 in the beginning
      assertEquals(0.0, network.getEucliedanDistance(output));

      network.calculate(input);

      for (int i = 0; i < 100; i++) {
         network.adjust(output);
         network.calculate(input);
      }
   }

//   @Test
//   void testReverser() {
//      final FeedforwardNeuralNetwork network = new FeedforwardNeuralNetwork(
//         3,
//         new int[] { 5 },
//         3,
//         new LogisticFunction(),
//         0.5);
//
//      double[] input = new double[0];
//      double[] output = new double[0];
//
//      for (int gen = 0; gen < 50; gen++) {
//         double currentError = 0;
//         final double sampleCount = 10000;
//         for (int i = 0; i < sampleCount; i++) {
//            input = new double[] { Math.random(), Math.random(),
//                     Math.random() };
//            output = new double[] { input[2], input[1], input[0] };
//            network.calculate(input);
//            currentError += network.getEucliedanDistance(output);
//         }
//         System.out.println(" >> " + gen + ": " + currentError / sampleCount);
//         for (int i = 0; i < 1000; i++) {
//            input = new double[] { Math.random(), Math.random(),
//                     Math.random() };
//            output = new double[] { input[2], input[1], input[0] };
//            for (int j = 0; j < 5; j++) {
//               network.calculate(input);
//               network.adjust(output);
//            }
//         }
//      }
//      double currentError = 0;
//      final double sampleCount = 10000;
//      for (int i = 0; i < sampleCount; i++) {
//         input = new double[] { Math.random(), Math.random(), Math.random() };
//         output = new double[] { input[2], input[1], input[0] };
//         network.calculate(input);
//         currentError += network.getEucliedanDistance(output);
//      }
//      System.out.println(" >> " + "FIN" + ": " + currentError / sampleCount);
//      System.out.print("Input: ");
//      for (int i = 0; i < input.length; i++) {
//         System.out.print(String.format("%3.1f, ", input[i] * 100));
//      }
//      System.out.println();
//      System.out.print("Output: ");
//      output = network.getOutput();
//      for (int i = 0; i < output.length; i++) {
//         System.out.print(String.format("%3.1f, ", output[i] * 100));
//      }
//      System.out.println();
//   }

}
