package io.github.andypyrope.ai.activation;

import io.github.andypyrope.ai.testutil.TestUtil;
import org.junit.jupiter.api.Test;

class LogisticFunctionTest {

   @Test
   void test() {
      final ActivationFunction function = new LogisticFunction();
      ActivationTestUtil.testSlope(function, 0.25);

      TestUtil.compareDoublesLoose(0.000045, function.getOutput(-10.0));
      TestUtil.compareDoubles(0.5, function.getOutput(0.0));
      TestUtil.compareDoublesLoose(0.99995, function.getOutput(10.0));

      TestUtil.compareDoubles(0.25, function.getSlope(0.0, function.getOutput(0.0)));
   }
}
