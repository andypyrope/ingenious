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
   private final CalculatorProvider _provider;

   SimpleOrganism(CalculatorProvider provider) {
      _dna = new ByteDna(DEFAULT_DNA_SIZE);
      _dna.mutate(INITIAL_DNA_MUTATION);
      _provider = provider;
      _calculator = _provider.provide(_dna);
   }

   SimpleOrganism(SimpleOrganism _parent1, SimpleOrganism _parent2) {
      _dna = new ByteDna(_parent1._dna, _parent2._dna);
      _dna.mutate(DEFAULT_DNA_MUTATION);
      _provider = _parent1._provider;
      _calculator = _provider.provide(_dna);
   }

   public long getFitness() {
      return _calculator.getFitness();
   }
}
