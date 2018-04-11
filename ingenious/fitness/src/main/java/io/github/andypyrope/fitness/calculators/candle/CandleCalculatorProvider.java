package io.github.andypyrope.fitness.calculators.candle;

import io.github.andypyrope.fitness.calculators.Calculator;
import io.github.andypyrope.fitness.calculators.CalculatorProvider;
import io.github.andypyrope.fitness.calculators.InvalidCalculatorSettingsException;
import io.github.andypyrope.fitness.data.candle.Candle;
import io.github.andypyrope.fitness.data.candle.CandleDataset;
import io.github.andypyrope.platform.dna.Dna;

public class CandleCalculatorProvider implements CalculatorProvider {

   private final CandleCalculatorSettings _settings;
   /**
    * The Candle datasets shared among all calculators
    */
   private final Candle[][] _normalizedCandles;

   public CandleCalculatorProvider(String[] datasets,
      CandleCalculatorSettings settings)
      throws InvalidCalculatorSettingsException {

      _settings = settings;
      _normalizedCandles = new Candle[datasets.length][];
      for (int i = 0; i < datasets.length; i++) {
         final CandleDataset current = new CandleDataset(datasets[i]);

         if (current.getData().length < settings.getOutputCandleOffset() +
               settings.getOutputCandleCount() + settings.getMaxInputCandles()) {
            throw new InvalidCalculatorSettingsException(
               String.format(
                  "The size of dataset '%s', %d, is smaller than the sum " +
                        "of the maximum input nodes, %d, and " +
                        "the number of output nodes, %d, plus their offset, %d",
                  datasets[i],
                  current.getData().length,
                  settings.getMaxInputCandles(),
                  settings.getOutputCandleCount(),
                  settings.getOutputCandleOffset()));
         }
         _normalizedCandles[i] = current.getSmoothData(
            settings.getCandleDistance(),
            settings.getCandleDistanceUnit());
      }
   }

   @Override
   public Calculator provide(Dna dna) {
      return new CandleCalculator(dna, _normalizedCandles, _settings);
   }

   @Override
   public int getDesiredDnaLength() {
      int desiredDnaLength = 0;
      desiredDnaLength++; // _inputCandleCount
      desiredDnaLength++; // _passesPerInput
      desiredDnaLength++; // _hiddenLayerCount
      desiredDnaLength += _settings.getMaxHiddenLayers(); // hiddenLayers

      return desiredDnaLength;
   }

   @Override
   public long getMaxStudyingComplexity() {
      return _settings.getMaxNeuralNetworkEdges();
   }
}
