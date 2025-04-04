package com.vcalebef.gerenciamento_estoque_api.web.dto;

import com.vcalebef.gerenciamento_estoque_api.enums.TipoBebida;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoResponseDto {

    private Long idSecao;
    private LocalTime horario;
    private TipoBebida tipoBebida;
    private BigDecimal volume;
    private String acao;
    private String responsavel;

}
