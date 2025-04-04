package com.vcalebef.gerenciamento_estoque_api.repository;

import com.vcalebef.gerenciamento_estoque_api.entity.Historico;
import com.vcalebef.gerenciamento_estoque_api.enums.TipoBebida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoRepository extends JpaRepository<Historico, Long> {

    @Query("SELECT h FROM Historico h " +
            "WHERE (:tipoBebida IS NULL OR h.tipoBebida = :tipoBebida) " +
            "AND (:idSecao IS NULL OR h.idSecao = :idSecao) " +
            "AND (:acao IS NULL OR h.acao = :acao) " +
            "ORDER BY " +
            "CASE WHEN :ordenacao = 'data' THEN h.horario END ASC, " +
            "CASE WHEN :ordenacao = 'secao' THEN h.idSecao END ASC")
    List<Historico> findHistoricoByFilters(
            @Param("tipoBebida") TipoBebida tipoBebida,
            @Param("idSecao") Long idSecao,
            @Param("acao") String acao,
            @Param("ordenacao") String ordenacao
    );

}
