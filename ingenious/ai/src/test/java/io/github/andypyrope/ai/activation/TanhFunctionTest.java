package io.github.andypyrope.ai.activation;

import io.github.andypyrope.ai.testutil.TestUtil;
import org.junit.jupiter.api.Test;

class TanhFunctionTest {

   @Test
   void test() {
      final ActivationFunction function = new TanhFunction();
      ActivationTestUtil.testSlope(function, 1);

      TestUtil.compareDoubles(-0.999999996, function.getOutput(-10.0));
      TestUtil.compareDoubles(0.0, function.getOutput(0.0));
      TestUtil.compareDoubles(0.999999996, function.getOutput(10.0));

      TestUtil.compareDoubles(1.0, function.getSlope(0.0, function.getOutput(0.0)));
   }
}