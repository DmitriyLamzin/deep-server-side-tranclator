package com.github.dmitriylamzin.redisfiller.domain;

import com.github.dmitriylamzin.dto.Bitcoin;
import org.springframework.batch.item.ItemReader;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BitcoinQueue implements ItemReader<Bitcoin> {

  private final Queue<Bitcoin> bitcoins = new ConcurrentLinkedQueue<>();

  private boolean isReady;

  public void put(Bitcoin bitcoin) {
    bitcoins.add(bitcoin);
  }

  public Bitcoin read() {
    return bitcoins.poll();
  }

  public boolean isReady() {
    return isReady;
  }

  public void setIsReady(boolean isReady) {
    this.isReady = isReady;
  }

  public boolean isEmpty() {
    return bitcoins.isEmpty();
  }

}
