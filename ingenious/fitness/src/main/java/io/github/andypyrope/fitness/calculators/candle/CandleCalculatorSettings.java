package io.github.andypyrope.fitness.calculators.candle;

import io.github.andypyrope.fitness.calculators.CalculatorSettings;
import io.github.andypyrope.platform.settings.Setting;
import io.github.andypyrope.platform.settings.numeric.IntSetting;
import io.github.andypyrope.platform.settings.numeric.StandardIntSetting;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

/**
 * A bean that contains the manually entered constraints/settings required by {@link
 * CandleCalculator}. Contains reasonable default values that can always be changed if
 * necessary.
 */
public class CandleCalculatorSettings implements CalculatorSettings {

   private static final int DEFAULT_MIN_INPUT_CANDLES = 3;
   private static final int DEFAULT_MAX_INPUT_CANDLES = 25;

   private static final int DEFAULT_MIN_HIDDEN_LAYERS = 0;
   private static final int DEFAULT_MAX_HIDDEN_LAYERS = 10;

   private static final int DEFAULT_MIN_HIDDEN_SIZE = 1;
   private static final int DEFAULT_MAX_HIDDEN_SIZE = 25;

   private static final double DEFAULT_MAX_VOLATILITY = 100.0;

   private static final int DEFAULT_MIN_PASSES_PER_INPUT = 1;
   private static final int DEFAULT_MAX_PASSES_PER_INPUT = 100;

   private static final int DEFAULT_PASSES_WHEN_GETTING_FITNESS = 1000;

   private static final int DEFAULT_OUTPUT_CANDLE_COUNT = 7;
   private static final int DEFAULT_OUTPUT_CANDLE_OFFSET = 0;

   private static final int DEFAULT_CANDLE_DISTANCE = 1;
   private static final TemporalUnit DEFAULT_CANDLE_DISTANCE_UNIT = ChronoUnit.DAYS;


   private int _minInputCandles = DEFAULT_MIN_INPUT_CANDLES;
   private int _maxInputCandles = DEFAULT_MAX_INPUT_CANDLES;

   private int _minHiddenLayers = DEFAULT_MIN_HIDDEN_LAYERS;
   private int _maxHiddenLayers = DEFAULT_MAX_HIDDEN_LAYERS;

   private int _minHiddenSize = DEFAULT_MIN_HIDDEN_SIZE;
   private int _maxHiddenSize = DEFAULT_MAX_HIDDEN_SIZE;

   private double _maxVolatility = DEFAULT_MAX_VOLATILITY;

   private int _minPassesPerInput = DEFAULT_MIN_PASSES_PER_INPUT;
   private int _maxPassesPerInput = DEFAULT_MAX_PASSES_PER_INPUT;

   private int _passesWhenGettingFitness = DEFAULT_PASSES_WHEN_GETTING_FITNESS;

   private IntSetting _outputCandleCount = new StandardIntSetting(
         "Output candle count", "output-candle-count",
         1, DEFAULT_OUTPUT_CANDLE_COUNT, 20);
   private int _outputCandleOffset = DEFAULT_OUTPUT_CANDLE_OFFSET;

   private long _candleDistance = DEFAULT_CANDLE_DISTANCE;
   private TemporalUnit _candleDistanceUnit = DEFAULT_CANDLE_DISTANCE_UNIT;

   @Override
   public Setting[] getSettings() {
      return new Setting[]{_outputCandleCount};
   }

   /**
    * @return The minimum number of candles an organism can desire
    */
   int getMinInputCandles() {
      return _minInputCandles;
   }

   /**
    * @param minInputCandles The minimum number of candles an organism can desire
    */
   public void setMinInputCandles(int minInputCandles) {
      _minInputCandles = minInputCandles;
   }

   /**
    * @return The maximum number of candles an organism can desire
    */
   int getMaxInputCandles() {
      return _maxInputCandles;
   }

   /**
    * @param maxInputCandles The maximum number of candles an organism can desire
    */
   public void setMaxInputCandles(int maxInputCandles) {
      _maxInputCandles = maxInputCandles;
   }

   /**
    * @return The minimum number of hidden layers in the neural network
    */
   int getMinHiddenLayers() {
      return _minHiddenLayers;
   }

   /**
    * @param minHiddenLayers The minimum number of hidden layers in the neural network
    */
   public void setMinHiddenLayers(int minHiddenLayers) {
      _minHiddenLayers = minHiddenLayers;
   }

   /**
    * @return The maximum number of hidden layers in the neural network
    */
   int getMaxHiddenLayers() {
      return _maxHiddenLayers;
   }

