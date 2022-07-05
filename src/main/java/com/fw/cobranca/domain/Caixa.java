package com.fw.cobranca.domain;

import com.fw.cobranca.util.Util;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

@Entity
@Table(name = "caixa")
@Data
@NoArgsConstructor
public class Caixa implements Util {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer idUsuario;
    private Integer idParcela;
    private Date dataMovimento;
    private Double valorMovimento;
    private String tipo; //C - Crédito, D - Débito
    private String origem; //RP - Recebimento Parcela, DE - Despesa, etc...
    private String observacoes;

    public Caixa(Map map) {
        this.id = toInt(map.get("id"));
        this.idUsuario = map.containsKey("idUsuario") ? toInt(map.get("idUsuario")) : toInt(map.get("id_usuario"));
        this.idParcela =  map.containsKey("idParcela") ? toInt(map.get("idParcela")) : toInt(map.get("id_parcela"));
        this.dataMovimento =  map.containsKey("dataMovimento") ? toStr(map.get("dataMovimento")).equals("") ? null : toDate(map.get("dataMovimento")) :  toDate(map.get("data_movimento"));
        this.valorMovimento =  map.containsKey("valorMovimento") ? toDouble(map.get("valorMovimento")) : toDouble(map.get("valor_movimento"));
        this.tipo = toStr(map.get("tipo"));
        this.origem = toStr(map.get("origem"));
        this.observacoes = toStr(map.get("observacoes"));
    }

}
