package io.github.andypyrope.fitness.ai;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import io.github.andypyrope.fitness.ai.activation.LogisticFunction;

class BackpropFeedforwardNeuronTest {

   @Test
   void test() {
      final FeedforwardNeuron neuron = new BackpropFeedforwardNeuron(
         new BackpropFeedforwardNeuron[0],
         new LogisticFunction(),
         35.12);

      assertEquals(0.0, neuron.getOutput());
   }

}
