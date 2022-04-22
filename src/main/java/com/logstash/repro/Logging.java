package com.logstash.repro;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import net.logstash.logback.composite.loggingevent.ArgumentsJsonProvider;
import net.logstash.logback.encoder.LogstashEncoder;
import org.slf4j.LoggerFactory;

public class Logging {

  public static void configureLogstashEncoderDefaults() {
    var rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    var context = rootLogger.getLoggerContext();

    context.reset();

    LogstashEncoder encoder = new LogstashEncoder();
    encoder.setContext(context);
    encoder.addProvider(new ArgumentsJsonProvider());
    encoder.start();

    final ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<>();
    appender.setTarget("System.out");
    appender.setName("stdout");
    appender.setEncoder(encoder);
    appender.setContext(context);
    appender.start();

    rootLogger.addAppender(appender);
    rootLogger.setLevel(Level.INFO);
  }
}
