package io.github.andypyrope.fitness.data.candle;

import java.util.ArrayList;
import java.util.List;

import io.github.andypyrope.fitness.data.common.CsvDataset;

public class CandleDataset extends CsvDataset {

   private final List<Candle> _data;

   private static final String DATE_COLUMN = "date";
   private static final String OPEN_COLUMN = "open";
   private static final String HIGH_COLUMN = "high";
   private static final String LOW_COLUMN = "low";
   private static final String CLOSE_COLUMN = "close";

   public CandleDataset(final String filename) {
      super(filename);
      _data = new ArrayList<>();
      for (int i = 0; i < _rowCount; i++) {
         _data.add(new Candle(
            null,
            getCell(i, DATE_COLUMN),
            getCell(i, OPEN_COLUMN),
            getCell(i, HIGH_COLUMN),
            getCell(i, LOW_COLUMN),
            getCell(i, CLOSE_COLUMN)));
      }
   }
   
   public List<Candle> getData() {
      return _data;
   }

}
