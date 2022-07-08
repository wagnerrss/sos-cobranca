package com.fw.cobranca.domain.dto;

import com.fw.cobranca.domain.Emprestimo;
import com.fw.cobranca.domain.Parcela;
import com.fw.cobranca.util.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


public class EmprestimoDTO implements Util {
    public Integer id;
    public Integer idTipo;
    public Integer idUsuario;
    public Integer idEstabelecimento;
    public Date dataSolicitacao;
    public Double valorSolicitado;
    public Integer status;
    public Date dataAprovacao;
    public Double valorAprovado;
    public Integer idUsuarioAnalise;
    public String observacoes;
    public Iterable<Parcela> parcelas;

    public EmprestimoDTO(Map<String, Object> map) throws ParseException {
        this.id = toInt(map.get("id"));
        this.idTipo = toInt(map.get("id_tipo"));
        this.idUsuario = toInt(map.get("id_usuario"));
        this.idEstabelecimento = toInt(map.get("id_estabelecimento"));
        this.dataSolicitacao = new SimpleDateFormat("yyyy-MM-dd").parse(toStr(map.get("data_solicitacao")));
        this.valorSolicitado = toDouble(map.get("valor_solicitado"));
        this.status = toInt(map.get("status"));
        this.dataAprovacao =toStr(map.get("data_aprovacao")).equals("") ? null : new SimpleDateFormat("yyyy-MM-dd").parse(toStr(map.get("data_aprovacao")));
        this.valorAprovado = toDouble(map.get("valor_aprovado"));
        this.idUsuarioAnalise = toInt(map.get("id_usuario_analise"));
        this.observacoes = toStr(map.get("observacoes"));
    }

    public EmprestimoDTO(Emprestimo emprestimo) {
        this.id = emprestimo.getId();
        this.idTipo = emprestimo.getIdTipo();
        this.idUsuario = emprestimo.getIdUsuario();
        this.idEstabelecimento = emprestimo.getIdEstabelecimento();
        this.dataSolicitacao = emprestimo.getDataSolicitacao();
        this.valorSolicitado = emprestimo.getValorSolicitado();
        this.status = emprestimo.getStatus();
        this.dataAprovacao = emprestimo.getDataAprovacao();
        this.valorAprovado = emprestimo.getValorAprovado();
        this.idUsuarioAnalise = emprestimo.getIdUsuarioAnalise();
        this.observacoes = emprestimo.getObservacoes();
    };
}
