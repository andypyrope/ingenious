package io.github.andypyrope.platform.settings.numeric;

import io.github.andypyrope.platform.settings.Setting;

/**
 * An integer setting.
 */
public interface IntSetting extends Setting {

   /**
    * @return The current value.
    */
   int getValue();

   /**
    * @param value The target value.
    */
   void setValue(int value);
}
