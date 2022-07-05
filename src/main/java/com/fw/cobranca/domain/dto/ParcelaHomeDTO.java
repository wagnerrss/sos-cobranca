package com.fw.cobranca.domain.dto;

import com.fw.cobranca.domain.Parcela;
import com.fw.cobranca.util.Util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class ParcelaHomeDTO implements Util {
    public Integer id;
    public String dataVencimento;
    public Integer idEmprestimo;
    public Double valorVencimento;
    public Integer numero;
    public Integer parcelas;
    public String atraso;
    public String dataPagamento;
    public Double valorPagamento;
    public Integer idUsuario;
    public String observacoes;
    public String comprovante;
    public String nomeEstabelecimento;
    public Integer status;

    public ParcelaHomeDTO(Map<String, Object> map) {
        this.id = toInt(map.get("id"));
        this.dataVencimento = toStr(map.get("data_vencimento"));
        this.idEmprestimo = toInt(map.get("id_emprestimo"));
        this.valorVencimento = toDouble(map.get("valor_vencimento"));
        this.numero = toInt(map.get("numero"));
        this.parcelas = toInt(map.get("parcelas"));
        this.atraso = toStr(map.get("atraso"));
        this.dataPagamento = toStr(map.get("data_pagamento"));
        this.valorPagamento = toDouble(map.get("valor_pagamento"));
        this.idUsuario = toInt(map.get("id_usuario"));
        this.observacoes = toStr(map.get("observacoes"));
        this.comprovante = toStr(map.get("comprovante"));
        this.nomeEstabelecimento = toStr(map.get("nome_estabelecimento"));
        this.status = toInt(map.get("status"));
    }

    public ParcelaHomeDTO(Parcela p, Integer qtdParcelas) {
        this.id = p.getId();
        this.dataVencimento = toStr(p.getDataVencimento());
        this.idEmprestimo = p.getIdEmprestimo();
        this.valorVencimento = p.getValorVencimento();
        this.numero = p.getNumero();
        this.parcelas = qtdParcelas;
        this.dataPagamento = toStr(p.getDataPagamento());
        this.valorPagamento = p.getValorPagamento() == null ? 0d : p.getValorPagamento();
        this.idUsuario = p.getIdUsuario();
        this.observacoes = p.getObservacoes();
        this.comprovante = p.getComprovante();
        this.nomeEstabelecimento = "";
        this.status = p.getStatus();

        // System.out.println(this.dataVencimento.substring(0, 10));
        LocalDate _dataVencimento = LocalDate.parse(this.dataVencimento.substring(0, 10), DateTimeFormatter.ISO_LOCAL_DATE);

        long diff = ChronoUnit.DAYS.between(_dataVencimento, LocalDate.now());

        this.atraso = this.valorPagamento > 0 ? "N" : (diff > 0 ? "S" : "N");
    }
}
