package br.ufpr.tcc.image.utils;

import static br.ufpr.tcc.image.exceptions.ErrorKey.RESOURCE_NOT_FOUND;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import br.ufpr.tcc.image.support.TestSupport;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.MessageSource;

public class MessageUtilsTest extends TestSupport {

  private final String PARAM = "anyParam";

  @InjectMocks
  private MessageUtils messageUtils;

  @Mock
  private MessageSource messageSource;

  @Override
  public void init() {}

  @Test
  public void shouldCallMessageSource() {
    messageUtils.getMessage(RESOURCE_NOT_FOUND, PARAM);
    verify(messageSource)
        .getMessage(eq(RESOURCE_NOT_FOUND.getKey()), eq(new Object[] {PARAM}), any());
  }
}