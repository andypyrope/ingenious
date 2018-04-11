package io.github.andypyrope.platform.settings.multi;

import io.github.andypyrope.platform.settings.InvalidDefaultValueException;
import io.github.andypyrope.platform.settings.InvalidValueException;
import io.github.andypyrope.platform.settings.SettingBase;
import io.github.andypyrope.platform.util.LogUtil;

import java.util.logging.Logger;

/**
 * A standard implementation of {@link MultiSetting}.
 */
public class StandardMultiSetting extends SettingBase implements MultiSetting {

   private final static Logger LOG = LogUtil.make(StandardMultiSetting.class);

   private final Option[] _options;
   private final int _defaultSelectedIndex;

   private int _selectedIndex;

   /**
    * @param label                The label of the setting.
    * @param id                   The ID of the setting.
    * @param options              The available options.
    * @param defaultSelectedIndex The index of the option selected by default.
    */
   StandardMultiSetting(final String label, final String id,
         final Option[] options, final int defaultSelectedIndex) {

      super(label, id);

      _options = options;
      _defaultSelectedIndex = defaultSelectedIndex;
      _selectedIndex = _defaultSelectedIndex;

      if (isInvalid(_defaultSelectedIndex)) {
         throw new InvalidDefaultValueException(getId(), _defaultSelectedIndex);
      }
   }

   @Override
   protected String fetchRawValue() {
      return String.valueOf(_selectedIndex);
   }

   @Override
   protected void applyRawValue(final String rawValue) {
      int index;

      try {
         index = Integer.parseInt(rawValue);
      } catch (NumberFormatException e) {
         LOG.warning(String.format("Cannot set multi-setting '%s' to '%s' because it " +
                     "is not a valid integer. Leaving it as '%d' instead.",
               getId(), rawValue, _selectedIndex));
         return;
      }

      if (isInvalid(index)) {
         _selectedIndex = index < 0 ? 0 : _options.length - 1;
         LOG.warning(String.format("Attempted to set the selected index of " +
               "multi-setting '%s' through a raw value of '%s' to '%d'. " +
               "Setting it to '%d' instead.", getId(), rawValue, index, _selectedIndex));
         return;
      }
      _selectedIndex = index;
   }

   @Override
   public boolean isDefault() {
      return _selectedIndex == _defaultSelectedIndex;
   }

   @Override
   public void resetToDefault() {
      _selectedIndex = _defaultSelectedIndex;
   }

   @Override
   public Option[] getOptions() {
      return _options;
   }

   @Override
   public void select(final int index) {
      if (index < 0 || index >= _options.length) {
         throw new InvalidValueException(getId(), index);
      }
      _selectedIndex = index;
   }

   @Override
   public Option getSelected() {
      return _options[_selectedIndex];
   }

   @Override
   public int getSelectedIndex() {
      return _selectedIndex;
   }

   private boolean isInvalid(final int value) {
      return value < 0 || value >= _options.length;
   }
}
