package io.github.andypyrope.platform.settings.text;

import io.github.andypyrope.platform.settings.InvalidValueException;
import io.github.andypyrope.platform.settings.Setting;

/**
 * A simple text value (string).
 */
public interface TextSetting extends Setting {

   /**
    * @return The current value of the setting.
    */
   String getValue();

   /**
    * Update the value of this setting
    *
    * @param value The target value
    * @throws InvalidValueException If the value is not within the constraints.
    */
   void setValue(String value) throws InvalidValueException;
}
