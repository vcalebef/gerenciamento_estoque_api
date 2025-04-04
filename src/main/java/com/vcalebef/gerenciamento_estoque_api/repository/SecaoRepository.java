package com.vcalebef.gerenciamento_estoque_api.repository;

import com.vcalebef.gerenciamento_estoque_api.entity.Secao;
import com.vcalebef.gerenciamento_estoque_api.enums.TipoBebida;
import com.vcalebef.gerenciamento_estoque_api.web.dto.SecaoResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SecaoRepository extends JpaRepository<Secao, Long> {


    @Query("SELECT COALESCE(SUM(s.volumeAtual), 0) FROM Secao s WHERE s.tipoBebida = :tipoBebida")
    BigDecimal getTotalVolumeByTipoBebida(TipoBebida tipoBebida);

    @Query("SELECT s FROM Secao s WHERE s.tipoBebida = :tipoBebida AND (s.capacidadeMaxima - s.volumeAtual) >= :volume")
    List<Secao> findSecoesDisponiveis(@Param("tipoBebida") TipoBebida tipoBebida, @Param("volume") BigDecimal volume);

    @Query("SELECT s FROM Secao s WHERE s.tipoBebida = :tipoBebida AND s.volumeAtual > 0")
    List<Secao> findSecoesDisponiveisVenda(TipoBebida tipoBebida);
}


