package com.fw.cobranca.domain;

import com.fw.cobranca.util.Util;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


@Entity
@Table(name = "emprestimo")
@Data
@NoArgsConstructor
public class Emprestimo implements Util {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer idTipo;
    private Integer idUsuario;
    @Transient
    private Usuario usuario;
    private Integer idEstabelecimento;
    @Transient
    private Estabelecimento estabelecimento;
    private Date dataSolicitacao;
    private Double valorSolicitado;
    private Integer status;
    private Date dataAprovacao;
    private Double valorAprovado;
    private Integer idUsuarioAnalise;
    private String observacoes;
    private Integer tipo;
    private String documento;
    private Integer quantidadeParcelas;
    @Transient
    private Integer quantidadeParcelasAtraso;
    @Transient
    private Integer quantidadeParcelasAVencer;
    @Transient
    private String nomeUsuarioAnalise;

    public Emprestimo(Map map) {
        this.id = toInt(map.get("id"));
        this.idTipo = toInt(map.get("id_tipo"));
        this.idUsuario = toInt(map.get("id_usuario"));
        this.idEstabelecimento = toInt(map.get("id_estabelecimento"));
        this.valorSolicitado = toDouble(map.get("valor_solicitado"));
        this.status = toInt(map.get("status"));
        this.valorAprovado = toDouble(map.get("valor_aprovado"));
        this.idUsuarioAnalise = toInt(map.get("id_usuario_analise"));
        this.observacoes = toStr(map.get("observacoes"));
        this.tipo = toInt(map.get("tipo"));
        this.documento = toStr(map.get("documento"));
        this.quantidadeParcelas = toInt(map.get("quantidade_parcelas"));
        this.quantidadeParcelasAtraso = map.get("quantidade_parcelas_atraso") == null ? 0 : toInt(map.get("quantidade_parcelas_atraso"));
        this.quantidadeParcelasAVencer = map.get("quantidade_parcelas_avencer") == null ? 0 : toInt(map.get("quantidade_parcelas_avencer"));
        this.nomeUsuarioAnalise = map.get("nome_usuario_analise") == null ? "" : toStr(map.get("nome_usuario_analise"));

        try {
            this.dataSolicitacao = toStr(map.get("data_solicitacao")).equals("") ? null : new SimpleDateFormat("yyyy-MM-dd").parse(toStr(map.get("data_solicitacao")));
            this.dataAprovacao = toStr(map.get("data_aprovacao")).equals("") ? null : new SimpleDateFormat("yyyy-MM-dd").parse(toStr(map.get("data_aprovacao")));
        } catch (Exception exception){
            this.dataSolicitacao = null;
            this.dataAprovacao = null;
        }
    }
}
