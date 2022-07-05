package com.fw.cobranca.domain;

import com.fw.cobranca.util.Util;
import lombok.Data;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table(name = "regiao")
@Data
public class Regiao implements Util {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String regiao;

    public Regiao() {
    }

    public Regiao(Integer id, String regiao) {
        this.id = id;
        this.regiao = regiao;
    }

    public Regiao(Map map) {
        this.id = toInt(map.get("id"));
        this.regiao = toStr(map.get("regiao"));
    }
}
