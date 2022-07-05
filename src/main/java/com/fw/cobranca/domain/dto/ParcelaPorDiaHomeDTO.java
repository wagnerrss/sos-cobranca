package com.fw.cobranca.domain.dto;


import com.fw.cobranca.util.Util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ParcelaPorDiaHomeDTO implements Util {
    public String dataVencimento;
    public String diaSemana;
    public String dia;
    public String mes;
    public List<ParcelaHomeDTO> parcelas;

    public ParcelaPorDiaHomeDTO(Map<String, Object> map) {
        this.dataVencimento = toStr(map.get("data_vencimento"));
        this.diaSemana = toStr(map.get("dia_semana"));
        this.dia = toStr(map.get("dia"));
        this.mes = toStr(map.get("mes"));
        this.parcelas = ((List<Map<String, Object>>) (map.get("parcelas"))).stream().map(ParcelaHomeDTO::new).collect(Collectors.toList());
    }

    public ParcelaPorDiaHomeDTO() {

    }
}
