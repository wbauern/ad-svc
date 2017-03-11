package com.sovrn.ad.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(Include.NON_EMPTY)
@JsonDeserialize(builder = ErrorItem.ErrorItemBuilder.class)
public class ErrorItem {

  private final String code;
  private final String message;

  @JsonPOJOBuilder(withPrefix = "")
  public static final class ErrorItemBuilder {
  }
}