package com.fw.cobranca.domain;

import com.fw.cobranca.util.Util;
import lombok.Data;

import javax.persistence.*;
import java.text.ParseException;
import java.util.Map;

@Entity
@Table(name = "usuario")
@Data
public class Usuario implements Util {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String foto;
    private String tipo; //consultor, administrador, cliente
    private String documento;
    private String nome;
    private String fone;
    private String senha;
    private String status;

    public Usuario() {
    }

    public Usuario(Map map) throws ParseException {
        this.id = toInt(map.get("id"));
        this.foto = toStr(map.get("foto"));
        this.tipo = toStr(map.get("tipo"));
        this.documento = toStr(map.get("documento"));
        this.nome = toStr(map.get("nome"));
        this.fone = toStr(map.get("fone"));
        this.senha = toStr(map.get("senha"));
        this.status = toStr(map.get("status"));
    }
}