   /**
    * @param maxHiddenLayers The maximum number of hidden layers in the neural network
    */
   public void setMaxHiddenLayers(int maxHiddenLayers) {
      _maxHiddenLayers = maxHiddenLayers;
   }

   /**
    * @return The minimum number of neurons in a single hidden layer
    */
   int getMinHiddenSize() {
      return _minHiddenSize;
   }

   /**
    * @param minHiddenSize The minimum number of neurons in a single hidden layer in the
    *                      neural network
    */
   public void setMinHiddenSize(int minHiddenSize) {
      _minHiddenSize = minHiddenSize;
   }

   /**
    * @return The maximum number of neurons in a single hidden layer
    */
   int getMaxHiddenSize() {
      return _maxHiddenSize;
   }

   /**
    * @param maxHiddenSize The maximum number of neurons in a single hidden layer in the
    *                      neural network
    */
   public void setMaxHiddenSize(int maxHiddenSize) {
      _maxHiddenSize = maxHiddenSize;
   }

   /**
    * @return The maximum volatility of the neural network of a calculator
    */
   double getMaxVolatility() {
      return _maxVolatility;
   }

   /**
    * @param maxVolatility The maximum volatility of the neural network of a calculator
    */
   public void setMaxVolatility(double maxVolatility) {
      _maxVolatility = maxVolatility;
   }

   /**
    * @return The minimum number of passes a calculator can make with the same
    *       input-output pair
    */
   int getMinPassesPerInput() {
      return _minPassesPerInput;
   }

   /**
    * @param minPassesPerInput The minimum number of passes a calculator can make with the
    *                          same input-output pair
    */
   public void setMinPassesPerInput(int minPassesPerInput) {
      _minPassesPerInput = minPassesPerInput;
   }

   /**
    * @return The maximum number of passes a calculator can make with the same
    *       input-output pair
    */
   int getMaxPassesPerInput() {
      return _maxPassesPerInput;
   }

   /**
    * @param maxPassesPerInput The maximum number of passes a calculator can make with the
    *                          same input-output pair
    */
   public void setMaxPassesPerInput(int maxPassesPerInput) {
      _maxPassesPerInput = maxPassesPerInput;
   }

   /**
    * @return The number of passes to make while calculating the fitness of an organism
    */
   int getPassesWhenGettingFitness() {
      return _passesWhenGettingFitness;
   }

   /**
    * @param passesWhenGettingFitness The number of passes to make while calculating the
    *                                 fitness of an organism. More means more accurate but
    *                                 also slower.
    */
   public void setPassesWhenGettingFitness(int passesWhenGettingFitness) {
      _passesWhenGettingFitness = passesWhenGettingFitness;
   }

   /**
    * @return The number of output candles
    */
   int getOutputCandleCount() {
      return _outputCandleCount.getValue();
   }

   /**
    * @return The number of indices after which the output candles should start
    */
   int getOutputCandleOffset() {
      return _outputCandleOffset;
   }

   /**
    * @param outputCandleOffset The number of indices after which the output candles
    *                           should start (0 by default - directly after the last input
    *                           candle)
    */
   public void setOutputCandleOffset(int outputCandleOffset) {
      _outputCandleOffset = outputCandleOffset;
   }

   /**
    * @return The distance between two candles.
    */
   long getCandleDistance() {
      return _candleDistance;
   }

   /**
    * @param candleDistance The distance between two candles. If the candles are not with
    *                       such a distance, new ones are created to fill the gaps and
    *                       ones that are too dense are omitted.
    */
   public void setCandleDistance(long candleDistance) {
      _candleDistance = candleDistance;
   }

   /**
    * @return The unit in which the distance between the candles is
    */
   TemporalUnit getCandleDistanceUnit() {
      return _candleDistanceUnit;
   }

   /**
    * @param candleDistanceUnit The unit in which the distance between the candles is
    */
   public void setCandleDistanceUnit(TemporalUnit candleDistanceUnit) {
      _candleDistanceUnit = candleDistanceUnit;
   }

   long getMaxNeuralNetworkEdges() {
      final long inputToFirstHidden = _maxInputCandles * _maxHiddenSize;
      final long intraHiddenLayer = (_maxHiddenLayers - 1) * _maxHiddenSize *
            _maxHiddenSize;
      final long lastHiddenToOutput = _maxHiddenSize * _outputCandleCount.getValue();

      return inputToFirstHidden + intraHiddenLayer + lastHiddenToOutput;
   }
}
