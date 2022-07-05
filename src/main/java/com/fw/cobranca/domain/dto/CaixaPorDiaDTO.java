package com.fw.cobranca.domain.dto;


import com.fw.cobranca.domain.Caixa;
import com.fw.cobranca.util.Util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CaixaPorDiaDTO implements Util {
    public String dataMovimento;
    public String diaSemana;
    public String dia;
    public String mes;
    public List<Caixa> caixas;

    public CaixaPorDiaDTO(Map<String, Object> map) {
        this.dataMovimento = toStr(map.get("data_movimento"));
        this.diaSemana = toStr(map.get("dia_semana"));
        this.dia = toStr(map.get("dia"));
        this.mes = toStr(map.get("mes"));
        this.caixas = ((List<Map<String, Object>>) (map.get("caixas"))).stream().map(Caixa::new).collect(Collectors.toList());
    }

    public CaixaPorDiaDTO() {

    }
}
