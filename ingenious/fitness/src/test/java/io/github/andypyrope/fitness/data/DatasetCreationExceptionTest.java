package io.github.andypyrope.fitness.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DatasetCreationExceptionTest {

   @Test
   void test() {
      final String message = "some message";
      assertEquals(message, new DatasetCreationException(message).getMessage());

      final Exception cause = new Exception();
      final DatasetCreationException exception = new DatasetCreationException(message,
         cause);
      assertEquals(message, exception.getMessage());
      assertEquals(cause, exception.getCause());
   }

}
