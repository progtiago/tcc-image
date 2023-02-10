package br.ufpr.tcc.image.utils;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

import br.ufpr.tcc.image.exceptions.ErrorKey;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageUtils {

  private final MessageSource messageSource;

  public String getMessage(final ErrorKey errorKey, final Object... param) {
    return messageSource.getMessage(errorKey.getKey(), param, getLocale());
  }
}