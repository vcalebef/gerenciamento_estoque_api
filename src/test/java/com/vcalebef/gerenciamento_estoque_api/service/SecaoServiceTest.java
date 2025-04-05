package com.vcalebef.gerenciamento_estoque_api.service;

import com.vcalebef.gerenciamento_estoque_api.entity.Secao;
import com.vcalebef.gerenciamento_estoque_api.enums.TipoBebida;
import com.vcalebef.gerenciamento_estoque_api.exception.StockCapacityException;
import com.vcalebef.gerenciamento_estoque_api.exception.TypeDrinkInvalidException;
import com.vcalebef.gerenciamento_estoque_api.repository.SecaoRepository;
import com.vcalebef.gerenciamento_estoque_api.web.dto.IncluirBebidaDto;
import com.vcalebef.gerenciamento_estoque_api.web.dto.SecaoResponseDto;
import com.vcalebef.gerenciamento_estoque_api.web.dto.VenderBebidaDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SecaoServiceTest {

    @Mock
    private SecaoRepository secaoRepository;

    @Mock
    private HistoricoService historicoService;

    @InjectMocks
    private SecaoService secaoService;

    private VenderBebidaDto vendaDto;

    @BeforeEach
    void setUp() {
        vendaDto = new VenderBebidaDto();
        vendaDto.setTipo(TipoBebida.ALCOOLICA);
        vendaDto.setVolume(new BigDecimal("80.00"));
        vendaDto.setResponsavel("Carlos");
    }

    @Test
    void deveVenderBebidaDistribuindoEntreSecoes() {
        // Total disponível = 50 + 50 = 100
        Secao secao1 = new Secao();
        secao1.setId(1L);
        secao1.setTipoBebida(TipoBebida.ALCOOLICA);
        secao1.setVolumeAtual(new BigDecimal("50"));

        Secao secao2 = new Secao();
        secao2.setId(2L);
        secao2.setTipoBebida(TipoBebida.ALCOOLICA);
        secao2.setVolumeAtual(new BigDecimal("50"));

        when(secaoRepository.getTotalVolumeByTipoBebida(TipoBebida.ALCOOLICA))
                .thenReturn(new BigDecimal("100"));

        when(secaoRepository.findSecoesDisponiveisVenda(TipoBebida.ALCOOLICA))
                .thenReturn(List.of(secao1, secao2));

        List<SecaoResponseDto> resposta = secaoService.venderBebida(vendaDto);

        // Verifica se as seções foram atualizadas corretamente
        assertEquals(2, resposta.size());
        assertEquals(BigDecimal.ZERO, resposta.get(0).getVolumeAtual()); // secao1: 50 - 50
        assertEquals(new BigDecimal("20.00"), resposta.get(1).getVolumeAtual()); // secao2: 50 - 30

        verify(secaoRepository, times(2)).save(any(Secao.class));
        verify(historicoService, times(2))
                .registrarHistorico(anyLong(), eq(TipoBebida.ALCOOLICA), eq(new BigDecimal("80.00")), eq("venda"), eq("Carlos"));
    }

    @Test
    void deveLancarExcecaoQuandoEstoqueInsuficiente() {
        when(secaoRepository.getTotalVolumeByTipoBebida(TipoBebida.ALCOOLICA))
                .thenReturn(new BigDecimal("30"));

        StockCapacityException ex = org.junit.jupiter.api.Assertions.assertThrows(
                StockCapacityException.class,
                () -> secaoService.venderBebida(vendaDto)
        );

        assertEquals("Não há estoque disponível para este tipo de bebida.", ex.getMessage());
        verifyNoInteractions(historicoService);
    }

    @Test
    void deveAdicionarBebidaComSucesso() throws TypeDrinkInvalidException {
        Long idSecao = 1L;
        IncluirBebidaDto dto = new IncluirBebidaDto("João", new BigDecimal("100.00"), TipoBebida.ALCOOLICA);

        Secao secao = new Secao();
        secao.setId(idSecao);
        secao.setVolumeAtual(BigDecimal.ZERO);
        secao.setCapacidadeMaxima(new BigDecimal("500.00"));

        when(secaoRepository.findById(idSecao)).thenReturn(Optional.of(secao));
        when(secaoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        SecaoResponseDto response = secaoService.adicionarBebida(idSecao, dto);

        assertEquals(idSecao, response.getIdSecao());
        assertEquals(TipoBebida.ALCOOLICA, response.getTipo());
        assertEquals(new BigDecimal("100.00"), response.getVolumeAtual());

        verify(historicoService, times(1))
                .registrarHistorico(eq(idSecao), eq(TipoBebida.ALCOOLICA), eq(new BigDecimal("100.00")), eq("entrada"), eq("João"));
    }

}
