package com.logstash.repro;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadLocalHolder {

  private static final AtomicInteger counter = new AtomicInteger();

  private final ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(this::initializeThread);

  private Integer initializeThread() {
    var cnt = counter.incrementAndGet();
    return cnt;
  }

  public final Integer acquire() {
    var integer = this.threadLocal.get();
    return integer;
  }
}
