package com.fw.cobranca.domain.dto;

import com.fw.cobranca.domain.Emprestimo;
import com.fw.cobranca.util.Util;

import java.util.List;

public class HomeDTOColaborador implements Util {
    public Integer idUsuario;
    public boolean temSaldo;
    public Double saldo;

    public Iterable<Emprestimo> emprestimosAguardandoAnalise;
    public Iterable<Emprestimo> emprestimosEmAnalise;
    public List<Emprestimo> emprestimosEmAtraso;
    public List<Emprestimo> emprestimosAVencer;
    public Integer quantidadeNotificacaoNaoLida;

    public Double totalEmprestimosAguardandoAnalise;
    public Double totalEmprestimosEmAnalise;
    public Double totalParcelasEmAtraso;
    public Double totalParcelasAVencer;
}
