package br.ufpr.tcc.image.utils;

import static java.awt.Color.WHITE;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static org.imgscalr.Scalr.Method.ULTRA_QUALITY;
import static org.imgscalr.Scalr.Mode.FIT_EXACT;

import br.ufpr.tcc.image.configurations.graphics.ImageConfigProperties;
import java.awt.image.BufferedImage;
import lombok.RequiredArgsConstructor;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResizerUtils {

  private final ImageConfigProperties imageConfigProperties;

  public BufferedImage resize(final BufferedImage bufferedImage) {
    int width = imageConfigProperties.getResize().getWidth();
    int height = imageConfigProperties.getResize().getHeight();

    var squareImage = isSquare(bufferedImage) ? bufferedImage : makeSquare(bufferedImage);
    return needsResize(squareImage) ? Scalr.resize(squareImage, ULTRA_QUALITY, FIT_EXACT, width, height) : squareImage;
  }

  private BufferedImage makeSquare(final BufferedImage originImage) {

    int edge = Integer.max(originImage.getWidth(), originImage.getHeight());
    var squareImage = new BufferedImage(edge, edge, TYPE_INT_RGB);
    int marginX = getCentralizedMargin(edge, originImage.getWidth());
    int marginY = getCentralizedMargin(edge, originImage.getHeight());
    var squareImageGraphics = squareImage.getGraphics();

    squareImageGraphics.setColor(WHITE);
    squareImageGraphics.fillRect(0, 0, edge, edge);
    squareImageGraphics.drawImage(originImage, marginX, marginY, null);
    squareImageGraphics.dispose();

    return squareImage;
  }

  private int getCentralizedMargin(final int containerEdgeSize, final int edgeSize) {
    return (containerEdgeSize - edgeSize) / 2;
  }

  private boolean isSquare(final BufferedImage bufferedImage) {
    return bufferedImage.getWidth() == bufferedImage.getHeight();
  }

  private boolean needsResize(final BufferedImage bufferedImage) {
    int widthGoal = imageConfigProperties.getResize().getWidth();
    int heightGoal = imageConfigProperties.getResize().getHeight();

    return bufferedImage.getWidth() != widthGoal || bufferedImage.getHeight() != heightGoal;
  }
}