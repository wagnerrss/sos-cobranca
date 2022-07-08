package com.fw.cobranca.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fw.cobranca.domain.Regiao;
import com.fw.cobranca.util.Util;

import java.text.ParseException;
import java.util.List;
import java.util.Map;


public class RegiaoDTO implements Util {
    public Integer id;
    public String regiao;
    public List<Integer> usuarios;
    @JsonIgnore
    public Integer ordena;

    public RegiaoDTO(Map<String, Object> map) throws ParseException {
        this.id = toInt(map.get("id"));
        this.regiao = toStr(map.get("regiao"));
        this.usuarios = (List) map.get("usuarios");
    }

    public RegiaoDTO(Regiao regiao){
        this.id = regiao.getId();
        this.regiao = regiao.getRegiao();
    }

    public RegiaoDTO(RegiaoDTO regiaoDTO) {
        this.id = regiaoDTO.id;
        this.regiao = regiaoDTO.regiao;
        this.usuarios = regiaoDTO.usuarios;
    }

}
