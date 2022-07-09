package com.fw.cobranca.domain.dto;

import com.fw.cobranca.domain.Emprestimo;
import com.fw.cobranca.domain.Versao;
import com.fw.cobranca.util.Util;

import java.util.List;

public class HomeDTOAdministrador implements Util {
    public Integer idUsuario;
    public boolean temSaldo;
    public Double saldo;
    public Double totalEmprestimosSolicitado;
    public Double totalParcelasEmAtraso;
    public Double totalParcelasAVencer;
    public Double totalParcelasAguardandoAnalise;
    public Iterable<Emprestimo> emprestimosAguardandoAnalise;
    public List<Emprestimo> emprestimosEmAtraso;
    public List<Emprestimo> emprestimosAVencer;
    public Iterable<ParcelaHomeDTO> parcelasAguardandoAnalise;
    public Integer quantidadeNotificacaoNaoLida;

    public Versao versao;

}
