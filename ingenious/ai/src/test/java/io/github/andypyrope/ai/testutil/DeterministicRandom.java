package io.github.andypyrope.ai.testutil;

import java.util.Random;

public class DeterministicRandom extends Random {
   private static final long serialVersionUID = 6919083519591098930L;

   private double _counter = 0;

   @Override
   public double nextDouble() {
      _counter += 0.1 + Math.sin(_counter);
      return _counter % 1.0;
   }
}
