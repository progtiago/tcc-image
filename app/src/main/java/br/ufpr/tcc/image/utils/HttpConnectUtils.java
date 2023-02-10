package br.ufpr.tcc.image.utils;

import static br.ufpr.tcc.image.exceptions.ErrorKey.MAXIMUM_REDIRECT_ATTEMPT_REACHED;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

import br.ufpr.tcc.image.configurations.graphics.ImageConfigProperties;
import br.ufpr.tcc.image.exceptions.DownloadImageException;
import java.net.HttpURLConnection;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HttpConnectUtils {

  public static final String LOCATION_HEADER = "Location";
  public static final String ACCEPT_HEADER = "Accept";
  public static final String USER_AGENT_HEADER = "User-Agent";

  private final ImageConfigProperties imageConfigProperties;
  private final MessageUtils messageUtils;

  public HttpURLConnection connect(final String url) throws Exception {
    int maxRedirect = imageConfigProperties.getDownload().getMaxRedirects();
    int redirectCount = 0;
    String urlToRequest = url;
    do {
      var urlConnection = tryConnect(urlToRequest);
      if (isRedirectCodeFamily(urlConnection.getResponseCode())) {
        urlToRequest = urlConnection.getHeaderField(LOCATION_HEADER);
        urlConnection.disconnect();
        redirectCount++;
      } else {
        return urlConnection;
      }
    } while (redirectCount < maxRedirect);
    throw new DownloadImageException(
        messageUtils.getMessage(MAXIMUM_REDIRECT_ATTEMPT_REACHED, maxRedirect), false);
  }

  private HttpURLConnection tryConnect(final String url) throws Exception {
    var urlConnection = (HttpURLConnection) new URL(url).openConnection();
    urlConnection.setConnectTimeout(imageConfigProperties.getDownload().getConnectionTimeoutMillis());
    urlConnection.setReadTimeout(imageConfigProperties.getDownload().getReadTimeoutMillis());
    urlConnection.setRequestProperty(USER_AGENT_HEADER, imageConfigProperties.getDownload().getUserAgent());
    urlConnection.setRequestProperty(ACCEPT_HEADER, IMAGE_JPEG_VALUE);
    urlConnection.setInstanceFollowRedirects(true);
    return urlConnection;
  }

  private boolean isRedirectCodeFamily(int responseCode) {
    return responseCode / 100 == 3;
  }
}
