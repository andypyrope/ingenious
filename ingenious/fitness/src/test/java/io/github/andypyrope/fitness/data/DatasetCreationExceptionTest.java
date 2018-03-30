package io.github.andypyrope.fitness.data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DatasetCreationExceptionTest {

   @Test
   void test() {
      final String message = "some message";
      assertNull(new DatasetCreationException().getMessage());
      assertEquals(message, new DatasetCreationException(message).getMessage());

      final Exception cause = new Exception();
      final DatasetCreationException datasetCreationException = new DatasetCreationException(
         message,
         cause);
      assertEquals(message, datasetCreationException.getMessage());
      assertEquals(cause, datasetCreationException.getCause());
   }

}
