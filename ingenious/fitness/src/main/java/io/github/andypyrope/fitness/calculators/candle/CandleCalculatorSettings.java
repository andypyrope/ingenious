package io.github.andypyrope.fitness.calculators.candle;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

/**
 * A bean that contains the manually entered constraints/settings required by
 * {@link CandleCalculator}. Contains reasonable default values that can always
 * be changed if necessary.
 */
public class CandleCalculatorSettings {

   private int _minInputCandles = 3;
   private int _maxInputCandles = 25;

   private int _minHiddenLayers = 0;
   private int _maxHiddenLayers = 10;

   private int _minHiddenSize = 1;
   private int _maxHiddenSize = 25;

   private double _maxVolatility = 100.0;

   private int _minPassesPerInput = 1;
   private int _maxPassesPerInput = 100;

   private int _passesWhenGettingFitness = 1000;

   private int _outputCandleCount = 7;
   private int _outputCandleOffset = 0;
   private long _candleDistance = 1;
   private TemporalUnit _candleDistanceUnit = ChronoUnit.DAYS;

   /**
    * @return The minimum number of candles an organism can desire
    */
   public int getMinInputCandles() {
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
   public int getMaxInputCandles() {
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
   public int getMinHiddenLayers() {
      return _minHiddenLayers;
   }

   /**
    * @param minHiddenLayers The minimum number of hidden layers in the neural
    *           network
    */
   public void setMinHiddenLayers(int minHiddenLayers) {
      _minHiddenLayers = minHiddenLayers;
   }

   /**
    * @return The maximum number of hidden layers in the neural network
    */
   public int getMaxHiddenLayers() {
      return _maxHiddenLayers;
   }

   /**
    * @param maxHiddenLayers The maximum number of hidden layers in the neural
    *           network
    */
   public void setMaxHiddenLayers(int maxHiddenLayers) {
      _maxHiddenLayers = maxHiddenLayers;
   }

   /**
    * @return The minimum number of neurons in a single hidden layer
    */
   public int getMinHiddenSize() {
      return _minHiddenSize;
   }

   /**
    * @param minHiddenSize The minimum number of neurons in a single hidden
    *           layer in the neural network
    */
   public void setMinHiddenSize(int minHiddenSize) {
      _minHiddenSize = minHiddenSize;
   }

   /**
    * @return The maximum number of neurons in a single hidden layer
    */
   public int getMaxHiddenSize() {
      return _maxHiddenSize;
   }

   /**
    * @param maxHiddenSize The maximum number of neurons in a single hidden
    *           layer in the neural network
    */
   public void setMaxHiddenSize(int maxHiddenSize) {
      _maxHiddenSize = maxHiddenSize;
   }

   /**
    * @return The maximum volatility of the neural network of a calculator
    */
   public double getMaxVolatility() {
      return _maxVolatility;
   }

   /**
    * @param maxVolatility The maximum volatility of the neural network of a
    *           calculator
    */
   public void setMaxVolatility(double maxVolatility) {
      _maxVolatility = maxVolatility;
   }

   /**
    * @return The minimum number of passes a calculator can make with the same
    *         input-output pair
    */
   public int getMinPassesPerInput() {
      return _minPassesPerInput;
   }

   /**
    * @param minPassesPerInput The minimum number of passes a calculator can
    *           make with the same input-output pair
    */
   public void setMinPassesPerInput(int minPassesPerInput) {
      _minPassesPerInput = minPassesPerInput;
   }

   /**
    * @return The maximum number of passes a calculator can make with the same
    *         input-output pair
    */
   public int getMaxPassesPerInput() {
      return _maxPassesPerInput;
   }

   /**
    * @param maxPassesPerInput The maximum number of passes a calculator can
    *           make with the same input-output pair
    */
   public void setMaxPassesPerInput(int maxPassesPerInput) {
      _maxPassesPerInput = maxPassesPerInput;
   }

   /**
    * @return The number of passes to make while calculating the fitness of an
    *         organism
    */
   public int getPassesWhenGettingFitness() {
      return _passesWhenGettingFitness;
   }

   /**
    * @param passesWhenGettingFitness The number of passes to make while
    *           calculating the fitness of an organism. More means more accurate
    *           but also slower.
    */
   public void setPassesWhenGettingFitness(int passesWhenGettingFitness) {
      _passesWhenGettingFitness = passesWhenGettingFitness;
   }

   /**
    * @return The number of output candles
    */
   public int getOutputCandleCount() {
      return _outputCandleCount;
   }

   /**
    * @param outputCandleCount The number of output candles
    */
   public void setOutputCandleCount(int outputCandleCount) {
      _outputCandleCount = outputCandleCount;
   }

   /**
    * @return The number of indices after which the output candles should start
    */
   public int getOutputCandleOffset() {
      return _outputCandleOffset;
   }

   /**
    * @param outputCandleOffset The number of indices after which the output
    *           candles should start (0 by default - directly after the last
    *           input candle)
    */
   public void setOutputCandleOffset(int outputCandleOffset) {
      _outputCandleOffset = outputCandleOffset;
   }

   /**
    * @return The distance between two candles.
    */
   public long getCandleDistance() {
      return _candleDistance;
   }

   /**
    * @param candleDistance The distance between two candles. If the candles are
    *           not with such a distance, new ones are created to fill the gaps
    *           and ones that are too dense are omitted.
    */
   public void setCandleDistance(long candleDistance) {
      _candleDistance = candleDistance;
   }

   /**
    * @return The unit in which the distance between the candles is
    */
   public TemporalUnit getCandleDistanceUnit() {
      return _candleDistanceUnit;
   }

   /**
    * @param candleDistanceUnit The unit in which the distance between the
    *           candles is
    */
   public void setCandleDistanceUnit(TemporalUnit candleDistanceUnit) {
      _candleDistanceUnit = candleDistanceUnit;
   }
}
