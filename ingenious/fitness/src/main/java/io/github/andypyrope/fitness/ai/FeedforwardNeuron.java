package io.github.andypyrope.fitness.ai;

interface FeedforwardNeuron {

   /**
    * Reset the net input of this node back to 0.
    */
   void resetNetInput();

   /**
    * Manually change the output of the neuron. Useful for input nodes in
    * general.
    * 
    * @param output The new output of this neuron
    */
   void setOutput(double output);

   /**
    * @return The output of this neuron
    */
   double getOutput();

   /**
    * Set the output to a value based on the input, activation function, and
    * bias.
    */
   void updateOutput();

   /**
    * Feed the output of this neuron into the inputs of the next neurons, based
    * on the edge weights between it and them.
    */
   void propagate();

   /**
    * (Re-)calculate the gradient-related fields of this neuron.
    */
   void calculateGradient();

   /**
    * Determine the gradient of this neuron based on its desired output.
    * 
    * @param targetOutput The target output of this neuron
    */
   void calculateGradient(double targetOutput);

   /**
    * Change the weights/bias/etc. of the neuron in order for the neural network
    * it's a part of to show better results.
    */
   void adjust();

}