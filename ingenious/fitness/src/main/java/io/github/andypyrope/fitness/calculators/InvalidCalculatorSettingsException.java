package io.github.andypyrope.fitness.calculators;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * An exception thrown when some of settings for a calculator provider are invalid.
 */
public class InvalidCalculatorSettingsException extends Exception {
   private static final long serialVersionUID = 7444235009026614634L;

   /**
    * Create a generic instance of {@link InvalidCalculatorSettingsException} about no
    * specific setting or reason.
    */
   public InvalidCalculatorSettingsException() {
      super("A setting is invalid");
   }

   /**
    * The default constructor with a message.
    *
    * @param message The exception message
    */
   public InvalidCalculatorSettingsException(String message) {
      super(message);
   }

   /**
    * Auto-generate a fairly specific message that describes the setting name.
    *
    * @param keys The setting keys
    */
   public InvalidCalculatorSettingsException(final String[] keys) {
      super(keys.length == 1
            ? "The '" + keys[0] + "' setting is invalid"
            : "The following settings are invalid: " +
                  Arrays.stream(keys).collect(Collectors.joining(", ")));
   }
}
