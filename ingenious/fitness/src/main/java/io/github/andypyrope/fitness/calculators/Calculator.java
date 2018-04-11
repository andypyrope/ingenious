package io.github.andypyrope.fitness.calculators;

public interface Calculator {
   /**
    * Run the fitness calculator. If a fitness has already been calculated and
    * cached, it is simply returned.
    * 
    * @return The calculated fitness
    */
   double getFitness();

   /**
    * @return The worst-case complexity of the algorithm that improves the
    *         calculator, measured in number of operations
    */
   long getStudyingComplexity();

   /**
    * Attempt to improve the fitness returned by this calculator. If there is no
    * implementation of this, it does nothing.
    */
   void study();

   /**
    * @return Whether there is an implementation of the {@link #study}
    *         method
    */
   boolean canStudy();
}
