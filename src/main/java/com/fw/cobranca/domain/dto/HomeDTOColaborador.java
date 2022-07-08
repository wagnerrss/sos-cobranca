package com.fw.cobranca.domain.dto;

import com.fw.cobranca.domain.Emprestimo;
import com.fw.cobranca.domain.Estabelecimento;
import com.fw.cobranca.domain.TipoEmprestimo;
import com.fw.cobranca.util.Util;

import java.util.List;

public class HomeDTOColaborador implements Util {
    public Integer idUsuario;
    public boolean temSaldo;
    public Double saldo;

    public Iterable<Emprestimo> emprestimosAguardandoAnalise;
    public List<Emprestimo> emprestimosEmAtraso;
    public List<Emprestimo> emprestimosAVencer;
}
