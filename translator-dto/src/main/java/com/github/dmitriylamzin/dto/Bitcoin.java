package com.github.dmitriylamzin.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("bitcoins")
public class Bitcoin implements Serializable {

  @Id
  private long id;
  private String bitcoinValue;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Bitcoin)) return false;

    Bitcoin bitcoin = (Bitcoin) o;

    if (id != bitcoin.id) return false;
    return !(bitcoinValue != null ? !bitcoinValue.equals(bitcoin.bitcoinValue) : bitcoin.bitcoinValue != null);

  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (bitcoinValue != null ? bitcoinValue.hashCode() : 0);
    return result;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getBitcoinValue() {
    return bitcoinValue;
  }

  public void setBitcoinValue(String bitcoinValue) {
    this.bitcoinValue = bitcoinValue;
  }

  @Override
  public String toString() {
    return "Bitcoin{" +
            "id=" + id +
            ", bitcoinValue='" + bitcoinValue + '\'' +
            '}';
  }
}
