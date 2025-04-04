package com.vcalebef.gerenciamento_estoque_api.config;

import com.vcalebef.gerenciamento_estoque_api.entity.Secao;
import com.vcalebef.gerenciamento_estoque_api.repository.SecaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
public class SecaoInitializer implements CommandLineRunner {

    @Autowired
    private SecaoRepository secaoRepository;

    @Override
    public void run(String... args) throws Exception {
        if (secaoRepository.count() == 0) { // Só insere se não houver seções no banco
            List<Secao> secoes = Arrays.asList(
                    new Secao(null, null, BigDecimal.ZERO, null, null),
                    new Secao(null, null, BigDecimal.ZERO, null, null),
                    new Secao(null, null, BigDecimal.ZERO, null, null),
                    new Secao(null, null, BigDecimal.ZERO, null, null),
                    new Secao(null, null, BigDecimal.ZERO, null, null)
            );
            secaoRepository.saveAll(secoes);
        }
    }
}
