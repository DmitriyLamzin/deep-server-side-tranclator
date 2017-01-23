package com.github.dmitriylamzin.elasticfiller.service.batch;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import com.github.dmitriylamzin.dto.Person;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.beans.factory.InitializingBean;

public class PersonLineMapper implements LineMapper<Person>, InitializingBean {

  private final JsonFactory jFactory = new JsonFactory();
  private static final String PERSON_ID = "id";
  private static final String PERSON_GENDER = "gender";
  private static final String PERSON_FIRST_NAME = "first_name";
  private static final String PERSON_LAST_NAME = "last_name";
  private static final String PERSON_EMAIL = "email";
  private static final String PERSON_IP_ADDRESS = "ip_address";

  @Override
  public void afterPropertiesSet() throws Exception {

  }

  @Override
  public Person mapLine(String line, int lineNumber) throws Exception {
    JsonParser parser = jFactory.createParser(line);
    Person person = new Person();
    while (parser.nextToken() != JsonToken.END_OBJECT){
      String fieldName = parser.getCurrentName();

      if (PERSON_ID.equals(fieldName)){
        parser.nextToken();
        person.setId(parser.getLongValue());
      }

      if (PERSON_GENDER.equals(fieldName)){
        parser.nextToken();
        person.setGender(parser.getText());
      }

      if (PERSON_FIRST_NAME.equals(fieldName)){
        parser.nextToken();
        person.setFirstName(parser.getText());
      }

      if (PERSON_LAST_NAME.equals(fieldName)){
        parser.nextToken();
        person.setLastName(parser.getText());
      }

      if (PERSON_EMAIL.equals(fieldName)){
        parser.nextToken();
        person.setEmail(parser.getText());
      }

      if (PERSON_IP_ADDRESS.equals(fieldName)){
        parser.nextToken();
        person.setIpAddress(parser.getText());
      }

    }
    return person;
  }
}
