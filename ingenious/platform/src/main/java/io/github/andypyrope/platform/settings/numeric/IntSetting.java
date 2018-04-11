package io.github.andypyrope.platform.settings.numeric;

import io.github.andypyrope.platform.settings.InvalidValueException;
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
    * @throws InvalidValueException If the value is not within the constraints.
    */
   void setValue(int value) throws InvalidValueException;
}
