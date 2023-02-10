package br.ufpr.tcc.image.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorKey {
  RESOURCE_NOT_FOUND("resource.not.found"),
  INVALID_CONTENT_TYPE_ERROR("invalid.content.type"),
  INVALID_HTTP_RESPONSE("invalid.http.response"),
  MAXIMUM_REDIRECT_ATTEMPT_REACHED("maximum.redirect.attempt.reached");

  private final String key;
}
