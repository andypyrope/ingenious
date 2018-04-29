package io.github.andypyrope.ai.activation;

import io.github.andypyrope.ai.testutil.TestUtil;
import org.junit.jupiter.api.Test;

class LeakyReLuFunctionTest {

   private static final double SLOPE = 0.1;

   @Test
   void test() {
      final ActivationFunction function = new LeakyReLuFunction(SLOPE);

      TestUtil.compareDoubles(4.8, function.getOutput(4.8));
      TestUtil.compareDoubles(-142.0 * SLOPE, function.getOutput(-142.0));

      TestUtil.compareDoubles(1.0, function.getSlope(2.0, function.getOutput(2.0)));
      TestUtil.compareDoubles(SLOPE, function.getSlope(-1.0, function.getOutput(-1.0)));
   }

}
