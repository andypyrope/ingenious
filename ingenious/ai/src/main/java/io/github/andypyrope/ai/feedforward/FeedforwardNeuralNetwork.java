package io.github.andypyrope.ai.feedforward;

public interface FeedforwardNeuralNetwork {

   /**
    * @return The total number of edges in this neural network.
    */
   int getEdgeCount();

   /**
    * Finds out what the output of the network is, given a specific input.
    *
    * @param inputValues The input of the network.
    */
   void calculate(double[] inputValues);

   /**
    * Runs an algorithm to improve the weights/biases.
    *
    * @param targetOutput The expected output
    */
   void adjust(double[] targetOutput);

   /**
    * @return The output of this neural network.
    */
   double[] getOutput();

   /**
    * @param targetOutput The expected output.
    * @return The euclidean distance between the actual output and an expected one.
    */
   double getEuclideanDistance(double[] targetOutput);
}
