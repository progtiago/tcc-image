package br.ufpr.tcc.image.utils;

import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

import br.ufpr.tcc.image.exceptions.JsonException;
import br.ufpr.tcc.image.support.TestSupport;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;


public class JsonUtilsTest extends TestSupport {

  public static final String MESSAGE = "{\"attribute\":\"%s\",\"value\":\"%s\"}";
  public static final String LIST_MESSAGE = "[{\"attribute\":\"%s\",\"value\":\"%s\"}]";

  @InjectMocks
  private JsonUtils jsonUtils;

  @Spy
  private ObjectMapper objectMapper;

  private AnyResource anyResource;

  @Override
  public void init() {
    anyResource = new AnyResource("attributeName", "attributeValue");
  }

  @Test
  public void shouldConvertJsonToObject() {
    AnyResource convertedMessage =
        jsonUtils.toObject(
            format(MESSAGE, anyResource.getAttribute(), anyResource.getValue()), AnyResource.class);

    assertEquals(anyResource, convertedMessage);
  }

  @Test
  public void shouldConvertJsonBytesToObject() {
    AnyResource convertedMessage =
        jsonUtils.toObject(
            format(MESSAGE, anyResource.getAttribute(), anyResource.getValue()), AnyResource.class);

    assertEquals(anyResource, convertedMessage);
  }

  @Test
  public void shouldConvertJsonToObjectList() {
    List<AnyResource> convertedMessage =
        jsonUtils.toObject(
            format(LIST_MESSAGE, anyResource.getAttribute(), anyResource.getValue()),
            new TypeReference<>() {});

    assertEquals(singletonList(anyResource), convertedMessage);
  }

  @Test(expected = JsonException.class)
  public void shouldNotConvertJsonToObject() {
    jsonUtils.toObject("asdsa", AnyResource.class);
  }


  @Test(expected = JsonException.class)
  public void shouldNotConvertJsonToObjectList() {
    jsonUtils.toObject("asdsa", new TypeReference<>() {});
  }

  @Test
  public void shouldConvertObjectToJson() {
    String expectedJson = format(MESSAGE, anyResource.getAttribute(), anyResource.getValue());

    String json = jsonUtils.toJson(anyResource);
    assertEquals(expectedJson, json);
  }

  @Test(expected = JsonException.class)
  public void shouldNotConvertObjectToJson() {
    jsonUtils.toJson(new Object());
  }

  @Test
  public void shouldConvertJSONBytesToObject() {
    AnyResource convertedMessage = jsonUtils.toObject(
        format(MESSAGE,
            anyResource.getAttribute(),
            anyResource.getValue()).getBytes(),
        AnyResource.class);

    assertEquals(anyResource, convertedMessage);
  }

  @Test(expected = JsonException.class)
  public void shouldNotConvertJsonBytesToObject() {
    jsonUtils.toObject("asdsa".getBytes(), AnyResource.class);
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  static class AnyResource {

    private String attribute;
    private String value;
  }
}
