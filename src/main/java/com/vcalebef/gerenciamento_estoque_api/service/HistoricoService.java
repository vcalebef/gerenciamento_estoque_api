package com.vcalebef.gerenciamento_estoque_api.service;

import com.vcalebef.gerenciamento_estoque_api.entity.Historico;
import com.vcalebef.gerenciamento_estoque_api.enums.TipoBebida;
import com.vcalebef.gerenciamento_estoque_api.repository.HistoricoRepository;
import com.vcalebef.gerenciamento_estoque_api.web.dto.HistoricoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoricoService {

    private final HistoricoRepository historicoRepository;

    public List<HistoricoResponseDto> consultarHistorico(TipoBebida tipoBebida, Long idSecao, String acao, String ordenacao) {
        List<Historico> historicoList = historicoRepository.findHistoricoByFilters(tipoBebida, idSecao, acao, ordenacao);
        return historicoList.stream()
                .map(h -> new HistoricoResponseDto(
                        h.getIdSecao(),
                        h.getHorario(),
                        h.getTipoBebida(),
                        h.getVolume(),
                        h.getAcao(),
                        h.getResponsavel()))
                .collect(Collectors.toList());
    }

    public void registrarHistorico(Long idSecao, TipoBebida tipo, BigDecimal volume, String acao, String responsavel) {
        Historico historico = new Historico();
        historico.setIdSecao(idSecao);
        historico.setTipoBebida(tipo);
        historico.setVolume(volume);
        historico.setHorario(LocalTime.now());
        historico.setAcao(acao);
        historico.setResponsavel(responsavel);

        historicoRepository.save(historico);
    }

}
