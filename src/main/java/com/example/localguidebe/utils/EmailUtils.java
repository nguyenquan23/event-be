package com.example.localguidebe.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class EmailUtils {
  public String readFileFromClasspath(String filePath) throws IOException {
    StringBuilder contentBuilder = new StringBuilder();
    try (BufferedReader br =
        new BufferedReader(
            new InputStreamReader(getClass().getClassLoader().getResourceAsStream(filePath)))) {
      String line;
      while ((line = br.readLine()) != null) {
        contentBuilder.append(line).append("\n");
      }
    }
    return contentBuilder.toString();
  }
}
