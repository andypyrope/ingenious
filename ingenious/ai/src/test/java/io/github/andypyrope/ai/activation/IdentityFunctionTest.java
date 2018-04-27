package io.github.andypyrope.ai.activation;

import io.github.andypyrope.ai.testutil.TestUtil;
import org.junit.jupiter.api.Test;

class IdentityFunctionTest {

   @Test
   void test() {
      final ActivationFunction function = new IdentityFunction();

      final int resolution = 100;
      final double from = -10.0;
      final double to = 10.0;
      final double step = (to - from) / resolution;

      double current = from;

      for (int i = 0; i < resolution; i++) {
         final double output = function.getOutput(current);

         TestUtil.compareDoubles(current, output);
         TestUtil.compareDoubles(1.0, function.getSlope(current, output));

         current += step;
      }
   }

}
