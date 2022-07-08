package com.fw.cobranca.repository;

import com.fw.cobranca.domain.Caixa;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Map;

public interface CaixaRepository extends CrudRepository<Caixa, Integer> {
    Iterable<Caixa> findByIdUsuario(Integer id_Usuario);

    @Query(value = " SELECT C.* FROM CAIXA C \n" +
            "JOIN USUARIO U ON U.ID = C.ID_USUARIO AND U.TIPO = 'A' ", nativeQuery = true)
    Iterable<Caixa> findByUsuarioAdm();

    @Query(value = " SELECT SUM(COALESCE(CASE WHEN TIPO = 'D' THEN -VALOR_MOVIMENTO ELSE VALOR_MOVIMENTO END, 0.0)) FROM CAIXA WHERE ID_PARCELA = :ID_PARCELA ", nativeQuery = true)
    Double findByParcela(@Param("ID_PARCELA") Integer ID_PARCELA);

    String sqlCaixaPorDia = " SELECT " +
            " C.ID, " +
            " TO_CHAR(CAST(C.DATA_MOVIMENTO AS DATE), 'DD/MM/YYYY') DATA_MOVIMENTO, " +
            " CAST(EXTRACT (DAY FROM C.DATA_MOVIMENTO) AS INTEGER) DIA, " +
            " CASE EXTRACT (MONTH FROM C.DATA_MOVIMENTO) " +
            "    WHEN 1 THEN 'Janeiro' " +
            "    WHEN 2 THEN 'Fevereiro' " +
            "    WHEN 3 THEN 'Março' " +
            "    WHEN 4 THEN 'Abril' " +
            "    WHEN 5 THEN 'Maio' " +
            "    WHEN 6 THEN 'Junho' " +
            "    WHEN 7 THEN 'Julho' " +
            "    WHEN 8 THEN 'Agosto' " +
            "    WHEN 9 THEN 'Setembro' " +
            "    WHEN 10 THEN 'Outubro' " +
            "    WHEN 11 THEN 'Novembro' " +
            "    WHEN 12 THEN 'Dezembro' " +
            " END AS MES, " +
            " CASE EXTRACT(DOW FROM C.DATA_MOVIMENTO) " +
            "    WHEN 0 THEN 'domingo' " +
            "    WHEN 1 THEN 'segunda-feira' " +
            "    WHEN 2 THEN 'terça-feira' " +
            "    WHEN 3 THEN 'quarta-feira' " +
            "    WHEN 4 THEN 'quinta-feira' " +
            "    WHEN 5 THEN 'sexta-feira' " +
            "    WHEN 6 THEN 'sábado' " +
            " END AS DIA_SEMANA, " +
            " C.ID_USUARIO, " +
            " C.VALOR_MOVIMENTO, " +
            " C.TIPO, " +
            " C.ID_PARCELA, " +
            " C.ORIGEM, " +
            " C.OBSERVACOES " +
            " FROM CAIXA C ";

    @Query(value = sqlCaixaPorDia +
            " WHERE CAST(C.DATA_MOVIMENTO AS DATE) BETWEEN (CURRENT_DATE - :DIAS) AND (CURRENT_DATE) " +
            " ORDER BY C.DATA_MOVIMENTO DESC ", nativeQuery = true)
    Iterable<Map<String, Object>> findPorDia(@Param("DIAS") Integer DIAS);

    @Query(value = sqlCaixaPorDia +
            " WHERE CAST(C.DATA_MOVIMENTO AS DATE) BETWEEN (CURRENT_DATE - :DIAS) AND (CURRENT_DATE) " +
            " AND C.ID_USUARIO = :ID_USUARIO " +
            " ORDER BY C.DATA_MOVIMENTO DESC ", nativeQuery = true)
    Iterable<Map<String, Object>> findPorDiaUsuario(@Param("DIAS") Integer DIAS,
                                                    @Param("ID_USUARIO") Integer ID_USUARIO);
}
