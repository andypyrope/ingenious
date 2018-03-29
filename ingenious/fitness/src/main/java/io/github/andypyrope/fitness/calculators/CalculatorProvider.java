package io.github.andypyrope.fitness.calculators;

import io.github.andypyrope.platform.dna.Dna;

public interface CalculatorProvider {
   /**
    * @param dna The DNA the fitness of which should be calculated
    * @return A new instance of {@link Calculator}
    */
   Calculator provide(Dna dna);
}
