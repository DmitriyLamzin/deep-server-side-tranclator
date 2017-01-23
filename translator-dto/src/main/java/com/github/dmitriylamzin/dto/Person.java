package com.github.dmitriylamzin.dto;



import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "persons", type = "person", shards = 1, replicas = 0, refreshInterval = "-1")
public class Person {

  @Id
  private long id;
  private String gender;
  private String firstName;
  private String lastName;
  private String email;
  private String ipAddress;

  public long getId() {
    return id;
  }

  public String getGender() {
    return gender;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getEmail() {
    return email;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public void setId(long id) {
    this.id = id;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Person)) return false;

    Person person = (Person) o;

    if (id != person.id) return false;
    if (gender != null ? !gender.equals(person.gender) : person.gender != null) return false;
    if (firstName != null ? !firstName.equals(person.firstName) : person.firstName != null) return false;
    if (lastName != null ? !lastName.equals(person.lastName) : person.lastName != null) return false;
    if (email != null ? !email.equals(person.email) : person.email != null) return false;
    return !(ipAddress != null ? !ipAddress.equals(person.ipAddress) : person.ipAddress != null);

  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (gender != null ? gender.hashCode() : 0);
    result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
    result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
    result = 31 * result + (email != null ? email.hashCode() : 0);
    result = 31 * result + (ipAddress != null ? ipAddress.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Person{" +
            "id=" + id +
            ", gender='" + gender + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", ipAddress='" + ipAddress + '\'' +
            '}';
  }
}
