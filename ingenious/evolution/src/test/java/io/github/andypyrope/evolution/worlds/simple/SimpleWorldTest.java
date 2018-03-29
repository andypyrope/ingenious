package io.github.andypyrope.evolution.worlds.simple;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.easymock.EasyMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.andypyrope.evolution.worlds.World;
import io.github.andypyrope.fitness.calculators.Calculator;
import io.github.andypyrope.fitness.calculators.CalculatorProvider;
import io.github.andypyrope.platform.dna.Dna;

class SimpleWorldTest {

   private static final long DEFAULT_FITNESS = 5;
   private static final int DEFAULT_SIZE = 10;
   private static final double DEFAULT_COPULATION_RATIO = 0.2;

   private final Calculator _calculatorMock = EasyMock
            .createNiceMock(Calculator.class);
   private final CalculatorProvider _providerMock = EasyMock
            .createNiceMock(CalculatorProvider.class);

   @BeforeEach
   void setUp() throws Exception {
      EasyMock.expect(_calculatorMock.getFitness()).andReturn(DEFAULT_FITNESS)
               .atLeastOnce();
      EasyMock.replay(_calculatorMock);

      EasyMock.expect(_providerMock.provide(EasyMock.anyObject(Dna.class)))
               .andReturn(_calculatorMock).atLeastOnce();
      EasyMock.replay(_providerMock);
   }

   @AfterEach
   void tearDown() throws Exception {
      EasyMock.verify(_calculatorMock);
      EasyMock.verify(_providerMock);
   }

   @Test
   void testGenerationAndSize() {
      final World world = makeSimpleWorld(DEFAULT_SIZE);

      assertEquals(0, world.getGeneration());
      world.iterate();
      assertEquals(1, world.getGeneration());
      world.iterate(5);
      assertEquals(6, world.getGeneration());
      assertEquals(DEFAULT_SIZE, world.size());
   }

   @Test
   void testFitness() {
      final World world = makeSimpleWorld(DEFAULT_SIZE);
      assertEquals(DEFAULT_FITNESS, world.getMinFitness());
      assertEquals(DEFAULT_FITNESS, world.getMeanFitness());
      assertEquals(DEFAULT_FITNESS, world.getMedianFitness());
      assertEquals(DEFAULT_FITNESS, world.getMaxFitness());
   }

   @Test
   void testOddSizeMedianFitness() {
      final World world = makeSimpleWorld(11);
      world.iterate();
      assertEquals(DEFAULT_FITNESS, world.getMedianFitness());
   }

   private SimpleWorld makeSimpleWorld(int size) {
      return new SimpleWorld(
         new SimpleWorldSettings(size, DEFAULT_COPULATION_RATIO),
         _providerMock);
   }
}
