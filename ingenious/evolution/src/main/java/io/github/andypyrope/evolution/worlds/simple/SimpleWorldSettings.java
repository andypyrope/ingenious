package io.github.andypyrope.evolution.worlds.simple;

public class SimpleWorldSettings {

   private final int _size;
   private final double _copulationRatio;

   public SimpleWorldSettings(int size, double copulationRatio) {
      _size = size;
      _copulationRatio = copulationRatio;
   }

   public final int getSize() {
      return _size;
   }

   public final double getCopulationRatio() {
      return _copulationRatio;
   }
}
