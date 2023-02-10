package br.ufpr.tcc.image.gateways.inputs.http.responses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse implements Serializable {

  private static final long serialVersionUID = -130292785026387590L;

  private List<String> errors = new ArrayList();

}