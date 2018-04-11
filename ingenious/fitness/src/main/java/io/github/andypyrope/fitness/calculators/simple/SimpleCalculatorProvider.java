package io.github.andypyrope.fitness.calculators.simple;

import io.github.andypyrope.fitness.calculators.Calculator;
import io.github.andypyrope.fitness.calculators.CalculatorProvider;
import io.github.andypyrope.platform.dna.Dna;

public class SimpleCalculatorProvider implements CalculatorProvider {

   private static final int DESIRED_DNA_LENGTH = 50;

   @Override
   public Calculator provide(Dna dna) {
      return new SimpleCalculator(dna);
   }

   @Override
   public int getDesiredDnaLength() {
      return DESIRED_DNA_LENGTH;
   }

   @Override
   public long getMaxStudyingComplexity() {
      return 0;
   }
}
