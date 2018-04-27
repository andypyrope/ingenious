package io.github.andypyrope.ai.activation;

import io.github.andypyrope.ai.testutil.TestUtil;
import org.junit.jupiter.api.Test;

class ReLuFunctionTest {

   @Test
   void test() {
      final ActivationFunction function = new ReLuFunction();

      TestUtil.compareDoubles(4.8, function.getOutput(4.8));
      TestUtil.compareDoubles(0.0, function.getOutput(-142.0));

      TestUtil.compareDoubles(1.0, function.getSlope(2.0, 2.0));
      TestUtil.compareDoubles(0.0, function.getSlope(-1.0, 0.0));
   }

}
