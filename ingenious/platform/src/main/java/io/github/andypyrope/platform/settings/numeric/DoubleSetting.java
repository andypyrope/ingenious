package io.github.andypyrope.platform.settings.numeric;

import io.github.andypyrope.platform.settings.Setting;

/**
 * A setting with a non-integer value.
 */
public interface DoubleSetting extends Setting {

   /**
    * @return The current value.
    */
   double getValue();

   /**
    * @param value The target value.
    */
   void setValue(double value);
}
