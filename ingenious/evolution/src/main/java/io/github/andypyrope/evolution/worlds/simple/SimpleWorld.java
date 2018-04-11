package io.github.andypyrope.evolution.worlds.simple;

import io.github.andypyrope.evolution.worlds.World;
import io.github.andypyrope.fitness.calculators.CalculatorProvider;

import java.util.ArrayList;
import java.util.List;

public class SimpleWorld implements World {

   private final List<SimpleOrganism> _organisms;
   private final CalculatorProvider _provider;
   private final SimpleWorldSettings _settings;

   private double _minFitness;
   private double _maxFitness;
   private double _medianFitness;

   private int _generation = 0;

   public SimpleWorld(SimpleWorldSettings settings,
         CalculatorProvider provider) {
      _settings = settings;
      _organisms = new ArrayList<>(_settings.getSize());
      _provider = provider;

      for (int i = 0; i < _settings.getSize(); i++) {
         final SimpleOrganism organism = new SimpleOrganism(_provider);
         organism.studyIfPossible(_settings.getAllowedComplexityPerOrganism());
         _organisms.add(organism);
      }
      sortOrganisms();
      updateStats();
   }

   public int size() {
      return _organisms.size();
   }

   public int getGeneration() {
      return _generation;
   }

   public void iterate() {
      final int organismsToReproduce = (int) (_organisms.size() *
            _settings.getCopulationRatio());

      for (int i = 0; i < organismsToReproduce; i++) {
         for (int j = i + 1; j < organismsToReproduce; j++) {
            final SimpleOrganism organism = new SimpleOrganism(_organisms.get(i),
                  _organisms.get(j));
            organism.studyIfPossible(_settings.getAllowedComplexityPerOrganism());
            _organisms.add(organism);
         }
      }
      sortOrganisms();
      while (_organisms.size() > _settings.getSize()) {
         _organisms.remove(_organisms.size() - 1);
      }
      updateStats();

      _generation++;
   }

   @Override
   public double getMedianFitness() {
      return _medianFitness;
   }

   @Override
   public double getMinFitness() {
      return _minFitness;
   }

   @Override
   public void iterate(int times) {
      for (int i = 0; i < times; i++) {
         iterate();
      }
   }

   @Override
   public double getMeanFitness() {
      double totalFitness = 0;
      for (SimpleOrganism organism : _organisms) {
         totalFitness += organism.getFitness();
      }
      return totalFitness / _organisms.size();
   }

   @Override
   public double getMaxFitness() {
      return _maxFitness;
   }

   @Override
   public String getInfo() {
      return String.format("Gen %02d. Fitness: %3.1f %3.1f %3.1f. Population: %d",
            _generation, _minFitness, _medianFitness, _maxFitness, size());
   }

   /**
    * Sort them in reverse order (the ones with the highest fitness first)
    */
   private void sortOrganisms() {
      _organisms.sort((organism1, organism2) -> Double.compare(organism2.getFitness(),
            organism1.getFitness()));
   }

   /**
    * Update the max, min, median fitness, etc., assuming they are sorted.
    */
   private void updateStats() {
      _minFitness = _organisms.get(_organisms.size() - 1).getFitness();
      _maxFitness = _organisms.get(0).getFitness();

      final int middleIndex = _organisms.size() / 2;
      _medianFitness = _organisms.get(middleIndex).getFitness();

      if (_organisms.size() % 2 == 0) {
         _medianFitness =
               (_medianFitness + _organisms.get(middleIndex + 1).getFitness()) / 2;
      }
   }
}
