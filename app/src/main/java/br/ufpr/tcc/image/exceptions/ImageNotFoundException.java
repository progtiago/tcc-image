package br.ufpr.tcc.image.exceptions;

import lombok.Getter;

@Getter
public class ImageNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 8490411189655464532L;

  public ImageNotFoundException() {
  }

}
