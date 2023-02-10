package br.ufpr.tcc.image.utils;

import br.ufpr.tcc.image.exceptions.StorageImageException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.stereotype.Component;

@Component
public class FileUtils {

  public void createDir(final Path path) {
    try {
      if (Files.notExists(path)) {
        Files.createDirectories(path);
      }
    } catch (Exception e) {
      throw new StorageImageException("Can't create image directory", e);
    }
  }
}
