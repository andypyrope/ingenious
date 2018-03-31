package io.github.andypyrope.fitness.ai;

/**
 * Used for neurons to know how to behave. Neurons of different types behave too
 * similarly so creating separate classes is unnecessary.
 */
enum FeedforwardNeuronType {
   INPUT,
   HIDDEN,
   OUTPUT;
}
