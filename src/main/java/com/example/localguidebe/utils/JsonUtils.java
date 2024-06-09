package com.example.localguidebe.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class JsonUtils {
  private static final ObjectMapper objectMapper =
      new ObjectMapper().registerModule(new JavaTimeModule());

  public static String convertObjectToJson(Object obj) throws Exception {
    return objectMapper.writeValueAsString(obj);
  }

  public static <T> T convertJsonToObject(String json, Class<T> clazz) throws Exception {
    return objectMapper.readValue(json, clazz);
  }

  public static <T> List<T> convertJsonToList(String json, Class<T> clazz) throws Exception {
    return objectMapper.readValue(
        json, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
  }

  public static <T> Set<T> convertJsonToSet(String json, Class<T> clazz) throws Exception {
    return objectMapper.readValue(
        json, objectMapper.getTypeFactory().constructCollectionType(Set.class, clazz));
  }

  public static <T> T convertJsonToObject(String json, TypeReference<T> typeReference)
      throws Exception {
    return objectMapper.readValue(json, typeReference);
  }

  public static String[] getNullPropertyNames(Object source) {
    final BeanWrapper src = new BeanWrapperImpl(source);
    PropertyDescriptor[] pds = src.getPropertyDescriptors();

    Set<String> emptyNames = new HashSet<>();
    for (PropertyDescriptor pd : pds) {
      Object srcValue = src.getPropertyValue(pd.getName());
      if (srcValue == null) emptyNames.add(pd.getName());
    }
    String[] result = new String[emptyNames.size()];
    return emptyNames.toArray(result);
  }
}
