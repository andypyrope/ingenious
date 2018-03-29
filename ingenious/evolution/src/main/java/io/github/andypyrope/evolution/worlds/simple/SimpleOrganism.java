package io.github.andypyrope.evolution.worlds.simple;

import io.github.andypyrope.fitness.calculators.Calculator;
import io.github.andypyrope.fitness.calculators.CalculatorProvider;
import io.github.andypyrope.platform.dna.ByteDna;

class SimpleOrganism {

   private static final int DEFAULT_DNA_SIZE = 50;
   private static final double INITIAL_DNA_MUTATION = 0.5;
   private static final double DEFAULT_DNA_MUTATION = 0.05;

   private final ByteDna _dna;
   private final Calculator _calculator;

   SimpleOrganism(CalculatorProvider provider) {
      _dna = new ByteDna(DEFAULT_DNA_SIZE);
      _dna.mutate(INITIAL_DNA_MUTATION);
      _calculator = provider.provide(_dna);
   }

   private SimpleOrganism(ByteDna dna, CalculatorProvider provider) {
      _dna = dna;
      _dna.mutate(DEFAULT_DNA_MUTATION);
      _calculator = provider.provide(_dna);
   }

   public SimpleOrganism copulate(SimpleOrganism other,
      CalculatorProvider provider) {

      return new SimpleOrganism(_dna.copulate(other._dna), provider);
   }

   public long getFitness() {
      return _calculator.getFitness();
   }

}
