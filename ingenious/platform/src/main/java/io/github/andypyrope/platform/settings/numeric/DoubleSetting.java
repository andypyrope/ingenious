package io.github.andypyrope.platform.settings.numeric;

import io.github.andypyrope.platform.settings.InvalidValueException;
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
    * @throws InvalidValueException If the value is not within the constraints.
    */
   void setValue(double value) throws InvalidValueException;
}
