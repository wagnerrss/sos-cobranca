package com.fw.cobranca.repository;

import com.fw.cobranca.domain.Emprestimo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;


public interface EmprestimoRepository extends CrudRepository<Emprestimo, Integer> {
    Iterable<Emprestimo> findByIdUsuario(Integer id_Usuario);

    Iterable<Emprestimo> findByIdEstabelecimento(Integer id_estabelecimento);

    @Query(value = " SELECT E.* " +
            " FROM EMPRESTIMO E " +
            //" JOIN TIPO_EMPRESTIMO TE ON TE.ID = E.ID_TIPO " +
            " WHERE E.ID_USUARIO = :ID_USUARIO " +
            " AND E.STATUS <> 9 " +
            " ORDER BY E.STATUS, E.DATA_SOLICITACAO, E.ID ", nativeQuery = true)
    Iterable<Emprestimo> findEmprestimosPorIdUsuario(@Param("ID_USUARIO") Integer ID_USUARIO);

    @Query(value = " SELECT E.* " +
            " FROM EMPRESTIMO E " +
           // " JOIN TIPO_EMPRESTIMO TE ON TE.ID = E.ID_TIPO " +
            " WHERE E.ID_USUARIO_ANALISE = :ID_USUARIO_ANALISE " +
            " AND E.STATUS <> 9 " +
            " ORDER BY E.STATUS, E.DATA_SOLICITACAO, E.ID ", nativeQuery = true)
    Iterable<Emprestimo> findEmprestimosPorIdUsuarioAnalise(@Param("ID_USUARIO_ANALISE") Integer ID_USUARIO_ANALISE);

    @Query(value = " SELECT E.* " +
            " FROM EMPRESTIMO E " +
           // " JOIN TIPO_EMPRESTIMO TE ON TE.ID = E.ID_TIPO " +
            " WHERE E.STATUS = :STATUS " +
            " ORDER BY E.STATUS, E.DATA_SOLICITACAO, E.ID ", nativeQuery = true)
    Iterable<Emprestimo> findEmprestimosPorStatus(@Param("STATUS") Integer STATUS);

    @Query(value = " SELECT E.* " +
            " FROM EMPRESTIMO E " +
           // " JOIN TIPO_EMPRESTIMO TE ON TE.ID = E.ID_TIPO " +
            " WHERE E.STATUS IN (0,1) " + //0 AGUARDANDO ANÁLISE,1 EM ANÁLISE
            " AND E.ID_USUARIO = :ID_USUARIO " +
            " ORDER BY E.STATUS, E.DATA_SOLICITACAO, E.ID ", nativeQuery = true)
    Iterable<Emprestimo> findEmprestimosAguardandoPorIdUsuario(@Param("ID_USUARIO") Integer ID_USUARIO);

    @Query(value = " SELECT E.* " +
            " FROM EMPRESTIMO E " +
            //" JOIN TIPO_EMPRESTIMO TE ON TE.ID = E.ID_TIPO " +
            " JOIN ESTABELECIMENTO ES ON ES.ID = E.ID_ESTABELECIMENTO " +
            " JOIN REGIAO R ON R.REGIAO = UPPER(ES.BAIRRO) ||' - '|| UPPER(ES.CIDADE) ||' - '|| UPPER(ES.ESTADO) " +
            " WHERE E.STATUS = :STATUS " +
            " AND ((E.ID_USUARIO_ANALISE =:ID_USUARIO_ANALISE) OR ((" +
            "  SELECT CR.ID FROM COLABORADOR_REGIAO CR " +
            "  WHERE CR.ID_REGIAO = R.ID AND CR.ID_USUARIO = :ID_USUARIO_ANALISE) > 0)) " +
            " ORDER BY E.STATUS, E.DATA_SOLICITACAO, E.ID ", nativeQuery = true)
    Iterable<Emprestimo> findEmprestimosPorStatusEIdUsuarioAnalise(@Param("STATUS") Integer STATUS,
                                                                   @Param("ID_USUARIO_ANALISE") Integer ID_USUARIO_ANALISE);

