package com.vcalebef.gerenciamento_estoque_api.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vcalebef.gerenciamento_estoque_api.enums.TipoBebida;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SecaoResponseDto {

    @JsonProperty("secao_id")
    private Long idSecao;
    private TipoBebida tipo;
    private BigDecimal volumeAtual;

}
