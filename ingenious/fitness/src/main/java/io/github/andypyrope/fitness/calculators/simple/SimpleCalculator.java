package io.github.andypyrope.fitness.calculators.simple;

import io.github.andypyrope.fitness.calculators.Calculator;
import io.github.andypyrope.fitness.calculators.InvalidDnaException;
import io.github.andypyrope.platform.dna.Dna;

class SimpleCalculator implements Calculator {

   private Long _persistedFitness = null;
   private final Dna _dna;

   SimpleCalculator(Dna dna) {
      if (dna == null) {
         throw new InvalidDnaException(
            "Cannot instantiate SimpleCalculator with null DNA");
      }
      _dna = dna;
   }

   @Override
   public long getFitness() {
      if (_persistedFitness != null) {
         return _persistedFitness;
      }
      long result = 0;
      final int dnaSize = _dna.size();

      for (int i = 0; i < dnaSize; i++) {
         result += getNumberOfOnes(_dna.read());
      }

      _persistedFitness = result;
      return result;
   }

   private int getNumberOfOnes(int value) {
      int result = 0;
      for (int i = 0; i < Integer.BYTES; i++) {
         result += value & 1;
         value >>= 1;
      }
      return result;
   }

   @Override
   public long getStudyingComplexity() {
      return 0;
   }

   @Override
   public void study(int iterationCount) {
      // No implementation
   }

   @Override
   public boolean canStudy() {
      return false;
   }

}