    @Query(value = " SELECT E.*, COUNT(P.VALOR_VENCIMENTO) quantidade_parcelas_atraso, U.NOME nome_usuario_analise\n" +
            "FROM PARCELA P \n" +
            "JOIN EMPRESTIMO E ON E.ID = P.ID_EMPRESTIMO AND E.STATUS = 2\n" +
            "JOIN USUARIO U ON U.ID = E.ID_USUARIO_ANALISE\n" +
            "WHERE CAST(P.DATA_VENCIMENTO AS DATE) < CURRENT_DATE AND COALESCE(P.VALOR_PAGAMENTO, 0.0) <= 0.00\n" +
            "GROUP BY E.ID, E.ID_TIPO, E.ID_USUARIO, E.ID_ESTABELECIMENTO, E.DATA_SOLICITACAO, E.VALOR_SOLICITADO,\n" +
            "E.STATUS, E.DATA_APROVACAO, E.VALOR_APROVADO, E.ID_USUARIO_ANALISE, E.OBSERVACOES, E.TIPO, E.DOCUMENTO, E.QUANTIDADE_PARCELAS, U.NOME ", nativeQuery = true)
    List<Map> findEmprestimosEmAtraso();

    @Query(value = " SELECT E.*, COUNT(P.VALOR_VENCIMENTO) quantidade_parcelas_atraso, U.NOME nome_usuario_analise\n" +
            "FROM PARCELA P \n" +
            "JOIN EMPRESTIMO E ON E.ID = P.ID_EMPRESTIMO AND E.STATUS = 2\n" +
            "JOIN USUARIO U ON U.ID = E.ID_USUARIO_ANALISE\n" +
            "WHERE CAST(P.DATA_VENCIMENTO AS DATE) < CURRENT_DATE AND COALESCE(P.VALOR_PAGAMENTO, 0.0) <= 0.00\n" +
            " AND E.ID_USUARIO_ANALISE = :USUARIO \n" +
            "GROUP BY E.ID, E.ID_TIPO, E.ID_USUARIO, E.ID_ESTABELECIMENTO, E.DATA_SOLICITACAO, E.VALOR_SOLICITADO,\n" +
            "E.STATUS, E.DATA_APROVACAO, E.VALOR_APROVADO, E.ID_USUARIO_ANALISE, E.OBSERVACOES, E.TIPO, E.DOCUMENTO, E.QUANTIDADE_PARCELAS, U.NOME ", nativeQuery = true)
    List<Map> findEmprestimosEmAtrasoPorUsuario(@Param("USUARIO") Integer USUARIO);

    @Query(value = " SELECT E.*, COUNT(P.VALOR_VENCIMENTO) quantidade_parcelas_avencer, U.NOME nome_usuario_analise\n" +
            "FROM PARCELA P \n" +
            "JOIN EMPRESTIMO E ON E.ID = P.ID_EMPRESTIMO AND E.STATUS = 2\n" +
            "JOIN USUARIO U ON U.ID = E.ID_USUARIO_ANALISE\n" +
            "WHERE CAST(P.DATA_VENCIMENTO AS DATE) >= CURRENT_DATE AND COALESCE(P.VALOR_PAGAMENTO, 0.0) <= 0.00\n" +
            "GROUP BY E.ID, E.ID_TIPO, E.ID_USUARIO, E.ID_ESTABELECIMENTO, E.DATA_SOLICITACAO, E.VALOR_SOLICITADO,\n" +
            "E.STATUS, E.DATA_APROVACAO, E.VALOR_APROVADO, E.ID_USUARIO_ANALISE, E.OBSERVACOES, E.TIPO, E.DOCUMENTO, E.QUANTIDADE_PARCELAS, U.NOME ", nativeQuery = true)
    List<Map> findEmprestimosAVencer();

    @Query(value = " SELECT E.*, COUNT(P.VALOR_VENCIMENTO) quantidade_parcelas_avencer, U.NOME nome_usuario_analise\n" +
            "FROM PARCELA P \n" +
            "JOIN EMPRESTIMO E ON E.ID = P.ID_EMPRESTIMO AND E.STATUS = 2\n" +
            "JOIN USUARIO U ON U.ID = E.ID_USUARIO_ANALISE\n" +
            "WHERE CAST(P.DATA_VENCIMENTO AS DATE) >= CURRENT_DATE AND COALESCE(P.VALOR_PAGAMENTO, 0.0) <= 0.00\n" +
            " AND E.ID_USUARIO_ANALISE = :USUARIO \n" +
            "GROUP BY E.ID, E.ID_TIPO, E.ID_USUARIO, E.ID_ESTABELECIMENTO, E.DATA_SOLICITACAO, E.VALOR_SOLICITADO,\n" +
            "E.STATUS, E.DATA_APROVACAO, E.VALOR_APROVADO, E.ID_USUARIO_ANALISE, E.OBSERVACOES, E.TIPO, E.DOCUMENTO, E.QUANTIDADE_PARCELAS, U.NOME ", nativeQuery = true)
    List<Map> findEmprestimosAVencerPorUsuarioAnalise(@Param("USUARIO") Integer USUARIO);

