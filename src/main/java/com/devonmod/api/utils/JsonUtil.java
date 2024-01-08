package com.devonmod.api.utils;

import static com.devonmod.api.exception.Error.JSON_WRITE;

import com.devonmod.api.exception.JsonWriteException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class JsonUtil {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  public String writeToJson(Object object) {
    try {
      OBJECT_MAPPER.registerModule(new JavaTimeModule());
      OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      return OBJECT_MAPPER.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new JsonWriteException(JSON_WRITE);
    }
  }

}
