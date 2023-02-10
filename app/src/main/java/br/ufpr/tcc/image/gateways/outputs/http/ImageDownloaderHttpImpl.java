package br.ufpr.tcc.image.gateways.outputs.http;

import static br.ufpr.tcc.image.exceptions.ErrorKey.INVALID_CONTENT_TYPE_ERROR;
import static br.ufpr.tcc.image.exceptions.ErrorKey.INVALID_HTTP_RESPONSE;
import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

import br.ufpr.tcc.image.exceptions.DownloadImageException;
import br.ufpr.tcc.image.exceptions.ImageNotFoundException;
import br.ufpr.tcc.image.gateways.outputs.ImageDownloader;
import br.ufpr.tcc.image.utils.HttpConnectUtils;
import br.ufpr.tcc.image.utils.MessageUtils;
import br.ufpr.tcc.image.utils.ResizerUtils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageDownloaderHttpImpl implements ImageDownloader {

  private static final String JPG = "jpg";
  private final HttpConnectUtils httpConnectUtils;
  private final MessageUtils messageUtils;
  private final ResizerUtils resizerUtils;

  @Override
  public File downloadUrl(final String urlAsString, final Path destination) {
    log.info("Starting image download: {}", urlAsString);
    File imageFile;
    HttpURLConnection httpURLConnection = null;

    try {
      httpURLConnection = getConnection(urlAsString);
      var downloadedImage = getDownloadedImage(httpURLConnection);
      var resizedImage = resizerUtils.resize(downloadedImage);
      imageFile = new File(destination.toString());
      ImageIO.write(resizedImage, JPG, imageFile);
    } catch (ImageNotFoundException e) {
      log.warn("Image not found: {}", urlAsString);
      throw e;
    } catch (DownloadImageException e) {
      log.error("Download error for image url: {}", urlAsString, e);
      throw e;
    } catch(IllegalArgumentException e) {
      log.error("Invalid image to download url: {}", urlAsString, e);
      throw new DownloadImageException(e.getMessage(), e, false);
    } catch(SocketTimeoutException e) {
      log.error("Read timed out for image url: {}", urlAsString, e);
      throw new DownloadImageException(e.getMessage(), e, true);
    } catch (Exception e) {
      log.error("Error downloading image url: {}", urlAsString, e);
      throw new DownloadImageException(e.getMessage(), e, true);
    } finally {
      disconnect(httpURLConnection);
    }
    return imageFile;
  }

  private void disconnect(final HttpURLConnection httpURLConnection) {
    if (nonNull(httpURLConnection)) {
      httpURLConnection.disconnect();
    }
  }

  private HttpURLConnection getConnection(String url) throws Exception {
    var httpURLConnection = httpConnectUtils.connect(url);
    var responseCode = httpURLConnection.getResponseCode();

    if(responseCode == NOT_FOUND.value()) {
      throw new ImageNotFoundException();
    }

    if(responseCode != OK.value()) {
      throw new DownloadImageException(messageUtils.getMessage(INVALID_HTTP_RESPONSE, responseCode),
          isRetryableStatus(responseCode));
    }
    return httpURLConnection;
  }

  private boolean isRetryableStatus(Integer responseCode) {
    return asList(BAD_REQUEST,UNAUTHORIZED,FORBIDDEN,NOT_ACCEPTABLE)
        .stream()
        .map(HttpStatus::value)
        .noneMatch(status -> status.equals(responseCode));
  }

  private BufferedImage getDownloadedImage(final HttpURLConnection connection) throws IOException {
    var contentType = connection.getContentType();
    try (InputStream inputStream = connection.getInputStream()) {
      validateInputStream(contentType, inputStream);
      var downloadedImage = ImageIO.read(inputStream);
      validateBufferedImage(contentType, downloadedImage);
      return downloadedImage;
    }
  }

  private void validateBufferedImage(String contentType, BufferedImage downloadedImage) {
    if (downloadedImage == null && !IMAGE_JPEG_VALUE.equalsIgnoreCase(contentType)) {
      throw new IllegalArgumentException(
          messageUtils.getMessage(INVALID_CONTENT_TYPE_ERROR, contentType, IMAGE_JPEG_VALUE));
    }
  }

  private void validateInputStream(final String contentType, final InputStream inputStream) {
    if (inputStream == null && !IMAGE_JPEG_VALUE.equalsIgnoreCase(contentType)) {
      throw new IllegalArgumentException(
          messageUtils.getMessage(INVALID_CONTENT_TYPE_ERROR, contentType, IMAGE_JPEG_VALUE));
    }
  }
}
