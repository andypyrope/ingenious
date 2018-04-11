package io.github.andypyrope.platform.settings.multi;

import io.github.andypyrope.platform.settings.InvalidDefaultValueException;
import io.github.andypyrope.platform.settings.InvalidValueException;
import io.github.andypyrope.platform.settings.helpers.SettingTestUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StandardMultiSettingTest {

   private static final String DEF_LABEL = "label";
   private static final String DEF_ID = "id";
   private static final SomeOption[] DEF_OPTIONS =
         new SomeOption[]{SomeOption.OPTION_A, SomeOption.OPTION_B};
   private static final int DEF_DEFAULT_SELECTION = 1;

   @Test
   void testInvalidDefaultValue() {
      SettingTestUtil.verifyException(() -> makeSetting(DEF_OPTIONS, 2),
            InvalidDefaultValueException.class);

      SettingTestUtil.verifyException(() -> makeSetting(DEF_OPTIONS, -1),
            InvalidDefaultValueException.class);
   }

   @Test
   void testPersistence() {
      final Option[] options = new Option[]{SomeOption.OPTION_A, SomeOption.OPTION_B,
            SomeOption.OPTION_B, SomeOption.OPTION_A};
      final MultiSetting setting = makeSetting(options, DEF_DEFAULT_SELECTION);
      assertEquals(DEF_DEFAULT_SELECTION, setting.getSelectedIndex());

      SettingTestUtil.loadAndSave(setting, "-1", "0");
      SettingTestUtil.loadAndSave(setting, "24", String.valueOf(options.length - 1));
      SettingTestUtil.loadAndSave(setting, "1");
      SettingTestUtil.loadAndSave(setting, "2asd4", "1");
   }

   @Test
   void testIsDefault() {
      final MultiSetting setting = makeSetting();
      assertTrue(setting.isDefault());

      setting.select(0);
      assertFalse(setting.isDefault());

      setting.select(DEF_DEFAULT_SELECTION);
      assertTrue(setting.isDefault());
   }

   @Test
   void testResetToDefault() {
      final MultiSetting setting = makeSetting();
      setting.select(0);
      assertEquals(0, setting.getSelectedIndex());

      setting.resetToDefault();
      assertEquals(DEF_DEFAULT_SELECTION, setting.getSelectedIndex());
   }

   @Test
   void testGetOptions() {
      assertSame(DEF_OPTIONS, makeSetting().getOptions());
   }

   @Test
   void testSelect() {
      final MultiSetting setting = makeSetting();
      assertEquals(1, setting.getSelectedIndex());
      setting.select(0);
      assertEquals(0, setting.getSelectedIndex());

      SettingTestUtil.verifyException(() -> setting.select(-1),
            InvalidValueException.class);
      SettingTestUtil.verifyException(() -> setting.select(2),
            InvalidValueException.class);
      assertEquals(0, setting.getSelectedIndex());
   }

   @Test
   void testGetSelected() {
      assertEquals(SomeOption.OPTION_B, makeSetting().getSelected());
   }

   @Test
   void testGetSelectedIndex() {
      assertEquals(DEF_DEFAULT_SELECTION, makeSetting().getSelectedIndex());
   }

   private MultiSetting makeSetting() {
      return makeSetting(DEF_OPTIONS, DEF_DEFAULT_SELECTION);
   }

   private MultiSetting makeSetting(Option[] options, int defaultSelection) {
      return new StandardMultiSetting(DEF_LABEL, DEF_ID, options, defaultSelection);
   }

   private enum SomeOption implements Option {
      OPTION_A, OPTION_B;

      @Override
      public String getLabel() {
         return null;
      }

      @Override
      public String getId() {
         return null;
      }
   }
}