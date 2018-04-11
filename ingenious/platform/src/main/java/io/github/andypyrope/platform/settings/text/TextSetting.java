package io.github.andypyrope.platform.settings.text;

import io.github.andypyrope.platform.settings.Setting;

/**
 * A simple text value (string).
 */
interface TextSetting extends Setting {

   /**
    * @return The current value of the setting.
    */
   String getValue();

   /**
    * Update the value of this setting
    *
    * @param value The target value
    */
   void setValue(String value);
}
