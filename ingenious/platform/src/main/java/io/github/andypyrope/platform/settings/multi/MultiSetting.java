package io.github.andypyrope.platform.settings.multi;

import io.github.andypyrope.platform.settings.InvalidValueException;
import io.github.andypyrope.platform.settings.Setting;

/**
 * A collection of many text-based options the user can choose from.
 */
public interface MultiSetting extends Setting {

   /**
    * @return The available options.
    */
   Option[] getOptions();

   /**
    * Select a specific option.
    *
    * @param index The index of the option to select.
    * @throws InvalidValueException If the value is not within the constraints.
    */
   void select(int index) throws InvalidValueException;

   /**
    * @return The currently selected option.
    */
   Option getSelected();

   /**
    * @return The index of the currently selected option.
    */
   int getSelectedIndex();
}
