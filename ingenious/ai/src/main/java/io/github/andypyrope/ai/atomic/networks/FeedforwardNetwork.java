package io.github.andypyrope.ai.atomic.networks;

import io.github.andypyrope.ai.NetworkLayer;
import io.github.andypyrope.ai.NoCalculationException;
import io.github.andypyrope.ai.activation.ActivationFunction;
import io.github.andypyrope.ai.atomic.AtomicNetwork;
import io.github.andypyrope.ai.atomic.layers.ActivationLayer;
import io.github.andypyrope.ai.atomic.layers.AtomicLayer;
import io.github.andypyrope.ai.atomic.layers.FullyConnectedLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FeedforwardNetwork implements AtomicNetwork {

   private final List<AtomicLayer> _layers;
   private final AtomicLayer _inputLayer;
   private final AtomicLayer _outputLayer;
   private final int _complexity;

   public FeedforwardNetwork(int inputSize, int[] hidden, int outputSize,
         ActivationFunction function, final Random random) {

      _layers = new ArrayList<>();

      int currentInputSize = inputSize;
      for (final int layerSize : hidden) {
         _layers.add(new FullyConnectedLayer(currentInputSize, layerSize, random));
         _layers.add(new ActivationLayer(layerSize, function, random));

         currentInputSize = layerSize;
      }

      _layers.add(new FullyConnectedLayer(currentInputSize, outputSize, random));
      _layers.add(new ActivationLayer(outputSize, function, random));

      _complexity = getTotalComplexity();

      for (int i = 0; i < _layers.size(); i++) {
         _layers.get(i).setSurroundingLayers(
               i == 0 ? null : _layers.get(i - 1),
               (i == _layers.size() - 1) ? null : _layers.get(i + 1));
      }

      _inputLayer = _layers.get(0);
      _outputLayer = _layers.get(_layers.size() - 1);
   }

   private int getTotalComplexity() {
      int complexity = 0;
      for (final NetworkLayer layer : _layers) {
         complexity += layer.getCalculationComplexity() + layer.getAdjustmentComplexity();
      }
      return complexity;
   }

   @Override
   public int getComplexity() {
      return _complexity;
   }

   @Override
   public void calculate(double[] inputValues) {
      _inputLayer.calculate(inputValues);
      for (int i = 1; i < _layers.size(); i++) {
         _layers.get(i).calculate();
      }
   }

   @Override
   public void adjust(double[] targetOutput) throws NoCalculationException {
      _outputLayer.adjust(targetOutput);
      for (int i = _layers.size() - 2; i >= 0; i--) {
         _layers.get(i).adjust();
      }
   }

   @Override
   public double[] getOutput() throws NoCalculationException {
      return _outputLayer.getOutputAsAtomic();
   }

   @Override
   public double getEuclideanDistance(double[] targetOutput)
         throws NoCalculationException {
      return _outputLayer.getEuclideanDistance(targetOutput);
   }
}
