package com.fw.cobranca.domain;

import com.fw.cobranca.util.Util;
import lombok.Data;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table(name = "colaborador_regiao")
@Data
public class ColaboradorRegiao implements Util {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer idUsuario;
    private Integer idRegiao;

    public ColaboradorRegiao() {
    }

    public ColaboradorRegiao(Integer id, Integer idUsuario, Integer idRegiao) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idRegiao = idRegiao;
    }

    public ColaboradorRegiao(Map map) {
        this.id = toInt(map.get("id"));
        this.idUsuario = toInt(map.get("id_usuario"));
        this.idRegiao = toInt(map.get("id_regiao"));
    }
}
