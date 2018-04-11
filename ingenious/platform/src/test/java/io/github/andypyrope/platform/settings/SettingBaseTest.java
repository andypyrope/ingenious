package io.github.andypyrope.platform.settings;

import io.github.andypyrope.platform.settings.persistence.PersistenceManager;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SettingBaseTest {

   private static final String SOME_LABEL = "some-label";
   private static final String SOME_ID = "some-id";
   private final PersistenceManager _persistenceManager =
         EasyMock.createMock(PersistenceManager.class);

   @Test
   void testLoadUndefined() {
      EasyMock.expect(_persistenceManager.getValue(SOME_ID, null)).andReturn(null);
      EasyMock.replay(_persistenceManager);

      final SomeSetting setting = new SomeSetting(SOME_LABEL, SOME_ID);
      setting.load(_persistenceManager);
      EasyMock.verify(_persistenceManager);

      assertEquals(SomeSetting.UNSET, setting._appliedRawValue);
   }

   @Test
   void testLoad() {
      final String value = "some-value";
      EasyMock.expect(_persistenceManager.getValue(SOME_ID, null)).andReturn(value);
      EasyMock.replay(_persistenceManager);

      final SomeSetting setting = new SomeSetting(SOME_LABEL, SOME_ID);
      setting.load(_persistenceManager);
      EasyMock.verify(_persistenceManager);

      assertEquals(value, setting._appliedRawValue);
   }

   @Test
   void testSaveDefaultAndErase() {
      _persistenceManager.setValue(SOME_ID, null);
      EasyMock.expectLastCall().once();
      EasyMock.replay(_persistenceManager);

      final SomeSetting setting = new SomeSetting(SOME_LABEL, SOME_ID);
      setting._isDefault = true;
      setting.save(_persistenceManager, true);
      EasyMock.verify(_persistenceManager);
   }

   @Test
   void testSaveDefaultAndNotErase() {
      _persistenceManager.setValue(SOME_ID, SomeSetting.FETCHED_RAW_VALUE);
      EasyMock.expectLastCall().once();
      EasyMock.replay(_persistenceManager);

      final SomeSetting setting = new SomeSetting(SOME_LABEL, SOME_ID);
      setting._isDefault = true;
      setting.save(_persistenceManager, false);
      EasyMock.verify(_persistenceManager);
   }

   @Test
   void testSaveNotDefaultAndErase() {
      _persistenceManager.setValue(SOME_ID, SomeSetting.FETCHED_RAW_VALUE);
      EasyMock.expectLastCall().once();
      EasyMock.replay(_persistenceManager);

      new SomeSetting(SOME_LABEL, SOME_ID).save(_persistenceManager, true);
      EasyMock.verify(_persistenceManager);
   }

   @Test
   void testSaveNotDefaultAndNotErase() {
      _persistenceManager.setValue(SOME_ID, SomeSetting.FETCHED_RAW_VALUE);
      EasyMock.expectLastCall().once();
      EasyMock.replay(_persistenceManager);

      new SomeSetting(SOME_LABEL, SOME_ID).save(_persistenceManager, false);
      EasyMock.verify(_persistenceManager);
   }

   @Test
   void testGetId() {
      assertEquals(SOME_LABEL, new SomeSetting(SOME_LABEL, null).getId());
      assertEquals(SOME_ID, new SomeSetting(SOME_LABEL, SOME_ID).getId());
   }

   @Test
   void testGetLabel() {
      assertNull(new SomeSetting(null, SOME_ID).getLabel());
      assertEquals(SOME_LABEL, new SomeSetting(SOME_LABEL, SOME_ID).getLabel());
   }

   private class SomeSetting extends SettingBase {

      static final String FETCHED_RAW_VALUE = "some-raw-value";
      static final String UNSET = "unset";
      String _appliedRawValue = UNSET;
      boolean _isDefault = false;

      SomeSetting(final String label, final String id) {
         super(label, id);
      }

      @Override
      protected String fetchRawValue() {
         return FETCHED_RAW_VALUE;
      }

      @Override
      protected void applyRawValue(final String rawValue) {
         _appliedRawValue = rawValue;
      }

      @Override
      public boolean isDefault() {
         return _isDefault;
      }

      @Override
      public void resetToDefault() {
      }
   }
}