    @Query(value = " SELECT E.*, COUNT(P.VALOR_VENCIMENTO) quantidade_parcelas_avencer, U.NOME nome_usuario_analise\n" +
            "FROM PARCELA P \n" +
            "JOIN EMPRESTIMO E ON E.ID = P.ID_EMPRESTIMO AND E.STATUS = 2\n" +
            "JOIN USUARIO U ON U.ID = E.ID_USUARIO_ANALISE\n" +
            "WHERE CAST(P.DATA_VENCIMENTO AS DATE) >= CURRENT_DATE AND COALESCE(P.VALOR_PAGAMENTO, 0.0) <= 0.00\n" +
            " AND E.ID_USUARIO = :USUARIO \n" +
            "GROUP BY E.ID, E.ID_TIPO, E.ID_USUARIO, E.ID_ESTABELECIMENTO, E.DATA_SOLICITACAO, E.VALOR_SOLICITADO,\n" +
            "E.STATUS, E.DATA_APROVACAO, E.VALOR_APROVADO, E.ID_USUARIO_ANALISE, E.OBSERVACOES, E.TIPO, E.DOCUMENTO, E.QUANTIDADE_PARCELAS, U.NOME ", nativeQuery = true)
    List<Map> findEmprestimosAVencerPorUsuario(@Param("USUARIO") Integer USUARIO);

    @Query(value = " SELECT E.*, " +
            "(SELECT COUNT(P.VALOR_VENCIMENTO) FROM PARCELA P " +
            " WHERE P.ID_EMPRESTIMO = E.ID " +
            " AND CAST(P.DATA_VENCIMENTO AS DATE) < CURRENT_DATE " +
            " AND COALESCE(P.VALOR_PAGAMENTO, 0.0) <= 0.00) quantidade_parcelas_atraso, " +
            " (SELECT COUNT(P.VALOR_VENCIMENTO) FROM PARCELA P " +
            " WHERE P.ID_EMPRESTIMO = E.ID " +
            " AND CAST(P.DATA_VENCIMENTO AS DATE) >= CURRENT_DATE " +
            " AND COALESCE(P.VALOR_PAGAMENTO, 0.0) <= 0.00) quantidade_parcelas_avencer, " +
            " u.nome nome_usuario_analise " +
            "            FROM EMPRESTIMO E " +
            "            JOIN USUARIO U ON U.ID = E.ID_USUARIO_ANALISE " +
            " WHERE E.STATUS = 2   ", nativeQuery = true)
    List<Map> findEmprestimos();

    @Query(value = " SELECT E.*, " +
            "(SELECT COUNT(P.VALOR_VENCIMENTO) FROM PARCELA P " +
            " WHERE P.ID_EMPRESTIMO = E.ID " +
            " AND CAST(P.DATA_VENCIMENTO AS DATE) < CURRENT_DATE " +
            " AND COALESCE(P.VALOR_PAGAMENTO, 0.0) <= 0.00) quantidade_parcelas_atraso, " +
            " (SELECT COUNT(P.VALOR_VENCIMENTO) FROM PARCELA P " +
            " WHERE P.ID_EMPRESTIMO = E.ID " +
            " AND CAST(P.DATA_VENCIMENTO AS DATE) >= CURRENT_DATE " +
            " AND COALESCE(P.VALOR_PAGAMENTO, 0.0) <= 0.00) quantidade_parcelas_avencer, " +
            "U.NOME nome_usuario_analise " +
            "            FROM EMPRESTIMO E " +
            "            JOIN USUARIO U ON U.ID = E.ID_USUARIO_ANALISE " +
            " WHERE E.STATUS = 2   " +
            " AND E.ID_USUARIO_ANALISE = :USUARIO ", nativeQuery = true)
    List<Map> findEmprestimosPorUsuario(@Param("USUARIO") Integer USUARIO);


}
