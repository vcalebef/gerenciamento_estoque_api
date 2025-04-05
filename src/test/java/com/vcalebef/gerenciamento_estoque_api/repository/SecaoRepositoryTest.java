package com.vcalebef.gerenciamento_estoque_api.repository;

import com.vcalebef.gerenciamento_estoque_api.entity.Secao;
import com.vcalebef.gerenciamento_estoque_api.enums.TipoBebida;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class SecaoRepositoryTest {

    @Autowired
    private SecaoRepository secaoRepository;

    @Autowired
    private EntityManager entityManager;

    private Secao criarSecao(TipoBebida tipo, BigDecimal volume, BigDecimal capacidadeMaxima) {
        Secao secao = new Secao();
        secao.setTipoBebida(tipo);
        secao.setVolumeAtual(volume);
        secao.setCapacidadeMaxima(capacidadeMaxima);
        secao.setDataDefinicaoTipo(LocalDateTime.now());
        return secao;
    }

    @Test
    @DisplayName("Deve retornar o volume total por tipo de bebida")
    void testGetTotalVolumeByTipoBebida() {
        entityManager.persist(criarSecao(TipoBebida.ALCOOLICA, new BigDecimal("10.5"), new BigDecimal("100")));
        entityManager.persist(criarSecao(TipoBebida.ALCOOLICA, new BigDecimal("5.5"), new BigDecimal("100")));
        entityManager.persist(criarSecao(TipoBebida.NAO_ALCOOLICA, new BigDecimal("8.0"), new BigDecimal("100")));

        BigDecimal total = secaoRepository.getTotalVolumeByTipoBebida(TipoBebida.ALCOOLICA);

        assertThat(total).isEqualByComparingTo(new BigDecimal("16.0"));
    }

    @Test
    @DisplayName("Deve encontrar seções disponíveis com capacidade suficiente")
    void testFindSecoesDisponiveis() {
        entityManager.persist(criarSecao(TipoBebida.ALCOOLICA, new BigDecimal("10.0"), new BigDecimal("20.0"))); // sobra 10
        entityManager.persist(criarSecao(TipoBebida.ALCOOLICA, new BigDecimal("15.0"), new BigDecimal("30.0"))); // sobra 15
        entityManager.persist(criarSecao(TipoBebida.ALCOOLICA, new BigDecimal("18.0"), new BigDecimal("20.0"))); // sobra 2 (não entra)
        entityManager.persist(criarSecao(TipoBebida.NAO_ALCOOLICA, new BigDecimal("5.0"), new BigDecimal("50.0"))); // tipo errado

        List<Secao> secoes = secaoRepository.findSecoesDisponiveis(TipoBebida.ALCOOLICA, new BigDecimal("5.0"));

        assertThat(secoes).hasSize(2);
        assertThat(secoes).allMatch(s -> s.getTipoBebida() == TipoBebida.ALCOOLICA);
    }

    @Test
    @DisplayName("Deve encontrar seções com volume disponível para venda")
    void testFindSecoesDisponiveisVenda() {
        entityManager.persist(criarSecao(TipoBebida.ALCOOLICA, new BigDecimal("10.0"), new BigDecimal("20.0"))); // ok
        entityManager.persist(criarSecao(TipoBebida.ALCOOLICA, new BigDecimal("0.0"), new BigDecimal("20.0"))); // volume 0
        entityManager.persist(criarSecao(TipoBebida.NAO_ALCOOLICA, new BigDecimal("5.0"), new BigDecimal("20.0"))); // tipo errado

        List<Secao> secoes = secaoRepository.findSecoesDisponiveisVenda(TipoBebida.ALCOOLICA);

        assertThat(secoes).hasSize(1);
        assertThat(secoes.get(0).getVolumeAtual()).isEqualByComparingTo("10.0");
    }
}
