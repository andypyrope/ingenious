package io.github.andypyrope.fitness.data.common;

import io.github.andypyrope.fitness.data.DatasetCreationException;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class CsvDataset {

   private static final String DEFAULT_SEPARATOR = ",";

   private final List<String> _columns;
   private final Map<String, Integer> _columnIndices;

   protected final int _rowCount;

   private final List<String[]> _rawRows;

   protected CsvDataset(final String filename) {
      final String separator = DEFAULT_SEPARATOR;

      if (!filename.endsWith(".csv")) {
         throw new DatasetCreationException(
            String.format("The data file '%s' should have a CSV extension",
               filename));
      }

      final List<String> lines;
      final URL resource = getClass().getClassLoader()
            .getResource(FilenameUtils.separatorsToUnix(filename));
      if (resource == null) {
         throw new DatasetCreationException(
            String.format("Data file '%s' does not exist", filename));
      }
      final File file = new File(resource.getFile());

      try (Stream<String> stream = Files.lines(file.toPath())) {
         lines = stream.collect(Collectors.toList());
      } catch (IOException e) {
         throw new DatasetCreationException(
            String.format("Failed to load dataset '%s'", filename),
            e);
      }

      _columns = new ArrayList<>();
      _columnIndices = new HashMap<>();
      for (String columnName : lines.get(0).split(separator)) {
         final String lowercaseColumn = columnName.toLowerCase();
         if (_columnIndices.containsKey(lowercaseColumn)) {
            throw new DatasetCreationException(
               String.format(
                  "The column '%s' in CSV file '%s' has been encountered twice",
                  columnName,
                  filename));
         }
         _columnIndices.put(lowercaseColumn, _columns.size());
         _columns.add(lowercaseColumn);
      }

      _rawRows = new ArrayList<>();
      for (int i = 1; i < lines.size(); i++) {
         final String[] currentRow = lines.get(i).split(separator, -1);
         if (currentRow.length != _columns.size()) {
            throw new DatasetCreationException(
               String.format(
                  "Line '%d' in CSV file '%s' has an incorrect number of cells (%d instead of %d)",
                  i + 1,
                  filename,
                  currentRow.length,
                  _columns.size()));
         }
         _rawRows.add(currentRow);
      }
      _rowCount = _rawRows.size();
   }

   protected String getCell(int row, String column) {
      return getCell(row, column, false);
   }

   protected String getCell(int row, String column, boolean safe) {
      if (!hasColumns(new String[] { column })) {
         if (safe) {
            return null;
         }
         throw new DatasetCreationException(
            String.format("Column '%s' does not exist", column));
      }

      return getCell(row, _columnIndices.get(column));
   }

   private String getCell(int row, int columnIndex) {
      return _rawRows.get(row)[columnIndex];
   }

   private boolean hasColumns(final String[] columns) {
      return Arrays.stream(columns).allMatch(_columns::contains);
   }
}
