package br.ufpr.tcc.image.exceptions;

public class JsonException extends RuntimeException {

  public JsonException(final String message, final Throwable throwable) {
    super(message, throwable);
  }
}
