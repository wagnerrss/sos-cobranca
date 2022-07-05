package com.fw.cobranca.repository;

import com.fw.cobranca.domain.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, Integer> {
    Optional<Usuario> findByNome(String nome);
    Optional<Usuario> findByDocumento(String documento);

    @Query(value = " SELECT * FROM USUARIO WHERE UPPER(NOME) LIKE UPPER(:NOME) ", nativeQuery = true)
    Iterable<Usuario> findByNomeAproximado(@Param("NOME") String NOME);

@Query(value = " SELECT * FROM USUARIO WHERE TIPO = :TIPO ", nativeQuery = true)
    Iterable<Usuario> findByTipo(@Param("TIPO") String TIPO);

    @Query(value = " SELECT * FROM USUARIO WHERE UPPER(NOME) LIKE UPPER(:NOME) AND TIPO = :TIPO", nativeQuery = true)
    Iterable<Usuario> findByNomeAproximadoETipo(@Param("NOME") String NOME,
                                           @Param("TIPO") String TIPO);

}
