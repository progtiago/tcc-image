package br.ufpr.tcc.image.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Metadata {

  private String imageDir;
  private Boolean stopDownloadAfter404;

}
