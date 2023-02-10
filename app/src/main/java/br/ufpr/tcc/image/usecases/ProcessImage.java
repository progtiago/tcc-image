package br.ufpr.tcc.image.usecases;

import static br.ufpr.tcc.image.domains.ImageStatus.ERROR;
import static br.ufpr.tcc.image.domains.ImageStatus.NOT_FOUND;
import static br.ufpr.tcc.image.domains.ImageStatus.SUCCESS;
import static org.apache.commons.lang3.BooleanUtils.isTrue;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultString;

import br.ufpr.tcc.image.configurations.graphics.ImageConfigProperties;
import br.ufpr.tcc.image.domains.Image;
import br.ufpr.tcc.image.domains.Metadata;
import br.ufpr.tcc.image.exceptions.ImageNotFoundException;
import br.ufpr.tcc.image.gateways.outputs.ImageDownloader;
import br.ufpr.tcc.image.utils.FileUtils;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProcessImage {

  private final ImageDownloader imageDownloader;
  private final FileUtils fileUtils;
  private final ImageConfigProperties imageConfigProperties;

  public void execute(final Image image, final Metadata metadata) {
    var rootDir = imageConfigProperties.getDownload().getRootDir();
    var imageDir = defaultString(metadata.getImageDir());
    var filename = String.format("image_%s.jpg", image.getPosition());

    try {
      fileUtils.createDir(Paths.get(rootDir, imageDir));
      imageDownloader.downloadUrl(
          image.getOriginalUri(), Paths.get(rootDir, imageDir, filename));
      image.setLocalUri(Paths.get(imageDir, filename).toString());
      image.setImageStatus(SUCCESS);
      image.setErrorMessage(EMPTY);
    } catch (ImageNotFoundException ie) {
      image.setImageStatus(NOT_FOUND);
      image.setErrorMessage(EMPTY);
      if(isTrue(metadata.getStopDownloadAfter404())) throw ie;
    } catch (Exception e) {
      image.setImageStatus(ERROR);
      image.setErrorMessage(e.getMessage());
      throw e;
    }
  }
}
