package com.fw.cobranca.domain;

import com.fw.cobranca.util.Util;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

@Entity
@Table(name = "notificacao")
@Data
@NoArgsConstructor
public class Notificacao implements Util {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer idUsuario;
    private Date dataNotificacao;
    private String tipo;
    private String lida;
    private String mensagem;

    public Notificacao(Map map) {
        this.id = toInt(map.get("id"));
        this.idUsuario = map.containsKey("idUsuario") ? toInt(map.get("idUsuario")) : toInt(map.get("id_usuario"));
        this.dataNotificacao = map.containsKey("dataNotificacao") ? toDate(map.get("dataNotificacao")) : toDate(map.get("data_notificacao"));
        this.tipo = toStr(map.get("tipo"));
        this.lida = toStr(map.get("lida"));
        this.mensagem =  toStr(map.get("mensagem"));
    }

    public Notificacao(Integer id, Integer idUsuario, Date dataNotificacao, String tipo, String lida, String mensagem) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.dataNotificacao = dataNotificacao;
        this.tipo = tipo;
        this.lida = lida;
        this.mensagem = mensagem;
    }
}
