package io.github.andypyrope.platform.settings;

import io.github.andypyrope.platform.settings.persistence.PersistenceManager;

/**
 * The base class for a setting.
 */
public abstract class SettingBase implements Setting {

   private final String _label;
   private final String _id;

   /**
    * @param label The label of the setting.
    * @param id    The ID of the setting.
    */
   protected SettingBase(final String label, final String id) {
      _label = label;
      _id = id == null ? label : id;
   }

   @Override
   public String getLabel() {
      return _label;
   }

   @Override
   public String getId() {
      return _id;
   }

   @Override
   public void load(final PersistenceManager manager) {
      final String value = manager.getValue(_id, null);
      if (value != null) {
         applyRawValue(value);
      }
   }

   @Override
   public void save(final PersistenceManager manager, final boolean eraseIfDefault) {
      manager.setValue(_id, (eraseIfDefault && isDefault()) ? null : fetchRawValue());
   }

   protected abstract String fetchRawValue();

   protected abstract void applyRawValue(String rawValue);
}
