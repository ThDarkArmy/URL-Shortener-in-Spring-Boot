package tda.darkarmy.url.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class GeneralApiResponse {
    private int statusCode;
    private String message;
}
