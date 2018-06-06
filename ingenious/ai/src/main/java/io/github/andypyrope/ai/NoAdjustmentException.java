package io.github.andypyrope.ai;

/**
 * Thrown when there is an attempt to get the input gradient of a layer, for which no
 * adjustments have been made yet.
 */
public class NoAdjustmentException extends RuntimeException {
   private static final long serialVersionUID = 2603428725495691042L;
}
