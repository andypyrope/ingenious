package io.github.andypyrope.platform.settings.text;

import io.github.andypyrope.platform.settings.InvalidDefaultValueException;
import io.github.andypyrope.platform.settings.InvalidValueException;
import io.github.andypyrope.platform.settings.SettingBase;
import io.github.andypyrope.platform.util.LogUtil;

import java.util.function.Predicate;
import java.util.logging.Logger;

/**
 * Standard implementation of {@link TextSetting}.
 */
public class StandardTextSetting extends SettingBase implements TextSetting {

   private final static Logger LOG = LogUtil.make(StandardTextSetting.class);

   private final String _defaultValue;
   private final Predicate<String> _filter;
   private String _value;

   /**
    * @param label        The label of the setting.
    * @param id           The ID of the setting.
    * @param defaultValue The default value of the setting.
    * @param filter       A filter which determines if a value is valid for this setting.
    *                     Can be left as `null` if any value is valid (except `null`).
    */
   StandardTextSetting(final String label, final String id,
         final String defaultValue, final Predicate<String> filter) {

      super(label, id);

      _defaultValue = defaultValue;
      _filter = filter;
      _value = _defaultValue != null ? _defaultValue : "";

      if (isInvalid(_defaultValue)) {
         throw new InvalidDefaultValueException(getId(), _defaultValue);
      }
   }

   @Override
   protected String fetchRawValue() {
      return _value;
   }
@Override
   protected void applyRawValue(final String rawValue) {
      if (isInvalid(rawValue)) {
         LOG.warning(String.format("Could not set the current value of text setting " +
               "'%s' through a raw value of '%s' to '%s'. Leaving it as '%s' " +
               "instead.", getId(), rawValue, rawValue, _value));
         return;
      }
      _value = rawValue;
   }

   @Override
   public String getValue() {
      return _value;
   }
@Override
   public void setValue(final String value) {
      if (isInvalid(value)) {
         throw new InvalidValueException(getId(), value);
      }
      _value = value;
   }

   @Override
   public boolean isDefault() {
      return _value.equals(_defaultValue);
   }

   @Override
   public void resetToDefault() {
      _value = _defaultValue;
   }

   private boolean isInvalid(final String value) {
      return value == null || (_filter != null && !_filter.test(value));
   }




}
