package com.logstash.repro;

import static java.util.concurrent.ForkJoinPool.commonPool;

import com.google.common.util.concurrent.RateLimiter;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

/** Application entry point. */
public final class Main {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  private Main() {}

  /**
   * Runs the application.
   *
   * @param args command-line arguments
   */
  public static void main(final String... args) {
    SLF4JBridgeHandler.removeHandlersForRootLogger();
    SLF4JBridgeHandler.install();
    Logging.configureLogstashEncoderDefaults();

    var rateLimiter = RateLimiter.create(10);
    var stop = new AtomicBoolean(false);

    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  System.err.println("*** shutting down since JVM is shutting down");
                  stop.set(true);
                }));

    var threadLocalHolder = new ThreadLocalHolder();

    while (!stop.get()) {
      if (rateLimiter.tryAcquire()) {
        commonPool()
            .execute(
                () -> {
                  var cnt = threadLocalHolder.acquire();
                  System.out.println("Count: " + cnt);
                });
      }
    }
  }
}
