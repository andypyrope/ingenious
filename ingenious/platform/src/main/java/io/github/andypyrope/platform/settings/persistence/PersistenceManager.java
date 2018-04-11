package io.github.andypyrope.platform.settings.persistence;

/**
 * Saves/loads key-value pairs.
 */
public interface PersistenceManager {

   /**
    * Save the value of a setting.
    *
    * @param id    The ID of the setting.
    * @param value The value of the setting. If `null`, then the value is cleared.
    */
   void setValue(final String id, final String value);

   /**
    * Load the value of a setting.
    *
    * @param id  The ID of the setting.
    * @param def The default value of the setting.
    * @return The value of the setting. Null if not defined.
    */
   String getValue(final String id, final String def);
}
