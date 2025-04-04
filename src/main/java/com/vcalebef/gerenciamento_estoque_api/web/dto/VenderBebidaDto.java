package com.vcalebef.gerenciamento_estoque_api.web.dto;

import com.vcalebef.gerenciamento_estoque_api.enums.TipoBebida;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VenderBebidaDto {

    @NotNull(message = "O volume da bebida deve ser informado")
    private BigDecimal volume;

    @NotNull(message = "O tipo de bebida deve ser informado")
    private TipoBebida tipo;

    @NotBlank(message = "O responsável não pode estar em branco")
    private String responsavel;

}
