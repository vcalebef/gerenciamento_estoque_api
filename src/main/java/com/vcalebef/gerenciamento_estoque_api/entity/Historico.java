package com.vcalebef.gerenciamento_estoque_api.entity;

import com.vcalebef.gerenciamento_estoque_api.enums.TipoBebida;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Table(name = "historico")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Historico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "idSecao", nullable = false)
    private Long idSecao;

    @Column(name = "horario", nullable = false)
    private LocalTime horario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_bebida", length = 25, nullable = false)
    private TipoBebida tipoBebida;

    @Column(name = "volume", precision = 10, scale = 2, nullable = false)
    private BigDecimal volume;

    @Column(name = "acao", nullable = false)
    private String acao;

    @Column(name = "responsavel", nullable = false)
    private String responsavel;

}
