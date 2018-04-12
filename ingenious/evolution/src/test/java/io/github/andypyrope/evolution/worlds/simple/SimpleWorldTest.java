package io.github.andypyrope.evolution.worlds.simple;

import io.github.andypyrope.evolution.testutil.TestUtil;
import io.github.andypyrope.evolution.worlds.World;
import io.github.andypyrope.fitness.calculators.Calculator;
import io.github.andypyrope.fitness.calculators.CalculatorProvider;
import io.github.andypyrope.platform.dna.Dna;
import org.easymock.EasyMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleWorldTest {

   private static final double DEFAULT_FITNESS = 5.35;
   private static final int DEFAULT_SIZE = 10;
   private static final double DEFAULT_COPULATION_RATIO = 0.2;

   private final Calculator _calculatorMock = EasyMock
         .createMock(Calculator.class);
   private final CalculatorProvider _providerMock = EasyMock
         .createMock(CalculatorProvider.class);

   @BeforeEach
   void setUp() {
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
   void tearDown() {
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
      TestUtil.compareDoubles(DEFAULT_FITNESS, world.getMinFitness());
      TestUtil.compareDoubles(DEFAULT_FITNESS, world.getMeanFitness());
      TestUtil.compareDoubles(DEFAULT_FITNESS, world.getMedianFitness());
      TestUtil.compareDoubles(DEFAULT_FITNESS, world.getMaxFitness());
   }

   @Test
   void testOddSizeMedianFitness() {
      final World world = makeSimpleWorld(11);
      world.iterate();
      assertEquals(DEFAULT_FITNESS, world.getMedianFitness());
   }

   private World makeSimpleWorld(int size) {
      return new SimpleWorld(
            new SimpleWorldSettings(size, DEFAULT_COPULATION_RATIO, 1000L),
            _providerMock);
   }
}
