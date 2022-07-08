package com.fw.cobranca.repository;

import com.fw.cobranca.domain.ColaboradorRegiao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

public interface ColaboradorRegiaoRepository extends CrudRepository<ColaboradorRegiao, Integer> {
    Iterable<ColaboradorRegiao> findByIdUsuario(Integer id_usuario);
    Iterable<ColaboradorRegiao> findByIdRegiao(Integer id_regiao);

    @Query(value = "SELECT * FROM COLABORADOR_REGIAO WHERE ID_USUARIO = :ID_USUARIO AND ID_REGIAO = :ID_REGIAO LIMIT 1 ", nativeQuery = true)
    Optional<ColaboradorRegiao> findByUsuarioRegiao(@Param("ID_USUARIO") Integer ID_USUARIO, @Param("ID_REGIAO") Integer ID_REGIAO);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM COLABORADOR_REGIAO WHERE ID_REGIAO = :ID_REGIAO  ", nativeQuery = true)
    Integer deleteByRegiao(@Param("ID_REGIAO") Integer ID_REGIAO);
}
