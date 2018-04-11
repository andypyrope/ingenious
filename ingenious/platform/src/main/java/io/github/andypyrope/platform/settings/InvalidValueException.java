package io.github.andypyrope.platform.settings;

/**
 * An exception thrown upon an attempt to set an invalid value for a setting.
 */
public class InvalidValueException extends RuntimeException {
   private static final long serialVersionUID = 8953810126049430835L;

   /**
    * @param id          The ID of the setting.
    * @param targetValue The value that is invalid.
    */
   public InvalidValueException(final String id, final String targetValue) {
      super(String.format("Could not set the value of setting '%s' to '%s'",
            id, targetValue));
   }

   /**
    * @param id          The ID of the setting.
    * @param targetValue The value that is invalid.
    */
   public InvalidValueException(final String id, final int targetValue) {
      super(String.format("Could not set the value of setting '%s' to '%d'",
            id, targetValue));
   }

   /**
    * @param id          The ID of the setting.
    * @param targetValue The value that is invalid.
    */
   public InvalidValueException(final String id, final double targetValue) {
      super(String.format("Could not set the value of setting '%s' to '%f'",
            id, targetValue));
   }
}
