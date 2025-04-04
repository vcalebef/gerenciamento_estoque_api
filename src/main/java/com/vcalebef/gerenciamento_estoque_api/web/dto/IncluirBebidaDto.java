package com.vcalebef.gerenciamento_estoque_api.web.dto;

import com.vcalebef.gerenciamento_estoque_api.enums.TipoBebida;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IncluirBebidaDto {

    @NotBlank(message = "O responsável não pode estar em branco")
    private String responsavel;

    @NotNull(message = "O volume da bebida deve ser informado")
    private BigDecimal volume;

    @NotNull(message = "O tipo de bebida deve ser informado")
    private TipoBebida tipo;

}
