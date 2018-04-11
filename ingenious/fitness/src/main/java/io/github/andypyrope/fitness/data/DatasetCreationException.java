package io.github.andypyrope.fitness.data;

public class DatasetCreationException extends RuntimeException {
   private static final long serialVersionUID = 2713447823726914113L;

   public DatasetCreationException(String message) {
      super(message);
   }
   
   public DatasetCreationException(String message, Exception cause) {
      super(message, cause);
   }

}
