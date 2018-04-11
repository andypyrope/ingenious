package io.github.andypyrope.fitness.calculators.candle;

import io.github.andypyrope.fitness.ai.FeedforwardNeuralNetwork;
import io.github.andypyrope.fitness.ai.activation.LogisticFunction;
import io.github.andypyrope.fitness.calculators.Calculator;
import io.github.andypyrope.fitness.calculators.InvalidDnaException;
import io.github.andypyrope.fitness.data.candle.Candle;
import io.github.andypyrope.platform.dna.Dna;

import java.util.concurrent.ThreadLocalRandom;

class CandleCalculator implements Calculator {

   private final double[] _datasetMaximums;
   /**
    * Normalized (temporally) candles
    */
   private final Candle[][] _candles;
   private final int[] _samplesPerDataset;
   private final int _outputCandleCount;
   private final int _outputCandleOffset;

   // Settings from DNA
   private final int _inputCandleCount;
   private final int _passesPerInput;

   private final int _passesWhenGettingFitness;

   // State/Cache fields
   private final int _studyingComplexity;
   private boolean _hasCachedFitness;
   private double _cachedFitness;
   private Candle[] _dataset;
   private double[] _inputsAtPosition;
   private double[] _expectedOutputAtPosition;
   private int _datasetIndex = -1;
   private int _position = -1;
   private int _pass = -1;

   private final FeedforwardNeuralNetwork _network;

   CandleCalculator(Dna dna, Candle[][] normalizedCandles,
      CandleCalculatorSettings settings) {
      if (dna == null) {
         throw new InvalidDnaException(
            "Cannot instantiate SimpleCalculator with null DNA");
      }
      // Settings passed in constructor
      _candles = normalizedCandles;
      _outputCandleCount = settings.getOutputCandleCount();
      _outputCandleOffset = settings.getOutputCandleOffset();

      _passesWhenGettingFitness = settings.getPassesWhenGettingFitness();

      _inputCandleCount = dna.read(settings.getMinInputCandles(),
         settings.getMaxInputCandles() + 1);
      _passesPerInput = dna.read(settings.getMinPassesPerInput(),
         settings.getMaxPassesPerInput() + 1);
      final int hiddenLayerCount = dna.read(settings.getMinHiddenLayers(),
            settings.getMaxHiddenLayers() + 1);

      final int[] hiddenLayers = new int[hiddenLayerCount];
      for (int i = 0; i < hiddenLayerCount; i++) {
         hiddenLayers[i] = dna.read(settings.getMinHiddenSize(),
            settings.getMaxHiddenSize() + 1);
      }

      _network = new FeedforwardNeuralNetwork(
         _inputCandleCount,
         hiddenLayers,
         _outputCandleCount,
         new LogisticFunction());

      _studyingComplexity = _network.getEdgeCount();

      _datasetMaximums = new double[_candles.length];
      int maxDatasetSize = 0;
      for (int i = 0; i < _candles.length; i++) {
         final Candle[] current = _candles[i];
         maxDatasetSize = Math.max(maxDatasetSize, current.length);
         for (final Candle aCurrent : current) {
            _datasetMaximums[i] = Math.max(_datasetMaximums[i],
                  aCurrent.getClosingPrice());
         }
      }
      int actualTotalSamples = 0;
      _samplesPerDataset = new int[_candles.length];
      for (int i = 0; i < _samplesPerDataset.length; i++) {
         _samplesPerDataset[i] = (_candles[i].length *
               settings.getPassesWhenGettingFitness()) / maxDatasetSize;
         actualTotalSamples += _samplesPerDataset[i];
      }
      _samplesPerDataset[0] += settings.getPassesWhenGettingFitness() -
            actualTotalSamples;
   }

   @Override
   public double getFitness() {
      if (_hasCachedFitness) {
         return _cachedFitness;
      }

      double result = 0.0;
      for (int i = 0; i < _candles.length; i++) {
         final int from = _inputCandleCount - 1;
         final int to = _candles[i].length - _outputCandleCount -
               _outputCandleOffset - 1;
         for (int j = 0; j < _samplesPerDataset[i]; j++) {
            final int index = ThreadLocalRandom.current().nextInt(from, to);
            _network.calculate(getInputArrayAtIndex(i, index));
            result += _network
                  .getEuclideanDistance(getOutputArrayAtIndex(i, index));
         }
      }

      _hasCachedFitness = true;
      final double average = result / _passesWhenGettingFitness;
      _cachedFitness = 1.0 / average;
      return _cachedFitness;
   }

   @Override
   public long getStudyingComplexity() {
      return _studyingComplexity;
   }

   @Override
   public void study() {
      _pass++;
      if (_pass == _passesPerInput || _inputsAtPosition == null) {
         updatePositionInDataset();
         _pass = 0;
      }

      _network.calculate(_inputsAtPosition);
      _network.adjust(_expectedOutputAtPosition);
      _hasCachedFitness = false;
   }

   private void updatePositionInDataset() {
      _position++;
      if (_dataset == null || _position + _outputCandleOffset +
            _outputCandleCount >= _dataset.length) {

         getNextDataset();
         _position = _inputCandleCount - 1;
      }

      _inputsAtPosition = getInputArrayAtIndex(_datasetIndex, _position);
      _expectedOutputAtPosition = getOutputArrayAtIndex(_datasetIndex,
         _position);
   }

   private double[] getInputArrayAtIndex(int datasetIndex, int index) {
      final double[] result = new double[_inputCandleCount];
      for (int i = 0; i < _inputCandleCount; i++) {
         result[i] = getNormalizedPrice(datasetIndex,
            index - _inputCandleCount + i + 1);
         result[i] /= _datasetMaximums[datasetIndex];
      }
      return result;
   }

   private double[] getOutputArrayAtIndex(int datasetIndex, int index) {
      final double[] result = new double[_outputCandleCount];
      for (int i = 0; i < _outputCandleCount; i++) {
         result[i] = getNormalizedPrice(datasetIndex,
            index + _outputCandleOffset + i + 1);
         result[i] -= getNormalizedPrice(datasetIndex, index);
         result[i] = (result[i] + 1.0) / 2;
      }
      return result;
   }

   private double getNormalizedPrice(int datasetIndex, int index) {
      return _candles[datasetIndex][index].getClosingPrice() /
            _datasetMaximums[datasetIndex];
   }

   private void getNextDataset() {
      _datasetIndex++;
      if (_datasetIndex == _candles.length) {
         _datasetIndex = 0;
      }
      _dataset = _candles[_datasetIndex];
   }

   @Override
   public boolean canStudy() {
      return true;
   }
}
