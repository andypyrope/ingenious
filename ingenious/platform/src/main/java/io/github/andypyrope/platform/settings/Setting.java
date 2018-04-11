package io.github.andypyrope.platform.settings;

import io.github.andypyrope.platform.settings.persistence.PersistenceManager;

/**
 * A single user-defined setting for a world/calculator type/etc.
 */
public interface Setting {

   /**
    * @return The instructional label corresponding to this setting
    */
   String getLabel();

   /**
    * @return An identification string of this setting, meant to be used for
    *       saving/loading.
    */
   String getId();

   /**
    * Loads the setting value. Does nothing if no such value has been saved.
    *
    * @param manager             The persistence manager to use.
    */
   void load(PersistenceManager manager);

   /**
    * Saves the setting value so that it can be loaded later.
    *
    * @param manager        The persistence manager to use.
    * @param eraseIfDefault Whether to erase the saved value if the current one is the
    *                       default one.
    */
   void save(PersistenceManager manager, boolean eraseIfDefault);

   /**
    * @return Whether this setting has a value that is the same as its default one.
    */
   boolean isDefault();

   /**
    * Set the value of this setting back to its default state. It is considered the
    * initial one from then on.
    */
   void resetToDefault();
}
