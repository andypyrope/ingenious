package io.github.andypyrope.platform.settings.multi;

/**
 * A single option in the {@link MultiSetting} setting.
 */
public interface Option {

   /**
    * @return The label this option should be displayed to the user with.
    */
   String getLabel();

   /**
    * @return A unique string representation of this option.
    */
   String getId();
}
