package com.fw.cobranca.repository;

import com.fw.cobranca.domain.Parcela;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Map;


public interface ParcelaRepository extends CrudRepository<Parcela, Integer> {
    Iterable<Parcela> findByIdEmprestimo(Integer id_Emprestimo);

    @Query(value = " SELECT  " +
            " P.ID, " +
            " TO_CHAR(CAST(P.DATA_VENCIMENTO AS DATE), 'DD/MM/YYYY') DATA_VENCIMENTO, " +
            " CAST(EXTRACT (DAY FROM P.DATA_VENCIMENTO) AS INTEGER) DIA, " +
            " CASE EXTRACT (MONTH FROM P.DATA_VENCIMENTO) " +
            " WHEN 1 THEN 'Janeiro' " +
            " WHEN 2 THEN 'Fevereiro' " +
            " WHEN 3 THEN 'Março' " +
            " WHEN 4 THEN 'Abril' " +
            " WHEN 5 THEN 'Maio' " +
            " WHEN 6 THEN 'Junho' " +
            " WHEN 7 THEN 'Julho' " +
            " WHEN 8 THEN 'Agosto' " +
            " WHEN 9 THEN 'Setembro' " +
            " WHEN 10 THEN 'Outubro' " +
            " WHEN 11 THEN 'Novembro' " +
            " WHEN 12 THEN 'Dezembro' " +
            " END AS MES, " +
            " CASE EXTRACT(DOW FROM P.DATA_VENCIMENTO) " +
            "    WHEN 0 THEN 'domingo' " +
            "    WHEN 1 THEN 'segunda-feira' " +
            "    WHEN 2 THEN 'terça-feira' " +
            "    WHEN 3 THEN 'quarta-feira' " +
            "    WHEN 4 THEN 'quinta-feira' " +
            "    WHEN 5 THEN 'sexta-feira' " +
            "    WHEN 6 THEN 'sábado' " +
            " END AS DIA_SEMANA, " +
            " P.ID_EMPRESTIMO, " +
            " P.VALOR_VENCIMENTO, " +
            " P.NUMERO, " +
            " P.STATUS, " +
            " TP.PARCELAS, " +
            " ES.NOME NOME_ESTABELECIMENTO, " +
            " CASE WHEN CAST(CURRENT_DATE - CAST(P.DATA_VENCIMENTO AS DATE) AS INTEGER) > 0 THEN 'S' ELSE 'N' END ATRASO " +
            "FROM PARCELA P  " +
            "JOIN EMPRESTIMO E ON E.ID = P.ID_EMPRESTIMO AND E.STATUS = 2 " +
            "JOIN ESTABELECIMENTO ES ON ES.ID = E.ID_ESTABELECIMENTO " +
            "JOIN TIPO_EMPRESTIMO TP ON TP.ID = E.ID_TIPO " +
            "WHERE E.ID_USUARIO = :ID_USUARIO  " +
            "AND COALESCE(P.VALOR_PAGAMENTO, 0.0) <= 0.0  " +
            "ORDER BY  " +
            "CAST(P.DATA_VENCIMENTO AS DATE),  " +
            "P.ID_EMPRESTIMO, " +
            "P.NUMERO ", nativeQuery = true)
    Iterable<Map<String, Object>> findEmAbertoPorUsuario(@Param("ID_USUARIO") Integer ID_USUARIO);

