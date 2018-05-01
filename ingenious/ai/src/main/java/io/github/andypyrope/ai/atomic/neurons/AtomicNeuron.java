package io.github.andypyrope.ai.atomic.neurons;

public interface AtomicNeuron {

   /**
    * Update the net input of the neuron.
    *
    * @param netInput The target net input.
    */
   void setNetInput(final double netInput);

   /**
    * @return The output of this neuron
    */
   double getOutput();

   /**
    * Set the output to a value based on the input, activation function, and bias. Then
    * feed the output of this neuron into the inputs of the next neurons, based on the
    * edge weights between it and them.
    */
   void propagate();

   /**
    * Update the gradients. Then change the weights/bias/etc. of the neuron in order for
    * the neural network it's a part of to show better results.
    */
   void adjust();

   /**
    * Update the gradients based on its desired output. Then change the bias/etc. of the
    * neuron in order for the neural network it's a part of to show better results.
    *
    * @param targetOutput The target output of this neuron.
    */
   void adjust(double targetOutput);

   /**
    * Adds a certain amount to the net input of this neuron.
    *
    * @param amount The amount to add.
    */
   void addInto(double amount);

   /**
    * @return The current gradient of the input of this neuron.
    */
   double getInputGradient();
}