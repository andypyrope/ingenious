package io.github.andypyrope.platform.settings.numeric;

import io.github.andypyrope.platform.settings.InvalidConstraintException;
import io.github.andypyrope.platform.settings.InvalidDefaultValueException;
import io.github.andypyrope.platform.settings.InvalidValueException;
import io.github.andypyrope.platform.settings.SettingBase;
import io.github.andypyrope.platform.util.LogUtil;

import java.util.logging.Logger;

/**
 * A setting with an integer value. Standard implementation of {@link IntSetting}.
 */
public class StandardIntSetting extends SettingBase implements IntSetting {

   private final static Logger LOG = LogUtil.make(StandardIntSetting.class);

   private final int _defaultValue;
   private final int _minValue;
   private final int _maxValue;

   private int _value;

   /**
    * @param label        The label of the setting.
    * @param id           The ID of the setting.
    * @param minValue     The minimum value (inclusive) of the setting.
    * @param defaultValue The default value of the setting.
    * @param maxValue     The maximum value (inclusive) of the setting.
    */
   StandardIntSetting(final String label, final String id, final int minValue,
         final int defaultValue, final int maxValue) {

      super(label, id);

      _minValue = minValue;
      _defaultValue = defaultValue;
      _maxValue = maxValue;
      _value = _defaultValue;

      if (minValue > maxValue) {
         throw new InvalidConstraintException(String.format(
               "The minimum value '%d' cannot be larger than the maximum value '%d'",
               minValue, maxValue));
      }
      if (isInvalid(_defaultValue)) {
         throw new InvalidDefaultValueException(getId(), _defaultValue);
      }
   }

   @Override
   public int getValue() {
      return _value;
   }

   @Override
   public void setValue(final int value) {
      if (isInvalid(value)) {
         throw new InvalidValueException(getId(), value);
      }
      _value = value;
   }

   @Override
   protected String fetchRawValue() {
      return String.valueOf(_value);
   }

   @Override
   protected void applyRawValue(final String rawValue) {
      int value;

      try {
         value = Integer.parseInt(rawValue);
      } catch (NumberFormatException e) {
         LOG.warning(String.format("Cannot set int setting '%s' to '%s' because it is " +
                     "not a valid integer. Leaving it as '%d' instead.",
               getId(), rawValue, _value));
         return;
      }

      if (value < _minValue) {
         _value = _minValue;
         LOG.warning(String.format("Cannot set int setting '%s' to '%d' because the " +
                     "minimum is '%d'. Setting it to '%d' instead.",
               getId(), value, _minValue, _value));
         return;
      }

      if (value > _maxValue) {
         _value = _maxValue;
         LOG.warning(String.format("Cannot set int setting '%s' to '%d' because the " +
                     "maximum is '%d'. Setting it to '%d' instead.",
               getId(), value, _maxValue, _value));
         return;
      }
      _value = value;
   }

   @Override
   public boolean isDefault() {
      return _value == _defaultValue;
   }

   @Override
   public void resetToDefault() {
      _value = _defaultValue;
   }

   private boolean isInvalid(final int value) {
      return value < _minValue || value > _maxValue;
   }
}
