package com.github.dmitriylamzin.dto;

import org.springframework.data.annotation.Id;

public class Account {
  @Id
  private Long id;
  private Bitcoin bitcoin;
  private Person person;

  public Account() {
  }

  public Account(Bitcoin bitcoin, Person person) {
    this.id = bitcoin.getId();
    this.bitcoin = bitcoin;
    this.person = person;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Bitcoin getBitcoin() {
    return bitcoin;
  }

  public void setBitcoin(Bitcoin bitcoin) {
    this.bitcoin = bitcoin;
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Account)) return false;

    Account account = (Account) o;

    if (id != null ? !id.equals(account.id) : account.id != null) return false;
    if (bitcoin != null ? !bitcoin.equals(account.bitcoin) : account.bitcoin != null) return false;
    return !(person != null ? !person.equals(account.person) : account.person != null);

  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (bitcoin != null ? bitcoin.hashCode() : 0);
    result = 31 * result + (person != null ? person.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Account{" +
            "id=" + id +
            ", bitcoin=" + bitcoin +
            ", person=" + person +
            '}';
  }
}
