package com.fw.cobranca.domain;

import com.fw.cobranca.util.Util;
import lombok.Data;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Entity
@Table(name = "parcela")
@Data
public class Parcela implements Util {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer idEmprestimo;
    private Integer numero;
    private Date dataVencimento;
    private Double valorVencimento;
    private Date dataPagamento;
    private Double valorPagamento;
    private Integer status; //1 em analise, 2 aprovado
    private String observacoes;
    private String comprovante;
    private Integer idUsuario;

    public Parcela() {
    }

    public Parcela(Integer id, Integer idEmprestimo, Integer numero, Date dataVencimento, Double valorVencimento, Date dataPagamento, Double valorPagamento, Integer status, String observacoes, String comprovante, Integer idUsuario) {
        this.id = id;
        this.idEmprestimo = idEmprestimo;
        this.numero = numero;
        this.dataVencimento = dataVencimento;
        this.valorVencimento = valorVencimento;
        this.dataPagamento = dataPagamento;
        this.valorPagamento = valorPagamento;
        this.status = status;
        this.observacoes = observacoes;
        this.comprovante = comprovante;
        this.idUsuario = idUsuario;
    }

    public Parcela(Map map) throws ParseException {
        this.id =  toInt(map.get("id"));
        this.idEmprestimo = toInt(map.get("id_emprestimo"));
        this.numero = toInt(map.get("numero"));
        this.dataVencimento =  toStr(map.get("data_vencimento")).equals("") ? null : toDate(map.get("data_vencimento"));
        this.valorVencimento = toDouble(map.get("valor_vencimento"));
        this.dataPagamento = toStr(map.get("data_pagamento")).equals("") ? null : toDate(map.get("data_pagamento"));
        this.valorPagamento = toDouble(map.get("valor_pagamento"));
        this.status = toInt(map.get("status"));
        this.observacoes = toStr(map.get("observacoes"));
        this.comprovante = toStr(map.get("comprovante"));
        this.idUsuario = toInt(map.get("id_usuario"));
    }

}
