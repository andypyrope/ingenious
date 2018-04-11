package io.github.andypyrope.platform.settings;

/**
 * An exception thrown when the default value of a setting is invalid.
 */
public class InvalidDefaultValueException extends RuntimeException {
   private static final long serialVersionUID = -2196304013843570960L;

   /**
    * @param id           The ID of the setting.
    * @param defaultValue The default value that is invalid.
    */
   public InvalidDefaultValueException(final String id, final String defaultValue) {
      super(String.format("The default value of '%s' cannot be '%s'",
            id, defaultValue));
   }

   /**
    * @param id           The ID of the setting.
    * @param defaultValue The default value that is invalid.
    */
   public InvalidDefaultValueException(final String id, final int defaultValue) {
      super(String.format("The default value of '%s' cannot be '%d'",
            id, defaultValue));
   }

   /**
    * @param id           The ID of the setting.
    * @param defaultValue The default value that is invalid.
    */
   public InvalidDefaultValueException(final String id, final double defaultValue) {
      super(String.format("The default value of '%s' cannot be '%f'",
            id, defaultValue));
   }
}
