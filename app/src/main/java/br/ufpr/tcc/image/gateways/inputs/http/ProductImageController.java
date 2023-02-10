package br.ufpr.tcc.image.gateways.inputs.http;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import br.ufpr.tcc.image.gateways.inputs.http.requests.ProductImageRequest;
import br.ufpr.tcc.image.gateways.internals.DownloadSender;
import br.ufpr.tcc.image.gateways.outputs.ProductImageDataGateway;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/images")
public class ProductImageController {

  private static final String IMAGE_DIR_HEADER = "image_dir";
  private static final String STOP_DOWNLOAD_AFTER_404_HEADER = "stop_download_after_404";

  private final ProductImageDataGateway productImageDataGateway;
  private final DownloadSender downloadSender;

  @ApiOperation(value = "Download product images")
  @ApiResponses(
      value = {
          @ApiResponse(code = 202, message = "Accepted - The process is asynchronous."),
          @ApiResponse(code = 400, message = "Bad request. Check request and try again.")
      })
  @PostMapping(path = "/download", consumes = APPLICATION_JSON_VALUE)
  @ResponseStatus(ACCEPTED)
  public void download(
      @RequestHeader(value = IMAGE_DIR_HEADER, required = false) final String imageDir,
      @RequestHeader(value = STOP_DOWNLOAD_AFTER_404_HEADER, required = false) final Boolean stopDownloadAfter404,
      @RequestBody @Valid final ProductImageRequest request) {
    productImageDataGateway.save(request.toDomain(imageDir, stopDownloadAfter404));
    downloadSender.send(request.getProductId());
  }
}
