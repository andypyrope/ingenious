package io.github.andypyrope.fitness.calculators;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class InvalidCalculatorSettingsExceptionTest {

   @Test
   void testSimple() {
      assertEquals("A setting is invalid",
         new InvalidCalculatorSettingsException().getMessage());
   }

   @Test
   void testWithMessage() {
      assertEquals("some-message",
         new InvalidCalculatorSettingsException("some-message").getMessage());
   }

   @Test
   void testWithOneKey() {
      assertEquals("The 'setting-1' setting is invalid",
         new InvalidCalculatorSettingsException(new String[] { "setting-1" })
               .getMessage());
   }

   @Test
   void testWithManyKeys() {
      assertEquals("The following settings are invalid: setting-1, setting-2",
         new InvalidCalculatorSettingsException(
            new String[] { "setting-1", "setting-2" }).getMessage());
   }

}