    @Query(value = " SELECT  " +
            " P.ID, " +
            " TO_CHAR(CAST(P.DATA_VENCIMENTO AS DATE), 'DD/MM/YYYY') DATA_VENCIMENTO, " +
            " CAST(EXTRACT (DAY FROM P.DATA_VENCIMENTO) AS INTEGER) DIA, " +
            " CASE EXTRACT (MONTH FROM P.DATA_VENCIMENTO) " +
            " WHEN 1 THEN 'Janeiro' " +
            " WHEN 2 THEN 'Fevereiro' " +
            " WHEN 3 THEN 'Março' " +
            " WHEN 4 THEN 'Abril' " +
            " WHEN 5 THEN 'Maio' " +
            " WHEN 6 THEN 'Junho' " +
            " WHEN 7 THEN 'Julho' " +
            " WHEN 8 THEN 'Agosto' " +
            " WHEN 9 THEN 'Setembro' " +
            " WHEN 10 THEN 'Outubro' " +
            " WHEN 11 THEN 'Novembro' " +
            " WHEN 12 THEN 'Dezembro' " +
            " END AS MES, " +
            " CASE EXTRACT(DOW FROM P.DATA_VENCIMENTO) " +
            "    WHEN 0 THEN 'domingo' " +
            "    WHEN 1 THEN 'segunda-feira' " +
            "    WHEN 2 THEN 'terça-feira' " +
            "    WHEN 3 THEN 'quarta-feira' " +
            "    WHEN 4 THEN 'quinta-feira' " +
            "    WHEN 5 THEN 'sexta-feira' " +
            "    WHEN 6 THEN 'sábado' " +
            " END AS DIA_SEMANA, " +
            " P.ID_EMPRESTIMO, " +
            " P.VALOR_VENCIMENTO, " +
            " P.NUMERO, " +
            " P.STATUS, " +
            " TP.PARCELAS, " +
            " ES.NOME NOME_ESTABELECIMENTO, " +
            " TO_CHAR(CAST(P.DATA_PAGAMENTO AS DATE), 'DD/MM/YYYY') DATA_PAGAMENTO, " +
            " COALESCE(P.VALOR_PAGAMENTO, 0.0) VALOR_PAGAMENTO, " +
            " P.ID_USUARIO, " +
            " P.OBSERVACOES, " +
            " P.COMPROVANTE, " +
            " CASE WHEN COALESCE(P.VALOR_PAGAMENTO, 0.0) <= 0.0 THEN " +
            " CASE WHEN CAST(CURRENT_DATE - CAST(P.DATA_VENCIMENTO AS DATE) AS INTEGER) > 0 THEN 'S' ELSE 'N' END ELSE 'N' END ATRASO " +
            "FROM PARCELA P  " +
            "JOIN EMPRESTIMO E ON E.ID = P.ID_EMPRESTIMO AND E.STATUS = 2 " +
            "JOIN ESTABELECIMENTO ES ON ES.ID = E.ID_ESTABELECIMENTO " +
            "JOIN TIPO_EMPRESTIMO TP ON TP.ID = E.ID_TIPO " +
            "WHERE P.STATUS = 1  " +
            "AND COALESCE(P.VALOR_PAGAMENTO, 0.0) > 0.00 " +
            "ORDER BY P.NUMERO ", nativeQuery = true)
    Iterable<Map<String, Object>> findAguardandoAnalise();

    @Query(value = " SELECT  " +
            " P.ID, " +
            " TO_CHAR(CAST(P.DATA_VENCIMENTO AS DATE), 'DD/MM/YYYY') DATA_VENCIMENTO, " +
            " CAST(EXTRACT (DAY FROM P.DATA_VENCIMENTO) AS INTEGER) DIA, " +
            " CASE EXTRACT (MONTH FROM P.DATA_VENCIMENTO) " +
            " WHEN 1 THEN 'Janeiro' " +
            " WHEN 2 THEN 'Fevereiro' " +
            " WHEN 3 THEN 'Março' " +
            " WHEN 4 THEN 'Abril' " +
            " WHEN 5 THEN 'Maio' " +
            " WHEN 6 THEN 'Junho' " +
            " WHEN 7 THEN 'Julho' " +
            " WHEN 8 THEN 'Agosto' " +
            " WHEN 9 THEN 'Setembro' " +
            " WHEN 10 THEN 'Outubro' " +
            " WHEN 11 THEN 'Novembro' " +
            " WHEN 12 THEN 'Dezembro' " +
            " END AS MES, " +
            " CASE EXTRACT(DOW FROM P.DATA_VENCIMENTO) " +
            "    WHEN 0 THEN 'domingo' " +
            "    WHEN 1 THEN 'segunda-feira' " +
            "    WHEN 2 THEN 'terça-feira' " +
            "    WHEN 3 THEN 'quarta-feira' " +
            "    WHEN 4 THEN 'quinta-feira' " +
            "    WHEN 5 THEN 'sexta-feira' " +
            "    WHEN 6 THEN 'sábado' " +
            " END AS DIA_SEMANA, " +
            " P.ID_EMPRESTIMO, " +
            " P.VALOR_VENCIMENTO, " +
            " P.NUMERO, " +
            " P.STATUS, " +
            " TP.PARCELAS, " +
            " TO_CHAR(CAST(P.DATA_PAGAMENTO AS DATE), 'DD/MM/YYYY') DATA_PAGAMENTO, " +
            " COALESCE(P.VALOR_PAGAMENTO, 0.0) VALOR_PAGAMENTO, " +
            " P.ID_USUARIO, " +
            " P.OBSERVACOES, " +
            " P.COMPROVANTE, " +
            " ES.NOME NOME_ESTABELECIMENTO, " +
            " CASE WHEN ((COALESCE(P.VALOR_PAGAMENTO, 0.0) > 0.0) AND (P.STATUS = 2)) THEN 1 ELSE 0 END ORDENA, " +
            " CASE WHEN COALESCE(P.VALOR_PAGAMENTO, 0.0) <= 0.0 THEN " +
            " CASE WHEN CAST(CURRENT_DATE - CAST(P.DATA_VENCIMENTO AS DATE) AS INTEGER) > 0 THEN 'S' ELSE 'N' END ELSE 'N' END ATRASO " +
            "FROM PARCELA P  " +
            "JOIN EMPRESTIMO E ON E.ID = P.ID_EMPRESTIMO AND E.STATUS = 2 " +
            "JOIN ESTABELECIMENTO ES ON ES.ID = E.ID_ESTABELECIMENTO " +
            "JOIN TIPO_EMPRESTIMO TP ON TP.ID = E.ID_TIPO " +
            "WHERE E.ID = :ID_EMPRESTIMO  " +
            "ORDER BY ORDENA, P.NUMERO ", nativeQuery = true)
    Iterable<Map<String, Object>> findPorEmprestimo(@Param("ID_EMPRESTIMO") Integer ID_EMPRESTIMO);

