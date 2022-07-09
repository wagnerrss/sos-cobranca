package com.fw.cobranca.service;


import com.fw.cobranca.domain.Emprestimo;
import com.fw.cobranca.domain.Usuario;
import com.fw.cobranca.domain.dto.HomeDTOAdministrador;
import com.fw.cobranca.domain.dto.HomeDTOColaborador;
import com.fw.cobranca.domain.dto.HomeDTOUsuario;
import com.fw.cobranca.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

@Service
public class HomeService implements Util {
    @Autowired
    private EstabelecimentoService estabelecimentoService;

    @Autowired
    private EmprestimoService emprestimoService;

    @Autowired
    private ParcelaService parcelaService;

    @Autowired
    private TipoEmprestimoService tipoEmprestimoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private NotificacaoService notificacaoService;

    @Autowired
    private CaixaService caixaService;

    @Autowired
    private VersaoService versaoService;

    public Object getDadosHome(Integer id_usuario) throws Exception {
        Optional<Usuario> usuario = usuarioService.getById(id_usuario);
        if (!usuario.isPresent()) {
            throw new Exception("Usuário não encontrado!");
        }

        switch (usuario.get().getTipo()) {

            //Administrador
            case "A": {
                HomeDTOAdministrador homeDTOAdministrador = new HomeDTOAdministrador();

                homeDTOAdministrador.idUsuario = id_usuario;

                homeDTOAdministrador.temSaldo = true;
                homeDTOAdministrador.saldo = caixaService.totalizaSaldoAdm();

                homeDTOAdministrador.totalEmprestimosSolicitado = toDouble(emprestimoService.totalEmprestimosSolicitado());
                homeDTOAdministrador.totalParcelasEmAtraso = toDouble(parcelaService.getTotalParcelasEmAtraso());
                homeDTOAdministrador.totalParcelasAVencer = toDouble(parcelaService.getTotalParcelasAVencer());
                homeDTOAdministrador.totalParcelasAguardandoAnalise = toDouble(parcelaService.getTotalAguardandoAnalise());

                homeDTOAdministrador.parcelasAguardandoAnalise = parcelaService.getAguardandoAnalise();

                homeDTOAdministrador.quantidadeNotificacaoNaoLida = notificacaoService.getQuantidadeNotificacaoNaoLidaPorUsuario(id_usuario);

                homeDTOAdministrador.emprestimosAguardandoAnalise = emprestimoService.getEmprestimosPorStatus(0);
                homeDTOAdministrador.emprestimosEmAtraso = new ArrayList<>();
                homeDTOAdministrador.emprestimosAVencer = new ArrayList<>();

                Iterable<Emprestimo> emprestimos = emprestimoService.getEmprestimos(0);
                emprestimos.forEach(emprestimo -> {

                    if (emprestimo.getQuantidadeParcelasAtraso() > 0) {
                        homeDTOAdministrador.emprestimosEmAtraso.add(emprestimo);
                    }

                    if (emprestimo.getQuantidadeParcelasAVencer() > 0) {
                        homeDTOAdministrador.emprestimosAVencer.add(emprestimo);
                    }

                });

                homeDTOAdministrador.versao = versaoService.getVersao();

                return homeDTOAdministrador;
            }

            //Colaborador
            case "C": {
                HomeDTOColaborador homeDTOColaborador = new HomeDTOColaborador();

                homeDTOColaborador.idUsuario = id_usuario;

                homeDTOColaborador.temSaldo = true;
                homeDTOColaborador.saldo = caixaService.totalizaSaldo(id_usuario);

                homeDTOColaborador.totalEmprestimosAguardandoAnalise = 0.0;
                homeDTOColaborador.totalEmprestimosEmAnalise = 0.0;
                homeDTOColaborador.totalParcelasEmAtraso = 0.0;
                homeDTOColaborador.totalParcelasAVencer = 0.0;

                homeDTOColaborador.emprestimosAguardandoAnalise = emprestimoService.getEmprestimosPorStatusEIdUsuarioAnalise(0, id_usuario);
                homeDTOColaborador.emprestimosAguardandoAnalise.forEach(emprestimo -> {
                    homeDTOColaborador.totalEmprestimosAguardandoAnalise += emprestimo.getValorSolicitado();
                });

                homeDTOColaborador.emprestimosEmAnalise = emprestimoService.getEmprestimosPorStatusEIdUsuarioAnalise(1, id_usuario);
                homeDTOColaborador.emprestimosEmAnalise.forEach(emprestimo -> {
                    homeDTOColaborador.totalEmprestimosEmAnalise += emprestimo.getValorSolicitado();
                });

                homeDTOColaborador.quantidadeNotificacaoNaoLida = notificacaoService.getQuantidadeNotificacaoNaoLidaPorUsuario(id_usuario);

                homeDTOColaborador.emprestimosEmAtraso = new ArrayList<>();
                homeDTOColaborador.emprestimosAVencer = new ArrayList<>();

                Iterable<Emprestimo> emprestimos = emprestimoService.getEmprestimos(id_usuario);
                emprestimos.forEach(emprestimo -> {

                    if (emprestimo.getQuantidadeParcelasAtraso() > 0) {
                        homeDTOColaborador.emprestimosEmAtraso.add(emprestimo);
                        homeDTOColaborador.totalParcelasEmAtraso += emprestimo.getQuantidadeParcelasAtraso() * (emprestimo.getValorAprovado() / emprestimo.getQuantidadeParcelas());
                    }

                    if (emprestimo.getQuantidadeParcelasAVencer() > 0) {
                        homeDTOColaborador.emprestimosAVencer.add(emprestimo);
                        homeDTOColaborador.totalParcelasAVencer += emprestimo.getQuantidadeParcelasAVencer() * (emprestimo.getValorAprovado() / emprestimo.getQuantidadeParcelas());
                    }
                });

                homeDTOColaborador.versao = versaoService.getVersao();

                return homeDTOColaborador;
            }

            //Usuário
            default: {
                HomeDTOUsuario homeDTOUsuario = new HomeDTOUsuario();

                homeDTOUsuario.idUsuario = id_usuario;

                homeDTOUsuario.temSaldo = false;
                homeDTOUsuario.saldo = 0.00;

                homeDTOUsuario.estabelecimentos = estabelecimentoService.getByIdUsuario(id_usuario);
                homeDTOUsuario.emprestimos = emprestimoService.getEmprestimosPorIdUsuario(id_usuario);

                Iterable<Map> emprestimosEmAberto = emprestimoService.getEmprestimosPorIdUsuarioEmAberto(id_usuario);

                homeDTOUsuario.valorParcelasEmAberto = 0.00;
                homeDTOUsuario.parcelas = new ArrayList<>();
                emprestimosEmAberto.forEach(emprestimo -> {
                    homeDTOUsuario.parcelas = parcelaService.getPorEmprestimo(toInt(emprestimo.get("id")));

                    homeDTOUsuario.parcelas.forEach(parcelas -> {
                        parcelas.parcelas.forEach(parcela -> {
                            if (parcela.valorPagamento <= 0.0) {
                                homeDTOUsuario.valorParcelasEmAberto += parcela.valorVencimento;
                            }
                        });
                    });
                });

                homeDTOUsuario.tipoEmprestimos = tipoEmprestimoService.getTipoEmprestimosPorTipo(0);

                homeDTOUsuario.quantidadeNotificacaoNaoLida = notificacaoService.getQuantidadeNotificacaoNaoLidaPorUsuario(id_usuario);

                homeDTOUsuario.versao = versaoService.getVersao();

                return homeDTOUsuario;
            }
        }
    }
}
