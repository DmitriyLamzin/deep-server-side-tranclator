package com.github.dmitriylamzin.elasticfiller.service.batch;


import com.github.dmitriylamzin.dto.Person;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class PersonLineMapperTest {
  private final static String JSON_TEST_DATA = "{\"id\":2," +
          "\"gender\":\"Female\"," +
          "\"first_name\":\"Deborah\"," +
          "\"last_name\":\"Price\"," +
          "\"email\":\"dprice1@google.com.au\"," +
          "\"ip_address\":\"82.166.11.236\"},\n";


  @Test
  public void ShouldMapLineToPerson() throws Exception {
    Person originalPerson = new Person();
    originalPerson.setEmail("dprice1@google.com.au");
    originalPerson.setId(2);
    originalPerson.setFirstName("Deborah");
    originalPerson.setLastName("Price");
    originalPerson.setGender("Female");
    originalPerson.setIpAddress("82.166.11.236");

    PersonLineMapper testedLineMapper = new PersonLineMapper();

    Person mappedPerson = testedLineMapper.mapLine(JSON_TEST_DATA, new Random().nextInt());

    assertEquals(originalPerson, mappedPerson);
  }
}
