package io.github.andypyrope.platform.settings.numeric;

import io.github.andypyrope.platform.settings.InvalidConstraintException;
import io.github.andypyrope.platform.settings.InvalidDefaultValueException;
import io.github.andypyrope.platform.settings.InvalidValueException;
import io.github.andypyrope.platform.settings.helpers.SettingTestUtil;
import io.github.andypyrope.platform.testutil.TestUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StandardDoubleSettingTest {

   private static final String DEF_LABEL = "label";
   private static final String DEF_ID = "id";
   private static final double DEF_DEFAULT_VALUE = 4.23;
   private static final double DEF_MIN_VALUE = -1;
   private static final double DEF_MAX_VALUE = 120;

   @Test
   void testInvalidDefaultValue() {
      SettingTestUtil.verifyException(() -> makeSetting(3, 2, 5), InvalidDefaultValueException.class);
   }

   @Test
   void testInvalidConstraints() {
      SettingTestUtil.verifyException(() -> makeSetting(3, 4, 1), InvalidConstraintException.class);
   }

   @Test
   void testSetValue() {
      makeSetting().setValue(DEF_MIN_VALUE + 1);

      SettingTestUtil.verifyException(() -> makeSetting().setValue(DEF_MIN_VALUE - 1),
            InvalidValueException.class);
      SettingTestUtil.verifyException(() -> makeSetting().setValue(DEF_MAX_VALUE + 1),
            InvalidValueException.class);
   }

   @Test
   void testGetValue() {
      TestUtil.compareDoubles(DEF_DEFAULT_VALUE, makeSetting().getValue());
   }

   @Test
   void testPersistence() {
      final DoubleSetting setting = makeSetting();

      SettingTestUtil.loadAndSave(setting, String.valueOf(DEF_MIN_VALUE - 1),
            String.valueOf(DEF_MIN_VALUE));

      SettingTestUtil.loadAndSave(setting, String.valueOf(DEF_MAX_VALUE + 1),
            String.valueOf(DEF_MAX_VALUE));

      SettingTestUtil.loadAndSave(setting, String.valueOf(DEF_DEFAULT_VALUE + 1));

      SettingTestUtil.loadAndSave(setting, "33fs4345",
            String.valueOf(DEF_DEFAULT_VALUE + 1));
   }

   @Test
   void testIsDefault() {
      final DoubleSetting setting = makeSetting();
      assertTrue(setting.isDefault());

      setting.setValue(DEF_DEFAULT_VALUE + 1);
      assertFalse(setting.isDefault());

      setting.setValue(DEF_DEFAULT_VALUE);
      assertTrue(setting.isDefault());
   }

   @Test
   void testResetToDefault() {
      final DoubleSetting setting = makeSetting();
      setting.setValue(DEF_DEFAULT_VALUE + 1);
      TestUtil.compareDoubles(DEF_DEFAULT_VALUE + 1, setting.getValue());

      setting.resetToDefault();
      TestUtil.compareDoubles(DEF_DEFAULT_VALUE, setting.getValue());
   }

   private DoubleSetting makeSetting() {
      return makeSetting(DEF_MIN_VALUE, DEF_DEFAULT_VALUE, DEF_MAX_VALUE);
   }

   private DoubleSetting makeSetting(double minValue, double defaultValue,
         double maxValue) {

      return new StandardDoubleSetting(DEF_LABEL, DEF_ID, minValue, defaultValue,
            maxValue);
   }
}