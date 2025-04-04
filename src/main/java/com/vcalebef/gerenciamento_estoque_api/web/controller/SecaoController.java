package com.vcalebef.gerenciamento_estoque_api.web.controller;

import com.vcalebef.gerenciamento_estoque_api.entity.Secao;
import com.vcalebef.gerenciamento_estoque_api.enums.TipoBebida;
import com.vcalebef.gerenciamento_estoque_api.exception.TypeDrinkInvalidException;
import com.vcalebef.gerenciamento_estoque_api.service.SecaoService;
import com.vcalebef.gerenciamento_estoque_api.web.dto.IncluirBebidaDto;
import com.vcalebef.gerenciamento_estoque_api.web.dto.SecaoResponseDto;
import com.vcalebef.gerenciamento_estoque_api.web.dto.VenderBebidaDto;
import com.vcalebef.gerenciamento_estoque_api.web.dto.mapper.SecaoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/secoes")
public class SecaoController {

    private final SecaoService secaoService;

    @PostMapping("/{idSecao}/entrada")
    public ResponseEntity<SecaoResponseDto> incluirBebida(
            @PathVariable Long idSecao ,
            @Valid @RequestBody IncluirBebidaDto incluirBebidaDto) throws TypeDrinkInvalidException {

        SecaoResponseDto responseDto = secaoService.adicionarBebida(idSecao, incluirBebidaDto);

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/venda")
    public ResponseEntity<List<SecaoResponseDto>> venderBebida(
            @Valid @RequestBody VenderBebidaDto venderBebidaDto) {

         List<SecaoResponseDto> responseDto = secaoService.venderBebida(venderBebidaDto);

         return ResponseEntity.ok(responseDto);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Secao> getSecaoById(@PathVariable Long id) {
        Secao secao = secaoService.buscarPorId(id);
        return ResponseEntity.ok(secao);
    }

    @GetMapping
    public ResponseEntity<List<Secao>> getSecoes() {

        List<Secao> secoes = secaoService.buscarSecoes();
        return ResponseEntity.ok(secoes);
    }

    @GetMapping("/volumeTotal/{tipoBebida}")
    public BigDecimal getVolumeTotalTipoBebida(@PathVariable TipoBebida tipoBebida) {
        return secaoService.consultarVolumeTotalTipo(tipoBebida);
    }

    @GetMapping("/locaisDisponiveis/{volume}/{tipoBebida}")
    public ResponseEntity<List<SecaoResponseDto>> getSecoesDisponiveis(
            @PathVariable TipoBebida tipoBebida,
            @PathVariable BigDecimal volume) {

        List<Secao> secoes = secaoService.consultarSecoesDisponiveis(tipoBebida, volume);

        return ResponseEntity.ok(SecaoMapper.toListSecaoResponseDto(secoes));
    }

    @GetMapping("locaisDisponiveisVenda/{tipoBebida}")
    public ResponseEntity<List<SecaoResponseDto>> getSecoesDisponiveisVenda(
            @PathVariable TipoBebida tipoBebida) {

        List<Secao> secoes = secaoService.consultarSecoesDisponiveisVenda(tipoBebida);

        return ResponseEntity.ok(SecaoMapper.toListSecaoResponseDto(secoes));

    }

}
