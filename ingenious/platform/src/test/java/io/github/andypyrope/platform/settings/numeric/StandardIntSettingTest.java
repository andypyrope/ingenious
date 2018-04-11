package io.github.andypyrope.platform.settings.numeric;

import io.github.andypyrope.platform.settings.InvalidConstraintException;
import io.github.andypyrope.platform.settings.InvalidDefaultValueException;
import io.github.andypyrope.platform.settings.InvalidValueException;
import io.github.andypyrope.platform.settings.helpers.SettingTestUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StandardIntSettingTest {

   private static final String DEF_LABEL = "label";
   private static final String DEF_ID = "id";
   private static final int DEF_DEFAULT_VALUE = 4;
   private static final int DEF_MIN_VALUE = -1;
   private static final int DEF_MAX_VALUE = 120;

   @Test
   void testInvalidDefaultValue() {
      SettingTestUtil.verifyException(() -> makeSetting(3, 2, 5),
            InvalidDefaultValueException.class);
   }

   @Test
   void testInvalidConstraints() {
      SettingTestUtil.verifyException(() -> makeSetting(3, 4, 1),
            InvalidConstraintException.class);
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
      assertEquals(DEF_DEFAULT_VALUE, makeSetting().getValue());
   }

   @Test
   void testPersistence() {
      final IntSetting setting = makeSetting();

      SettingTestUtil.loadAndSave(setting, String.valueOf(DEF_MIN_VALUE - 1),
            String.valueOf(DEF_MIN_VALUE));
      SettingTestUtil.loadAndSave(setting, String.valueOf(DEF_MAX_VALUE + 1),
            String.valueOf(DEF_MAX_VALUE));
      SettingTestUtil.loadAndSave(setting, String.valueOf(DEF_DEFAULT_VALUE + 1));
      SettingTestUtil.loadAndSave(setting, "3.5", String.valueOf(DEF_DEFAULT_VALUE + 1));
   }

   @Test
   void testIsDefault() {
      final IntSetting setting = makeSetting();
      assertTrue(setting.isDefault());

      setting.setValue(DEF_DEFAULT_VALUE + 1);
      assertFalse(setting.isDefault());

      setting.setValue(DEF_DEFAULT_VALUE);
      assertTrue(setting.isDefault());
   }

   @Test
   void testResetToDefault() {
      final IntSetting setting = makeSetting();
      setting.setValue(DEF_DEFAULT_VALUE + 1);
      assertEquals(DEF_DEFAULT_VALUE + 1, setting.getValue());

      setting.resetToDefault();
      assertEquals(DEF_DEFAULT_VALUE, setting.getValue());
   }

   private IntSetting makeSetting() {
      return makeSetting(DEF_MIN_VALUE, DEF_DEFAULT_VALUE, DEF_MAX_VALUE);
   }

   private IntSetting makeSetting(int minValue, int defaultValue, int maxValue) {
      return new StandardIntSetting(DEF_LABEL, DEF_ID, minValue, defaultValue, maxValue);
   }
}