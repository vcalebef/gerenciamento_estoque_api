package com.vcalebef.gerenciamento_estoque_api.web.controller;

import com.vcalebef.gerenciamento_estoque_api.enums.TipoBebida;
import com.vcalebef.gerenciamento_estoque_api.service.HistoricoService;
import com.vcalebef.gerenciamento_estoque_api.web.dto.HistoricoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/historico")
@RequiredArgsConstructor
public class HistoricoController {

    private final HistoricoService historicoService;

    @GetMapping
    public ResponseEntity<List<HistoricoResponseDto>> consultarHistorico(
            @RequestParam(required = false) TipoBebida tipoBebida,
            @RequestParam(required = false) Long idSecao,
            @RequestParam(required = false) String acao,
            @RequestParam(defaultValue = "data") String ordenacao) {

        List<HistoricoResponseDto> historico = historicoService.consultarHistorico(tipoBebida, idSecao, acao, ordenacao);
        return ResponseEntity.ok(historico);
    }

}
