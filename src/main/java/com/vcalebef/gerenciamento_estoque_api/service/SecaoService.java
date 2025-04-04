package com.vcalebef.gerenciamento_estoque_api.service;

import com.vcalebef.gerenciamento_estoque_api.entity.Secao;
import com.vcalebef.gerenciamento_estoque_api.enums.TipoBebida;
import com.vcalebef.gerenciamento_estoque_api.exception.StockCapacityException;
import com.vcalebef.gerenciamento_estoque_api.exception.TypeDrinkInvalidException;
import com.vcalebef.gerenciamento_estoque_api.repository.HistoricoRepository;
import com.vcalebef.gerenciamento_estoque_api.repository.SecaoRepository;
import com.vcalebef.gerenciamento_estoque_api.web.dto.IncluirBebidaDto;
import com.vcalebef.gerenciamento_estoque_api.web.dto.SecaoResponseDto;
import com.vcalebef.gerenciamento_estoque_api.web.dto.VenderBebidaDto;
import com.vcalebef.gerenciamento_estoque_api.exception.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class SecaoService {

    @Autowired
    private final SecaoRepository secaoRepository;

    @Autowired
    private final HistoricoService historicoService;


    @Transactional
    public SecaoResponseDto adicionarBebida(Long idSecao, IncluirBebidaDto incluirBebidaDto) throws TypeDrinkInvalidException {

        Secao secao = secaoRepository.findById(idSecao)
                .orElseThrow(() -> new EntityNotFoundException("Seção não encontrada"));

        LocalDateTime agora = LocalDate.now().atStartOfDay();

        // Verifica se a secao esta vazia para a inclusao de um novo tipo de bebida
        if (secao.getDataDefinicaoTipo() == null || !agora.equals(secao.getDataDefinicaoTipo())) {
            if (secao.getVolumeAtual().compareTo(BigDecimal.ZERO) == 0) {
                secao.setDataDefinicaoTipo(agora);
                secao.definirTipoBebida(incluirBebidaDto.getTipo());
            }
        }

        // Verifica se a o tipo da bebida é o mesmo incluso na seção
        if (secao.getTipoBebida() != null && !secao.getTipoBebida().equals(incluirBebidaDto.getTipo())) {
            throw new TypeDrinkInvalidException(String.format("Tipo da bebida é diferente do tipo da seção. tipo atual = {%s}", incluirBebidaDto.getTipo()));
        }

        BigDecimal novoVolume = secao.getVolumeAtual().add(incluirBebidaDto.getVolume());

        // Verifica se há espaço suficiente
        if (novoVolume.compareTo(secao.getCapacidadeMaxima()) > 0) {
            throw new StockCapacityException("Capacidade máxima da seção atingida!");
        } else {
            secao.setVolumeAtual(novoVolume);
        }

        secaoRepository.save(secao);

        //Registrar historico
        historicoService.registrarHistorico(secao.getId(), incluirBebidaDto.getTipo(), incluirBebidaDto.getVolume(), "entrada", incluirBebidaDto.getResponsavel());

        return new SecaoResponseDto(secao.getId(), secao.getTipoBebida(), secao.getVolumeAtual());
    }

    @Transactional(readOnly = true)
    public Secao buscarPorId(Long id) {

        return secaoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Secao cujo id é {%s} nao encontrada", id))
        );

    }

    @Transactional(readOnly = true)
    public List<Secao> buscarSecoes() {
        return secaoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public BigDecimal consultarVolumeTotalTipo(TipoBebida tipoBebida) {
        return secaoRepository.getTotalVolumeByTipoBebida(tipoBebida);
    }

    @Transactional(readOnly = true)
    public List<Secao> consultarSecoesDisponiveis(TipoBebida tipoBebida, BigDecimal volume) {
        return secaoRepository.findSecoesDisponiveis(tipoBebida, volume);
    }

    @Transactional(readOnly = true)
    public List<Secao> consultarSecoesDisponiveisVenda(TipoBebida tipoBebida) {
        return secaoRepository.findSecoesDisponiveisVenda(tipoBebida);
    }

    public List<SecaoResponseDto> venderBebida(@Valid VenderBebidaDto venderBebidaDto) {

        BigDecimal volumeTotal = consultarVolumeTotalTipo(venderBebidaDto.getTipo());

        if (volumeTotal.compareTo(BigDecimal.ZERO) <= 0 || venderBebidaDto.getVolume().compareTo(volumeTotal) > 0) {
            throw new StockCapacityException("Não há estoque disponível para este tipo de bebida.");
        }

        List<Secao> secoes = consultarSecoesDisponiveisVenda(venderBebidaDto.getTipo());
        BigDecimal volumeRestante = venderBebidaDto.getVolume();

        // Lista para armazenar as seções que foram alteradas
        List<SecaoResponseDto> secoesAlteradas = new ArrayList<>();

        for (Secao secao : secoes) {
            if (volumeRestante.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }

            BigDecimal volumeNaSecao = secao.getVolumeAtual();

            if (volumeNaSecao.compareTo(volumeRestante) >= 0) {
                // A seção tem bebida suficiente para suprir toda a venda
                secao.setVolumeAtual(volumeNaSecao.subtract(volumeRestante));
                volumeRestante = BigDecimal.ZERO;
            } else {
                // Se a seção não tiver o suficiente, zeramos ela e seguimos para a próxima
                secao.setVolumeAtual(BigDecimal.ZERO);
                volumeRestante = volumeRestante.subtract(volumeNaSecao);
            }

            secaoRepository.save(secao);

            //Registrar historico
            historicoService.registrarHistorico(secao.getId(), venderBebidaDto.getTipo(), venderBebidaDto.getVolume(),"venda", venderBebidaDto.getResponsavel());

            // Adiciona a seção alterada na lista de retorno
            secoesAlteradas.add(new SecaoResponseDto(
                    secao.getId(),
                    secao.getTipoBebida(),
                    secao.getVolumeAtual()
            ));
        }

        if (volumeRestante.compareTo(BigDecimal.ZERO) > 0) {
            throw new StockCapacityException("O estoque disponível não é suficiente para essa venda.");
        }

        return secoesAlteradas;

    }

}
