package io.github.andypyrope.fitness.ai.feedforward;

import io.github.andypyrope.fitness.ai.feedforward.neurons.FeedforwardNeuron;
import io.github.andypyrope.fitness.ai.feedforward.neurons.FeedforwardNeuronFactory;
import io.github.andypyrope.fitness.testutil.TestUtil;
import org.easymock.EasyMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

class StandardFeedforwardNeuralNetworkTest {

   private static final int INPUT_SIZE = 2;
   private static final int[] HIDDEN_LAYER_SIZES = new int[]{3, 1};
   private static final int OUTPUT_SIZE = 2;

   private FeedforwardNeuronFactory _neuronFactory;
   private FeedforwardNeuron[][] _layers;
   private FeedforwardNeuron[] _inputNeurons;
   private FeedforwardNeuron[] _outputNeurons;

   @BeforeEach
   void setUp() {
      _layers = makeNeuronMocks(INPUT_SIZE, HIDDEN_LAYER_SIZES[0], HIDDEN_LAYER_SIZES[1],
            OUTPUT_SIZE);
      _inputNeurons = _layers[0];
      _outputNeurons = _layers[_layers.length - 1];

      _neuronFactory = FeedforwardNeuronTestUtil.makeFactoryMock(_layers);
   }

   private FeedforwardNeuron[][] makeNeuronMocks(final int... sizes) {
      final FeedforwardNeuron[][] result = new FeedforwardNeuron[sizes.length][];
      for (int i = 0; i < sizes.length; i++) {
         result[i] = new FeedforwardNeuron[sizes[i]];
         for (int j = 0; j < sizes[i]; j++) {
            result[i][j] = EasyMock.createMock(FeedforwardNeuron.class);
         }
      }
      return result;
   }

   @AfterEach
   void tearDown() {
      EasyMock.verify(_neuronFactory);
      forAllNeurons(EasyMock::verify);
   }

   @Test
   void testGetEdgeCount() {
      Assertions.assertEquals(2 * 3 + 3 + 2, makeNetwork().getEdgeCount());
   }

   @Test
   void testCalculateAndAdjust() {
      final double[] input = new double[]{0.24, 0.523};
      final double[] targetOutput = new double[]{0.4, 0.5};

      forAllNeurons(neuron -> {
         neuron.propagate();
         EasyMock.expectLastCall();
      });

      for (int i = 0; i < _inputNeurons.length; i++) {
         _inputNeurons[i].setNetInput(input[i]);
         EasyMock.expectLastCall();

         _inputNeurons[i].adjust();
         EasyMock.expectLastCall();
      }

      for (int i = 1; i < _layers.length - 1; i++) {
         for (final FeedforwardNeuron neuron : _layers[i]) {
            neuron.setNetInput(0.0);
            EasyMock.expectLastCall();

            neuron.adjust();
            EasyMock.expectLastCall();
         }
      }

      for (int i = 0; i < _outputNeurons.length; i++) {
         _outputNeurons[i].setNetInput(0.0);
         EasyMock.expectLastCall();

         // Learning
         _outputNeurons[i].adjust(targetOutput[i]);
         EasyMock.expectLastCall();
      }

      final FeedforwardNeuralNetwork network = makeNetwork();
      network.calculate(input);
      network.adjust(targetOutput);
   }

   @Test
   void testGetOutput() {
      final double[] mockedOutput = new double[]{0.1, 0.2};
      for (int i = 0; i < _outputNeurons.length; i++) {
         EasyMock.expect(_outputNeurons[i].getOutput()).andReturn(mockedOutput[i]);
      }
      final FeedforwardNeuralNetwork network = makeNetwork();
      final double[] actualOutput = network.getOutput();
      TestUtil.compareDoubleArrays(mockedOutput, actualOutput);
   }

   @Test
   void testGetEuclideanDistance() {
      final double[] mockedOutput = new double[]{0.1, 0.2};
      final double[] targetOutput = new double[]{0.4, 0.6};

      for (int i = 0; i < _outputNeurons.length; i++) {
         EasyMock.expect(_outputNeurons[i].getOutput()).andReturn(mockedOutput[i]);
      }

      final FeedforwardNeuralNetwork network = makeNetwork();
      TestUtil.compareDoubles(0.5, network.getEuclideanDistance(targetOutput));
   }

   private FeedforwardNeuralNetwork makeNetwork() {
      forAllNeurons(EasyMock::replay);
      return new StandardFeedforwardNeuralNetwork(INPUT_SIZE, HIDDEN_LAYER_SIZES,
            OUTPUT_SIZE, _neuronFactory);
   }

   private void forAllNeurons(final Consumer<FeedforwardNeuron> consumer) {
      for (final FeedforwardNeuron[] layer : _layers) {
         for (final FeedforwardNeuron neuron : layer) {
            consumer.accept(neuron);
         }
      }
   }
}
