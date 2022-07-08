package com.fw.cobranca.service;


import com.fw.cobranca.domain.Emprestimo;
import com.fw.cobranca.domain.dto.HomeDTOAdministrador;
import com.fw.cobranca.domain.dto.HomeDTOColaborador;
import com.fw.cobranca.domain.dto.HomeDTOUsuario;
import com.fw.cobranca.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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

    public Object getDadosHome(Integer id_usuario) {
        switch (usuarioService.getById(id_usuario).get().getTipo()) {
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

                return homeDTOAdministrador;
            }
            //Colaborador
            case "C": {
                HomeDTOColaborador homeDTOColaborador = new HomeDTOColaborador();

                homeDTOColaborador.idUsuario = id_usuario;

                homeDTOColaborador.temSaldo = true;
                homeDTOColaborador.saldo = caixaService.totalizaSaldo(id_usuario);

                homeDTOColaborador.emprestimosAguardandoAnalise = emprestimoService.getEmprestimosPorStatusEIdUsuarioAnalise(0, id_usuario);

                homeDTOColaborador.emprestimosEmAtraso = new ArrayList<>();
                homeDTOColaborador.emprestimosAVencer = new ArrayList<>();

                Iterable<Emprestimo> emprestimos = emprestimoService.getEmprestimos(id_usuario);
                emprestimos.forEach(emprestimo -> {

                    if (emprestimo.getQuantidadeParcelasAtraso() > 0) {
                        homeDTOColaborador.emprestimosEmAtraso.add(emprestimo);
                    }

                    if (emprestimo.getQuantidadeParcelasAVencer() > 0) {
                        homeDTOColaborador.emprestimosAVencer.add(emprestimo);
                    }
                });

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
                homeDTOUsuario.parcelas = parcelaService.getEmAbertoPorUsuario(id_usuario);
                homeDTOUsuario.tipoEmprestimos = tipoEmprestimoService.getTipoEmprestimos();

                homeDTOUsuario.quantidadeNotificacaoNaoLida = notificacaoService.getQuantidadeNotificacaoNaoLidaPorUsuario(id_usuario);

                return homeDTOUsuario;
            }
        }
    }
}
