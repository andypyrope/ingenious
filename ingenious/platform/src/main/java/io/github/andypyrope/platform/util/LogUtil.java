package io.github.andypyrope.platform.util;

import java.util.logging.Logger;

/**
 * A utility class for loggers.
 */
public class LogUtil {

   /**
    * Creates a new {@link Logger} instance.
    *
    * @param clazz The class the logger will be for.
    * @return A new logger.
    */
   public static Logger make(final Class clazz) {
      return Logger.getLogger(clazz.getName());
   }
}
