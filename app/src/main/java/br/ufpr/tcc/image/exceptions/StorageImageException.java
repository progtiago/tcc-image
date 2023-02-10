package br.ufpr.tcc.image.exceptions;

public class StorageImageException extends RuntimeException {

  public StorageImageException() {}

  public StorageImageException(final String message, final Throwable e) {
    super(message, e);
  }
}
