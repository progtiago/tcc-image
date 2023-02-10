package br.ufpr.tcc.image.exceptions;

import lombok.Getter;

@Getter
public class DownloadImageException extends RuntimeException {

  private static final long serialVersionUID = -7989799904591699129L;

  private final boolean retryable;

  public DownloadImageException(final boolean retryable) {
    this.retryable = retryable;
  }

  public DownloadImageException(final String message, final boolean retryable) {
    super(message);
    this.retryable = retryable;
  }

  public DownloadImageException(final String message,
                                final Throwable throwable,
                                final boolean retryable) {
    super(message, throwable);
    this.retryable = retryable;
  }

}
