package br.ufpr.tcc.image.exceptions;

public class NotFoundException extends RuntimeException {

  private static final long serialVersionUID = 9001076528690941288L;

  public NotFoundException() {}

  public NotFoundException(final String message) {
    super(message);
  }
}
