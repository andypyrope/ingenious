package io.github.andypyrope.ai;

/**
 * Thrown when there is an attempt to adjust a layer without having calculated anything
 * with it yet.
 */
public class NoCalculationException extends RuntimeException {
   private static final long serialVersionUID = 8241364349278481892L;
}
