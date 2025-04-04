package com.vcalebef.gerenciamento_estoque_api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vcalebef.gerenciamento_estoque_api.enums.TipoBebida;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "secoes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Secao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("secao_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_bebida", length = 25)
    private TipoBebida tipoBebida;

    @Column(name = "volume_atual", precision = 10, scale = 2, nullable = false)
    private BigDecimal volumeAtual = BigDecimal.ZERO;

    @Column(name = "capacidade_maxima", precision = 10, scale = 2)
    private BigDecimal capacidadeMaxima;

    @Column(name = "data_definicao_tipo")
    private LocalDateTime dataDefinicaoTipo;

    public void definirTipoBebida(TipoBebida tipoBebida) {
        this.tipoBebida = tipoBebida;
        this.capacidadeMaxima = (tipoBebida == TipoBebida.ALCOOLICA) ? new BigDecimal("500") : new BigDecimal("400");
    }

}
