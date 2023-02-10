package br.ufpr.tcc.image.gateways.outputs;

import java.io.File;
import java.nio.file.Path;

public interface ImageDownloader {

  File downloadUrl(String urlAsString, Path destination);

}
