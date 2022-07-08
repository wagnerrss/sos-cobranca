package com.fw.cobranca.domain;

import com.fw.cobranca.util.Util;
import lombok.Data;
import javax.persistence.*;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "estabelecimento")
@Data
public class Estabelecimento implements Util {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer idUsuario;
    private String cnpj;
    private String nome;
    private String endereco;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String fone;
    private String comprovante;
    private String observacoes;

    public Estabelecimento() {
    }

    public Estabelecimento(Map map) throws ParseException {
        this.id = toInt(map.get("id"));
        this.idUsuario = toInt(map.get("idUsuario"));
        this.cnpj = toStr(map.get("cnpj"));
        this.nome = toStr(map.get("nome"));
        this.endereco = toStr(map.get("endereco"));
        this.numero = toStr(map.get("numero"));
        this.complemento = toStr(map.get("complemento"));
        this.bairro = toStr(map.get("bairro"));
        this.cidade = toStr(map.get("cidade"));
        this.estado = toStr(map.get("estado"));
        this.cep = toStr(map.get("cep"));
        this.fone = toStr(map.get("fone"));
        this.comprovante = toStr(map.get("comprovante"));
        this.observacoes = toStr(map.get("observacoes"));
    }
}
