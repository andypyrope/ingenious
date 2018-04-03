package io.github.andypyrope.evolution.worlds.simple;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.easymock.EasyMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.andypyrope.evolution.worlds.World;
import io.github.andypyrope.fitness.calculators.Calculator;
import io.github.andypyrope.fitness.calculators.CalculatorProvider;
import io.github.andypyrope.platform.dna.Dna;

class SimpleWorldTest {

   private static final double DEFAULT_FITNESS = 5.35;
   private static final int DEFAULT_SIZE = 10;
   private static final double DEFAULT_COPULATION_RATIO = 0.2;

   private final Calculator _calculatorMock = EasyMock
         .createMock(Calculator.class);
   private final CalculatorProvider _providerMock = EasyMock
         .createMock(CalculatorProvider.class);

   @BeforeEach
   void setUp() throws Exception {
      EasyMock.expect(_calculatorMock.getFitness()).andReturn(DEFAULT_FITNESS)
            .anyTimes();
      EasyMock.expect(_calculatorMock.getStudyingComplexity()).andReturn(300L)
            .anyTimes();
      EasyMock.expect(_calculatorMock.canStudy()).andReturn(true).anyTimes();

      _calculatorMock.study();
      EasyMock.expectLastCall().atLeastOnce();

      EasyMock.replay(_calculatorMock);

      EasyMock.expect(_providerMock.provide(EasyMock.anyObject(Dna.class)))
            .andReturn(_calculatorMock).atLeastOnce();
      EasyMock.expect(_providerMock.getDesiredDnaLength()).andReturn(50)
            .anyTimes();
      EasyMock.replay(_providerMock);
   }

   @AfterEach
   void tearDown() throws Exception {
      EasyMock.verify(_calculatorMock);
      EasyMock.verify(_providerMock);
   }

   @Test
   void testGenerationAndSize() {
      final SimpleWorld world = makeSimpleWorld(DEFAULT_SIZE);

      assertEquals(0, world.getGeneration());
      world.iterate();
      assertEquals(1, world.getGeneration());
      world.iterate(5);
      assertEquals(6, world.getGeneration());
      assertEquals(DEFAULT_SIZE, world.size());
   }

   @Test
   void testFitness() {
      final SimpleWorld world = makeSimpleWorld(DEFAULT_SIZE);
      compareDoubles(DEFAULT_FITNESS, world.getMinFitness());
      compareDoubles(DEFAULT_FITNESS, world.getMeanFitness());
      compareDoubles(DEFAULT_FITNESS, world.getMedianFitness());
      compareDoubles(DEFAULT_FITNESS, world.getMaxFitness());
   }

   @Test
   void testOddSizeMedianFitness() {
      final SimpleWorld world = makeSimpleWorld(11);
      world.iterate();
      assertEquals(DEFAULT_FITNESS, world.getMedianFitness());
   }

   private void compareDoubles(double a, double b) {
      assertTrue(Math.abs(a - b) < 0.0000001);
   }

   private SimpleWorld makeSimpleWorld(int size) {
      return new SimpleWorld(
         new SimpleWorldSettings(size, DEFAULT_COPULATION_RATIO, 1000L),
         _providerMock);
   }
}
