package io.github.andypyrope.fitness.calculators.simple;

import io.github.andypyrope.fitness.calculators.Calculator;
import io.github.andypyrope.fitness.calculators.CalculatorProvider;
import io.github.andypyrope.platform.dna.Dna;

public class SimpleCalculatorProvider implements CalculatorProvider {
   public Calculator provide(Dna dna) {
      return new SimpleCalculator(dna);
   }

   public int getDesiredDnaLength() {
      return 50;
   }
}
