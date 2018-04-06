package io.github.andypyrope.app.tui;

import io.github.andypyrope.evolution.worlds.World;
import org.easymock.EasyMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConsoleUiTest {

   private static final int DEFAULT_SIZE = 50;
   private static final double DEFAULT_MIN_FITNESS = 10;
   private static final double DEFAULT_MEAN_FITNESS = 12.4;
   private static final double DEFAULT_MEDIAN_FITNESS = 12.5688888;
   private static final double DEFAULT_MAX_FITNESS = 91;
   private static final int DEFAULT_GENERATION = 1325;
   private static final String DEFAULT_LINE = "Gen 1325. Fitness: 10.0 12.4 12.6 91.0. Population: 50";
   private static final String INPUT_LINE = " >> ";
   private static final String EXIT_LINE = "Bye!";
   private final ByteArrayOutputStream _outStream = new ByteArrayOutputStream();
   private int _iterateArgument = 1;

   @AfterAll
   static void tearDownAfterClass() {
      System.setIn(System.in);
      System.setOut(System.out);
      System.setErr(System.err);
   }

   @BeforeEach
   void setUp() {
      System.setOut(new PrintStream(_outStream));
   }

   @Test
   void testExit() {
      mockInput("exit");
      makeInteractor().launch();
      verifyOutput(DEFAULT_LINE, INPUT_LINE + EXIT_LINE);
   }

   @Test
   void testIterateOnce() {
      mockInput("iterate", "exit");
      makeInteractor().launch();
      verifyOutput(DEFAULT_LINE,
         INPUT_LINE + DEFAULT_LINE,
         INPUT_LINE + EXIT_LINE);
   }

   @Test
   void testIterateFiveTimes() {
      _iterateArgument = 5;
      mockInput("iterate 5", "exit");
      makeInteractor().launch();
      verifyOutput(DEFAULT_LINE,
         INPUT_LINE + DEFAULT_LINE,
         INPUT_LINE + EXIT_LINE);
   }

   @Test
   void testIncorrectCommand() {
      mockInput("sdgasdgkpasdg", "exit");
      makeInteractor().launch();
      verifyOutput(DEFAULT_LINE,
         INPUT_LINE + "Incorrect command",
         INPUT_LINE + EXIT_LINE);
   }

   @Test
   void testHelp() {
      mockInput("help", "exit");
      makeInteractor().launch();
      verifyOutput(DEFAULT_LINE,
         INPUT_LINE + "Sorry bruh",
         INPUT_LINE + EXIT_LINE);
   }

   private void mockInput(String... lines) {
      System.setIn(new ByteArrayInputStream(
         Arrays.stream(lines).collect(Collectors.joining(String.format("%n")))
               .getBytes()));
   }

   private void verifyOutput(String... lines) {
      assertEquals(
         Arrays.stream(lines).collect(Collectors.joining(String.format("%n"))),
         _outStream.toString().trim());
   }

   private ConsoleUi makeInteractor() {
      final World world = EasyMock.createNiceMock(World.class);
      EasyMock.expect(world.size()).andReturn(DEFAULT_SIZE).anyTimes();
      EasyMock.expect(world.getMinFitness()).andReturn(DEFAULT_MIN_FITNESS)
            .anyTimes();
      EasyMock.expect(world.getMeanFitness()).andReturn(DEFAULT_MEAN_FITNESS)
            .anyTimes();
      EasyMock.expect(world.getMedianFitness())
            .andReturn(DEFAULT_MEDIAN_FITNESS).anyTimes();
      EasyMock.expect(world.getMaxFitness()).andReturn(DEFAULT_MAX_FITNESS)
            .anyTimes();
      EasyMock.expect(world.getGeneration()).andReturn(DEFAULT_GENERATION)
            .anyTimes();

      world.iterate(_iterateArgument);
      EasyMock.expectLastCall();

      EasyMock.replay(world);

      return new ConsoleUi(world, new Scanner(System.in));
   }

}
