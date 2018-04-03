package io.github.andypyrope.evolution.worlds.simple;

public class SimpleWorldSettings {

   private final int _size;
   private final double _copulationRatio;
   private final long _allowedComplexityPerOrganism;

   public SimpleWorldSettings(int size, double copulationRatio,
      long allowedComplexityPerOrganism) {

      _size = size;
      _copulationRatio = copulationRatio;
      _allowedComplexityPerOrganism = allowedComplexityPerOrganism;
   }

   public final long getAllowedComplexityPerOrganism() {
      return _allowedComplexityPerOrganism;
   }

   public final int getSize() {
      return _size;
   }

   public final double getCopulationRatio() {
      return _copulationRatio;
   }
}
