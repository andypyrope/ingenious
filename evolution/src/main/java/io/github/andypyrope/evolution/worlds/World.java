package io.github.andypyrope.evolution.worlds;

public interface World {

   /**
    * @return The number of organisms
    */
   int size();

   /**
    * @return The current generation. Starts with 0.
    */
   int getGeneration();

   /**
    * Cause a generation to pass
    */
   void iterate();

   /**
    * Cause a specified number of generations to pass
    * 
    * @param times The number of generations to pass
    */
   void iterate(int times);

   /**
    * @return The mean fitness (equal to the sum of the fitness of all organisms
    *         divided by their count)
    */
   double getMeanFitness();

   /**
    * @return The median fitness (equal to the fitness of the organism in the
    *         middle once they have all been sorted by their fitness)
    */
   double getMedianFitness();

   /**
    * @return The minimum fitness of an organism in this world
    */
   double getMinFitness();

   /**
    * @return The maximum fitness of an organism in this world
    */
   double getMaxFitness();
}
