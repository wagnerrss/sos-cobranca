package com.fw.cobranca.repository;

import com.fw.cobranca.domain.Notificacao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface NotificacaoRepository extends CrudRepository<Notificacao, Integer> {
    Iterable<Notificacao> findByIdUsuario(Integer id_usuario);

    @Query(value = " SELECT COUNT(ID) FROM NOTIFICACAO WHERE LIDA = 'N' AND ID_USUARIO = :ID_USUARIO ", nativeQuery = true)
    Integer quantidadeNotificacaoNaoLidaPorUsuario(@Param("ID_USUARIO") Integer ID_USUARIO);

    @Query(value = " SELECT COUNT(N.ID) FROM NOTIFICACAO N JOIN USUARIO U ON U.ID = N.ID_USUARIO WHERE U.TIPO = 'A' AND N.LIDA = 'N' ", nativeQuery = true)
    Integer quantidadeNotificacaoNaoLidaAdministrador();
}

