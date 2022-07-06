package com.fw.cobranca.repository;

import com.fw.cobranca.domain.Estabelecimento;
import com.fw.cobranca.domain.Usuario;
import com.fw.cobranca.domain.dto.EstabelecimentoDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface EstabelecimentoRepository extends CrudRepository<Estabelecimento, Integer> {
    Iterable<Estabelecimento> findByIdUsuario(Integer id_usuario);

    Optional<Estabelecimento> findByNome(String nome);

    Optional<Estabelecimento> findByCnpj(String cnpj);

//    @Query(value = " SELECT * FROM ESTABELECIMENTO WHERE ID_USUARIO = :ID_USUARIO ", nativeQuery = true)
//    List<Map<String, Object>> findByIdUsuario(@Param("ID_USUARIO") Integer ID_USUARIO);
}
