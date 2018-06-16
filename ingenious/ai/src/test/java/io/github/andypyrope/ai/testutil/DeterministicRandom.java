package io.github.andypyrope.ai.testutil;

import java.util.Random;

public class DeterministicRandom extends Random {
   private static final long serialVersionUID = 6919083519591098930L;

   private static double[] COUNTER_INCREMENT = new double[]{
         0.1, 23.0, 0.2, 15, 0.3, 2, 0.4, 42, 0.5, 53, 0.6, 12, 0.7, 5, 0.8, 0.9
   };
   private int _incrementIndex = 1;
   private double _counter = COUNTER_INCREMENT[0];

   @Override
   public double nextDouble() {
      _counter += COUNTER_INCREMENT[_incrementIndex] + Math.sin(_counter);
      _incrementIndex = (_incrementIndex + 1) % COUNTER_INCREMENT.length;
      return _counter % 1.0;
   }
}
