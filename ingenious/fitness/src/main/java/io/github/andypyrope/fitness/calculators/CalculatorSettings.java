package io.github.andypyrope.fitness.calculators;

import io.github.andypyrope.platform.settings.Setting;

public interface CalculatorSettings {

   /**
    * @return The settings that can be defined for this calculator provider.
    */
   Setting[] getSettings();
}
