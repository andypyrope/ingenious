package io.github.andypyrope.fitness.ai;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import io.github.andypyrope.fitness.ai.activation.LogisticFunction;

class FeedforwardNeuronTest {

   @Test
   void test() {
      final FeedforwardNeuron neuron = new FeedforwardNeuron(
         new FeedforwardNeuron[0],
         new LogisticFunction());
      
      assertEquals(0.0, neuron.getOutput());
   }

}
