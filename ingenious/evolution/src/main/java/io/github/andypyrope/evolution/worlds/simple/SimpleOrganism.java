package io.github.andypyrope.evolution.worlds.simple;

import io.github.andypyrope.fitness.calculators.Calculator;
import io.github.andypyrope.fitness.calculators.CalculatorProvider;
import io.github.andypyrope.platform.dna.IntDna;

class SimpleOrganism {

   private static final double DEFAULT_DNA_MUTATION = 0.1;

   private final IntDna _dna;
   private final Calculator _calculator;
   private final CalculatorProvider _provider;

   SimpleOrganism(CalculatorProvider provider) {
      _provider = provider;
      _dna = new IntDna(_provider.getDesiredDnaLength());
      _dna.randomize();
      _calculator = _provider.provide(_dna);
   }

   SimpleOrganism(SimpleOrganism _parent1, SimpleOrganism _parent2) {
      _provider = _parent1._provider;
      _dna = new IntDna(_parent1._dna, _parent2._dna);
      _dna.mutate(Math.min(1.0,
         -1 * _parent1.getFitness() + -1 * _parent2.getFitness()));
      _calculator = _provider.provide(_dna);
   }

   void studyIfPossible(long allowedComplexity) {
      if (!_calculator.canStudy()) {
         return;
      }

      long attempts = allowedComplexity / _calculator.getStudyingComplexity();
      for (long i = 0; i < attempts; i++) {
         _calculator.study();
      }
   }

   public double getFitness() {
      return _calculator.getFitness();
   }
}
