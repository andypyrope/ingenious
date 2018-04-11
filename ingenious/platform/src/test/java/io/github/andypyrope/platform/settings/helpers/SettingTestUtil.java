package io.github.andypyrope.platform.settings.helpers;

import io.github.andypyrope.platform.settings.Setting;
import io.github.andypyrope.platform.settings.persistence.PersistenceManager;
import org.easymock.EasyMock;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SettingTestUtil {

   public static void verifyException(Runnable runnable,
         Class<? extends Exception> expected) {

      Class<? extends Exception> actualClass = null;
      try {
         runnable.run();
      } catch (Exception e) {
         actualClass = e.getClass();
      }
      assertEquals(expected, actualClass);
   }

   public static void loadAndSave(Setting setting, String valueToLoad) {
      loadAndSave(setting, valueToLoad, valueToLoad);
   }


   public static void loadAndSave(Setting setting, String valueToLoad,
         String expectedSavedValue) {

      final PersistenceManager manager = EasyMock.createMock(PersistenceManager.class);
      EasyMock.expect(manager.getValue(setting.getId(), null)).andReturn(valueToLoad);
      manager.setValue(setting.getId(), expectedSavedValue);
      EasyMock.expectLastCall();
      EasyMock.replay(manager);

      setting.load(manager);
      setting.save(manager, false);
      EasyMock.verify(manager);
   }
}
