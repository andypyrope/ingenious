package io.github.andypyrope.platform.settings.numeric;

import io.github.andypyrope.platform.settings.InvalidConstraintException;
import io.github.andypyrope.platform.settings.InvalidDefaultValueException;
import io.github.andypyrope.platform.settings.InvalidValueException;
import io.github.andypyrope.platform.settings.SettingBase;
import io.github.andypyrope.platform.util.LogUtil;

import java.util.logging.Logger;

/**
 * A setting with a non-integer value. Standard implementation of {@link DoubleSetting}.
 */
public class StandardDoubleSetting extends SettingBase implements DoubleSetting {

   private final static Logger LOG = LogUtil.make(StandardDoubleSetting.class);

   private final double _defaultValue;
   private final double _minValue;
   private final double _maxValue;

   private double _value;

   /**
    * @param label        The label of the setting.
    * @param id           The ID of the setting.
    * @param minValue     The minimum value (inclusive) of the setting.
    * @param defaultValue The default value of the setting.
    * @param maxValue     The maximum value (inclusive) of the setting.
    */
   StandardDoubleSetting(final String label, final String id,
         final double minValue, final double defaultValue, final double maxValue) {

      super(label, id);

      _minValue = minValue;
      _defaultValue = defaultValue;
      _maxValue = maxValue;
      _value = _defaultValue;

      if (minValue > maxValue) {
         throw new InvalidConstraintException(String.format(
               "The minimum value '%f' cannot be larger than the maximum value '%f'",
               minValue, maxValue));
      }
      if (isInvalid(_defaultValue)) {
         throw new InvalidDefaultValueException(getId(), _defaultValue);
      }
   }

   @Override
   public double getValue() {
      return _value;
   }

   @Override
   public void setValue(final double value) {
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
      double value;

      try {
         value = Double.parseDouble(rawValue);
      } catch (NumberFormatException e) {
         LOG.warning(String.format("Cannot set double setting '%s' to '%s' because it " +
                     "is not a valid floating point number. Leaving it as '%f' instead.",
               getId(), rawValue, _value));
         return;
      }

      if (value < _minValue) {
         _value = _minValue;
         LOG.warning(String.format("Cannot set double setting '%s' to '%f' because the " +
                     "minimum is '%f'. Setting it to '%f' instead.",
               getId(), value, _minValue, _value));
         return;
      }

      if (value > _maxValue) {
         _value = _maxValue;
         LOG.warning(String.format("Cannot set double setting '%s' to '%f' because the " +
                     "maximum is '%f'. Setting it to '%f' instead.",
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

   private boolean isInvalid(final double value) {
      return value < _minValue || value > _maxValue;
   }
}