    @Query(value = "SELECT SUM(P.VALOR_PAGAMENTO) FROM PARCELA P " +
            " JOIN EMPRESTIMO E ON E.ID = P.ID_EMPRESTIMO AND E.STATUS = 2 " +
            " WHERE P.STATUS = 1 AND COALESCE(P.VALOR_PAGAMENTO, 0.0) > 0.00 ", nativeQuery = true)
    Double findTotalParcelasAguardandoAnalise();

    @Query(value = "SELECT SUM(P.VALOR_VENCIMENTO) FROM PARCELA P " +
            " JOIN EMPRESTIMO E ON E.ID = P.ID_EMPRESTIMO AND E.STATUS = 2 " +
            " WHERE CAST(P.DATA_VENCIMENTO AS DATE) < CURRENT_DATE AND COALESCE(P.VALOR_PAGAMENTO, 0.0) <= 0.00 ", nativeQuery = true)
    Double findTotalParcelasEmAtraso();

    @Query(value = "SELECT SUM(P.VALOR_VENCIMENTO) FROM PARCELA P " +
            " JOIN EMPRESTIMO E ON E.ID = P.ID_EMPRESTIMO AND E.STATUS = 2 " +
            " WHERE CAST(P.DATA_VENCIMENTO AS DATE) >= CURRENT_DATE AND COALESCE(P.VALOR_PAGAMENTO, 0.0) <= 0.00 ", nativeQuery = true)
    Double findTotalParcelasAVencer();

    @Query(value = " SELECT COUNT(P.VALOR_VENCIMENTO), P.ID_EMPRESTIMO, E.ID_USUARIO_ANALISE, U.NOME FROM PARCELA P " +
            " JOIN EMPRESTIMO E ON E.ID = P.ID_EMPRESTIMO AND E.STATUS = 2 " +
            " JOIN USUARIO U ON U.ID = E.ID_USUARIO_ANALISE " +
            " WHERE CAST(P.DATA_VENCIMENTO AS DATE) < CURRENT_DATE AND COALESCE(P.VALOR_PAGAMENTO, 0.0) <= 0.00 " +
            " GROUP BY P.ID_EMPRESTIMO, E.ID_USUARIO_ANALISE, U.NOME ", nativeQuery = true)
    Double findQuantidadeParcelasEmAtrasoPorEmprestimo();

    @Query(value = " SELECT COUNT(*) FROM PARCELA " +
            " WHERE ID_EMPRESTIMO = :ID_EMPRESTIMO " +
            " AND NUMERO < :NUMERO " +
            " AND VALOR_PAGAMENTO <= 0.0 " , nativeQuery = true)
    Integer totalizaParcelaAnteriorEmAberto(@Param("ID_EMPRESTIMO") Integer ID_EMPRESTIMO,
                                            @Param("NUMERO") Integer NUMERO);


}
