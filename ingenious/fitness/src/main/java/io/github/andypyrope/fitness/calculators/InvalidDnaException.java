package io.github.andypyrope.fitness.calculators;

public class InvalidDnaException extends RuntimeException {
   private static final long serialVersionUID = 6867939348471798899L;

   public InvalidDnaException() {
      super();
   }

   public InvalidDnaException(final String message) {
      super(message);
   }

}
