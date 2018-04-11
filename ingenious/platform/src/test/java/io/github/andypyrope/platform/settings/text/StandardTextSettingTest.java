package io.github.andypyrope.platform.settings.text;

import io.github.andypyrope.platform.settings.InvalidDefaultValueException;
import io.github.andypyrope.platform.settings.InvalidValueException;
import io.github.andypyrope.platform.settings.helpers.SettingTestUtil;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class StandardTextSettingTest {

   private static final String DEF_LABEL = "label";
   private static final String DEF_ID = "id";
   private static final String DEF_DEFAULT_VALUE = "some-value";
   private static final Predicate<String> DEF_FILTER = (value) -> !value.isEmpty();
   private static final Predicate<String> BAD_FILTER = (value) -> false;

   private static final String NEW_VALUE = "new-value";

   @Test
   void testInvalidDefaultValue() {
      SettingTestUtil.verifyException(() -> makeSetting(null, DEF_FILTER),
            InvalidDefaultValueException.class);

      SettingTestUtil.verifyException(() -> makeSetting(DEF_DEFAULT_VALUE, BAD_FILTER),
            InvalidDefaultValueException.class);

      makeSetting(DEF_DEFAULT_VALUE, null);
   }

   @Test
   void testSetValue() {
      final TextSetting setting = makeSetting();

      setting.setValue(NEW_VALUE);
      assertEquals(NEW_VALUE, setting.getValue());
      SettingTestUtil.verifyException(() -> setting.setValue(""),
            InvalidValueException.class);
      SettingTestUtil.verifyException(() -> makeSetting().setValue(null),
            InvalidValueException.class);
   }

   @Test
   void testGetValue() {
      assertEquals(DEF_DEFAULT_VALUE, makeSetting().getValue());
   }

   @Test
   void testPersistence() {
      final TextSetting setting = makeSetting();
      SettingTestUtil.loadAndSave(setting, NEW_VALUE);
      SettingTestUtil.loadAndSave(setting, null, NEW_VALUE);
      SettingTestUtil.loadAndSave(setting, "", NEW_VALUE);
   }

   @Test
   void testIsDefault() {
      final TextSetting setting = makeSetting();
      assertTrue(setting.isDefault());

      setting.setValue(NEW_VALUE);
      assertFalse(setting.isDefault());

      setting.setValue(DEF_DEFAULT_VALUE);
      assertTrue(setting.isDefault());
   }

   @Test
   void testResetToDefault() {
      final TextSetting setting = makeSetting();
      setting.setValue(NEW_VALUE);
      assertEquals(NEW_VALUE, setting.getValue());

      setting.resetToDefault();
      assertEquals(DEF_DEFAULT_VALUE, setting.getValue());
   }

   private TextSetting makeSetting() {
      return makeSetting(DEF_DEFAULT_VALUE, DEF_FILTER);
   }

   private TextSetting makeSetting(String defaultValue, Predicate<String> filter) {
      return new StandardTextSetting(DEF_LABEL, DEF_ID, defaultValue, filter);
   }
}