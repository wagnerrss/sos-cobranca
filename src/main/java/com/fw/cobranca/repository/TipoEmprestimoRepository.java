package com.fw.cobranca.repository;

import com.fw.cobranca.domain.TipoEmprestimo;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TipoEmprestimoRepository extends CrudRepository<TipoEmprestimo, Integer> {
    Optional<TipoEmprestimo> findByDescricao(String descricao);
}
