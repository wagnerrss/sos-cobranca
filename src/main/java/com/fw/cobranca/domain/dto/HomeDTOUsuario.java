package com.fw.cobranca.domain.dto;

import com.fw.cobranca.domain.Emprestimo;
import com.fw.cobranca.domain.Estabelecimento;
import com.fw.cobranca.domain.TipoEmprestimo;
import com.fw.cobranca.util.Util;

public class HomeDTOUsuario implements Util {
    public Integer idUsuario;
    public boolean temSaldo;
    public Double saldo;
    public Iterable<Estabelecimento> estabelecimentos;
    public Iterable<Emprestimo> emprestimos;
    public Iterable<ParcelaPorDiaHomeDTO> parcelas;
    public Double valorParcelasEmAberto;
    public Iterable<TipoEmprestimo> tipoEmprestimos;
    public Integer quantidadeNotificacaoNaoLida;
}
