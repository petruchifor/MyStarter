package com.telus.commerce.myapp.domain;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Resource {

  @NotNull(message = "first name cannot be empty")
  private String firstName;

  @NotNull(message = "last name cannot be empty")
  private String lastName;
}
