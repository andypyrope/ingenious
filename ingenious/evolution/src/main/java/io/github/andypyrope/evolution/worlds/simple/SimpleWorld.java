package io.github.andypyrope.evolution.worlds.simple;

import java.util.ArrayList;
import java.util.List;

import io.github.andypyrope.evolution.worlds.World;
import io.github.andypyrope.fitness.calculators.CalculatorProvider;

public class SimpleWorld implements World {

   private final List<SimpleOrganism> _organisms;
   private final CalculatorProvider _provider;
   private final SimpleWorldSettings _settings;

   private int _currentGeneration = 0;

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
   }

   public int size() {
      return _organisms.size();
   }

   public int getGeneration() {
      return _currentGeneration;
   }

   public void iterate() {
      final int organismsToReproduce = (int) (_organisms.size() *
            _settings.getCopulationRatio());

      for (int i = 0; i < organismsToReproduce; i++) {
         for (int j = i + 1; j < organismsToReproduce; j++) {
            final SimpleOrganism organism = new SimpleOrganism(
               _organisms.get(i),
               _organisms.get(j));
            organism
                  .studyIfPossible(_settings.getAllowedComplexityPerOrganism());
            _organisms.add(organism);
         }
      }
      sortOrganisms();
      while (_organisms.size() > _settings.getSize()) {
         _organisms.remove(_organisms.size() - 1);
      }

      _currentGeneration++;
   }

   /**
    * Sort them in reverse order (the ones with the highest fitness first)
    */
   private void sortOrganisms() {
      _organisms.sort((organism1, organism2) -> {
         if (organism1.getFitness() > organism2.getFitness()) {
            return -1;
         }
         if (organism1.getFitness() < organism2.getFitness()) {
            return 1;
         }
         return 0;
      });
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
   public double getMedianFitness() {
      List<SimpleOrganism> organisms = new ArrayList<>(_organisms.size());
      organisms.addAll(_organisms);
      organisms.sort((organism1, organism2) -> {
         if (organism1.getFitness() > organism2.getFitness()) {
            return 1;
         }
         if (organism1.getFitness() < organism2.getFitness()) {
            return -1;
         }
         return 0;
      });
      final int middleIndex = organisms.size() / 2;
      final double medianFitness = organisms.get(middleIndex).getFitness();

      if (organisms.size() % 2 == 0) {
         return (medianFitness + organisms.get(middleIndex + 1).getFitness()) /
               2.0;
      }
      return medianFitness;
   }

   @Override
   public double getMinFitness() {
      double minFitness = _organisms.get(0).getFitness();
      for (SimpleOrganism organism : _organisms) {
         minFitness = Math.min(minFitness, organism.getFitness());
      }
      return minFitness;
   }

   @Override
   public double getMaxFitness() {
      double maxFitness = _organisms.get(0).getFitness();
      for (SimpleOrganism organism : _organisms) {
         maxFitness = Math.max(maxFitness, organism.getFitness());
      }
      return maxFitness;
   }
}
