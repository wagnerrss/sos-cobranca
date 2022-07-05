package com.fw.cobranca.repository;

import com.fw.cobranca.domain.Regiao;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RegiaoRepository extends CrudRepository<Regiao, Integer> {
    Optional<Regiao> findByRegiao(String regiao);
}
