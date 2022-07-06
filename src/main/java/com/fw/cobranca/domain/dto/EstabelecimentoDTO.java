package com.fw.cobranca.domain.dto;

import com.fw.cobranca.domain.Emprestimo;
import com.fw.cobranca.domain.Estabelecimento;
import com.fw.cobranca.util.Util;
import lombok.Data;
import org.aspectj.weaver.AjAttribute;

import javax.persistence.*;
import java.util.Map;

public class EstabelecimentoDTO implements Util {

    public Integer id;
    public Integer idUsuario;
    public String cnpj;
    public String nome;
    public String endereco;
    public String numero;
    public String complemento;
    public String bairro;
    public String cidade;
    public String estado;
    public String cep;
    public String fone;
    public String comprovante;
    public String observacoes;
    public Iterable<EmprestimoDTO> emprestimos;

    public EstabelecimentoDTO(Estabelecimento estabelecimento) {
        this.id = estabelecimento.getId();
        this.idUsuario = estabelecimento.getIdUsuario();
        this.cnpj = estabelecimento.getCnpj();
        this.nome = estabelecimento.getNome();
        this.endereco = estabelecimento.getEndereco();
        this.numero = estabelecimento.getNumero();
        this.complemento = estabelecimento.getComplemento();
        this.bairro = estabelecimento.getBairro();
        this.cidade = estabelecimento.getCidade();
        this.estado = estabelecimento.getEstado();
        this.cep = estabelecimento.getCep();
        this.fone = estabelecimento.getFone();
        this.comprovante = estabelecimento.getComprovante();
        this.observacoes = estabelecimento.getObservacoes();
    }

    public EstabelecimentoDTO(Map<String, Object> map) {
        this.id = toInt(map.get("id"));
        this.idUsuario = toInt(map.get("id_usuario"));
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
