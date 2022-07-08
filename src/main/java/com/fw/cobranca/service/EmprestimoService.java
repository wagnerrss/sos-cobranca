package com.fw.cobranca.service;

import com.fw.cobranca.api.CaixaController;
import com.fw.cobranca.domain.*;
import com.fw.cobranca.domain.dto.EmprestimoDTO;
import com.fw.cobranca.repository.EmprestimoRepository;
import com.fw.cobranca.repository.ParcelaRepository;
import com.fw.cobranca.upload.FirebaseStorageService;
import com.fw.cobranca.upload.UploadInput;
import com.fw.cobranca.util.Util;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@Service
public class EmprestimoService implements Util {

    @Autowired
    private EmprestimoRepository repository;

    @Autowired
    private ParcelaRepository parcelaRepository;

    @Autowired
    private ParcelaService parcelaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private NotificacaoService notificacaoService;

    @Autowired
    private CaixaController caixaController;

    @Autowired
    private EstabelecimentoService estabelecimentoService;

    @Autowired
    private ColaboradorRegiaoService colaboradorRegiaoService;

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    public Iterable<Emprestimo> getByIdUsuario(Integer id_usuario) {
        return repository.findByIdUsuario(id_usuario);
    }

    public Iterable<Emprestimo> getEmprestimosPorIdUsuario(Integer id_usuario) {
        Iterable<Emprestimo> emprestimos = repository.findEmprestimosAguardandoPorIdUsuario(id_usuario);
        emprestimos.forEach(emprestimo -> {
            emprestimo.setUsuario(usuarioService.getById(emprestimo.getIdUsuario()).get());
            System.out.println(emprestimo.getUsuario());
            emprestimo.setEstabelecimento(estabelecimentoService.getById(emprestimo.getIdEstabelecimento()).get());
            System.out.println(emprestimo.getEstabelecimento());
        });

        return emprestimos;
    }

    public Iterable<Map> getEmprestimosPorIdUsuarioEmAberto(Integer id_usuario) {
        Iterable<Map> emprestimos = repository.findEmprestimosAVencerPorUsuario(id_usuario);

        return emprestimos;
    }

    public Double totalEmprestimosSolicitado() {
        Iterable<Emprestimo> emprestimos = repository.findEmprestimosPorStatus(0);

        AtomicReference<Double> total = new AtomicReference<>(0.00);
        emprestimos.forEach(emprestimo -> {
            total.updateAndGet(v -> v + emprestimo.getValorSolicitado());

        });

        return total.get();
    }

    public Iterable<Emprestimo> getEmprestimosPorStatus(Integer status) {
        Iterable<Emprestimo> emprestimos = repository.findEmprestimosPorStatus(status);

        emprestimos.forEach(emprestimo -> {
            emprestimo.setUsuario(usuarioService.getById(emprestimo.getIdUsuario()).get());

            emprestimo.setEstabelecimento(estabelecimentoService.getById(emprestimo.getIdEstabelecimento()).get());

        });

        return emprestimos;
    }

    public Iterable<Emprestimo> getEmprestimosPorStatusEIdUsuarioAnalise(Integer status, Integer idUsuarioAnalise) {
        Iterable<Emprestimo> emprestimos = repository.findEmprestimosPorStatusEIdUsuarioAnalise(status, idUsuarioAnalise);

        emprestimos.forEach(emprestimo -> {
            emprestimo.setUsuario(usuarioService.getById(emprestimo.getIdUsuario()).get());

            emprestimo.setEstabelecimento(estabelecimentoService.getById(emprestimo.getIdEstabelecimento()).get());

        });

        return emprestimos;
    }

    public Iterable<Emprestimo> getEmprestimos(Integer idUsuarioAnalise) {
        List<Emprestimo> emprestimos = idUsuarioAnalise <= 0 ?
                repository.findEmprestimos().stream().map(Emprestimo::new).collect(Collectors.toList()) :
                repository.findEmprestimosPorUsuario(idUsuarioAnalise).stream().map(Emprestimo::new).collect(Collectors.toList());

        emprestimos.forEach(emprestimo -> {
            emprestimo.setUsuario(usuarioService.getById(emprestimo.getIdUsuario()).get());

            emprestimo.setEstabelecimento(estabelecimentoService.getById(emprestimo.getIdEstabelecimento()).get());

        });

        return emprestimos;
    }


    public Iterable<Emprestimo> getEmprestimosEmAtraso(Integer idUsuarioAnalise) {
        List<Emprestimo> emprestimos = idUsuarioAnalise <= 0 ?
                repository.findEmprestimosEmAtraso().stream().map(Emprestimo::new).collect(Collectors.toList()) :
                repository.findEmprestimosEmAtrasoPorUsuario(idUsuarioAnalise).stream().map(Emprestimo::new).collect(Collectors.toList());

        emprestimos.forEach(emprestimo -> {
            emprestimo.setUsuario(usuarioService.getById(emprestimo.getIdUsuario()).get());

            emprestimo.setEstabelecimento(estabelecimentoService.getById(emprestimo.getIdEstabelecimento()).get());

        });

        return emprestimos;
    }

    public Iterable<Emprestimo> getEmprestimosAVencer(Integer idUsuarioAnalise) {
        List<Emprestimo> emprestimos = idUsuarioAnalise <= 0 ?
                repository.findEmprestimosAVencer().stream().map(Emprestimo::new).collect(Collectors.toList()) :
                repository.findEmprestimosAVencerPorUsuarioAnalise(idUsuarioAnalise).stream().map(Emprestimo::new).collect(Collectors.toList());

        emprestimos.forEach(emprestimo -> {
            emprestimo.setUsuario(usuarioService.getById(emprestimo.getIdUsuario()).get());

            emprestimo.setEstabelecimento(estabelecimentoService.getById(emprestimo.getIdEstabelecimento()).get());

        });

        return emprestimos;
    }

    public Iterable<EmprestimoDTO> getByIdEstabelecimento(Integer id_estabelecimento) {
        List<EmprestimoDTO> emprestimos = new ArrayList<>();

        repository.findByIdEstabelecimento(id_estabelecimento).forEach(emprestimo -> {
            EmprestimoDTO emprestimoDTO = new EmprestimoDTO(emprestimo);
            emprestimos.add(emprestimoDTO);
        });

        emprestimos.forEach(emprestimo -> {
            Iterable<Parcela> parcelas = parcelaRepository.findByIdEmprestimo(emprestimo.id);

            emprestimo.parcelas = parcelas;
        });

        return emprestimos;
    }

    public boolean verificaEmprestimoEmAberto(Integer id_estabelecimento) throws Exception {
        AtomicReference<String> res = new AtomicReference<>("");
        Iterable<EmprestimoDTO> emprestimos = getByIdEstabelecimento(id_estabelecimento);
        emprestimos.forEach(emprestimo -> {
            if (emprestimo.status == 0) {
                res.set("A");
            }

            if (emprestimo.status == 1) {
                res.set("E");
            }

            emprestimo.parcelas.forEach(parcela -> {
                if (parcela.getValorPagamento() <= 0f) {
                    res.set("PA");
                }

                if (parcela.getStatus() == 1) {
                    res.set("PE");
                }
            });
        });

        switch (res.get()) {
            case "A":
                throw new Exception("Existem empréstimos aguardando análise, não é possível solicitar um novo empréstimo.");
            case "E":
                throw new Exception("Existem empréstimos em análise, não é possível solicitar um novo empréstimo.");
            case "PA":
                throw new Exception("Existem empréstimos com parcelas em aberto, favor realizar o pagamento dessas parcelas para solicitar um novo empréstimo.");
            case "PE":
                throw new Exception("Existem empréstimos com parcelas em análise, favor aguardar a confirmação para solicitar um novo empréstimo.");
        }

        return true;
    }

    public Optional<Emprestimo> getById(Integer id) {
        return repository.findById(id);
    }

    public Emprestimo insert(Emprestimo c) {
        if ((c.getId() == null) || (c.getId() <= 0)) {
//            Optional<Emprestimo> optional = getByNome(c.getNome());
//            if (optional.isPresent()) {
//                throw new RuntimeException("Registro já existe");
//            }
        } else {
            throw new RuntimeException("Não foi possível inserir o registro");
        }

        c.setDataSolicitacao(new DateTime().toDate());

        String comprovanteBase64 = c.getDocumento();
        c.setDocumento("");

        c = repository.save(c);

        //verifica se o estabelecimento que veio da chamada tem base64
        if (comprovanteBase64.contains("base64,")) {
            //salva a imagem base64 no firebase
            UploadInput firebase = new UploadInput();
            firebase.setFilename("emprestimo/" + c.getId());
            firebase.setMimeType("image/jpg");
            firebase.setBase64(comprovanteBase64.replace("base64,", ""));
            String urlComprovante = firebaseStorageService.upload(firebase);

            c.setDocumento(urlComprovante);

            c = repository.save(c);
        }

        notificaUsuarioNovoEmprestimo(c, true);

        return c;
    }

    public void notificaUsuarioNovoEmprestimo(Emprestimo emprestimo, boolean notificaAdm) {
        /*Notificação*/
        List<Integer> idsUsuario = new ArrayList<>();

        if (notificaAdm) {
            //adiciona os usuario administradores.
            usuarioService.getUsuariosPorTipo("A").forEach((usuario -> {
                idsUsuario.add(usuario.getId());
            }));
        }

        //adiciona os colaboradores da região do estabelecimento
        Optional<Estabelecimento> e = estabelecimentoService.getById(emprestimo.getIdEstabelecimento());

        String mensagem = e.get().getNome() + "\n" +
                e.get().getBairro().toUpperCase() + " - " + e.get().getCidade().toUpperCase() + " - " + e.get().getEstado().toUpperCase() + "\n" +
                ((emprestimo.getTipo() <= 0) ?
                        "Solicitado " + formatCurr("", emprestimo.getValorSolicitado()) + " em " + emprestimo.getQuantidadeParcelas() + "X." :
                        tipoToStr(emprestimo.getTipo()));

        colaboradorRegiaoService.getByIdRegiao(e.get().getIdRegiao()).forEach((colaboradorRegiao -> {
            idsUsuario.add(colaboradorRegiao.getIdUsuario());
        }));

        idsUsuario.forEach((idUsuario -> {
            Notificacao n = new Notificacao(0, idUsuario, new Date(), "E", "N", "Novo empréstimo solicitado!", mensagem);
            notificacaoService.insert(n);
        }));
        /*Fim notificação*/
    }

    public EmprestimoDTO update(Integer id, Emprestimo d) {
        Assert.notNull(id, "Não foi possível atualizar o registro");

        Optional<Emprestimo> optional = getById(id);
        if (optional.isPresent()) {
            Emprestimo c = optional.get();

            if ((c.getStatus() == 2) || (c.getStatus() == 99)) {
                throw new RuntimeException("Empréstimo já aprovado ou cancelado!");
            }

            c.setStatus(d.getStatus());

            Optional<Estabelecimento> e = estabelecimentoService.getById(c.getIdEstabelecimento());
            String mensagem = "";
            String texto = e.get().getNome() + "\n" +
                    e.get().getBairro().toUpperCase() + " - " + e.get().getCidade().toUpperCase() + " - " + e.get().getEstado().toUpperCase() + "\n" +
                    "Empréstimo Nº " + c.getId() + ", " +
                    ((c.getTipo() <= 0) ?
                            "solicitado " + formatCurr("", c.getValorSolicitado()) + " em " + c.getQuantidadeParcelas() + "X." :
                            tipoToStr(c.getTipo()));

            //alterou o responsável pela análise pelo login do adm
            if ((d.getStatus() == 0) && (c.getIdUsuarioAnalise() <= 0) && (d.getIdUsuarioAnalise() > 0)) {
                //notifica o colaborador q tem empréstimo novo
                notificaUsuarioNovoEmprestimo(d, false);
            }

            //status = 1 - em análise
            if (d.getStatus() == 1) {
                Optional<Usuario> u = usuarioService.getById(d.getIdUsuarioAnalise());
                mensagem = texto +
                        "\nUsuário análise: " + u.get().getNome();
                Notificacao n = new Notificacao(0, d.getIdUsuario(), new Date(), "E", "N", "Empréstimo em análise!", mensagem);
                notificacaoService.insert(n);
            }

            //status = 2 aprovado
            if (d.getStatus() == 2) {
                c.setDataAprovacao(new Date());
                c.setValorAprovado(d.getValorAprovado() <= 0.0 ? d.getValorSolicitado() : d.getValorAprovado());
                c.setQuantidadeParcelas(d.getQuantidadeParcelas());
                c.setIdTipo(d.getIdTipo());

                Optional<Usuario> u = usuarioService.getById(d.getIdUsuarioAnalise());
                mensagem = texto +
                        "\nUsuário aprovação: " + u.get().getNome()
                        + ", aprovado " + formatCurr("", c.getValorAprovado())
                        + " em " + c.getQuantidadeParcelas() + "X.";
                Notificacao n = new Notificacao(0, d.getIdUsuario(), new Date(), "E", "N", "Empréstimo aprovado!", mensagem);
                notificacaoService.insert(n);

                //insere o valor no caixa do adm
                Map caixa = new LinkedHashMap();
                caixa.put("id", 0);
                caixa.put("idUsuario", 999); //adm
                caixa.put("idParcela", c.getId());
                caixa.put("dataMovimento", new Date());
                caixa.put("valorMovimento", c.getValorAprovado());
                caixa.put("tipo", "D"); //debito
                caixa.put("origem", "AE"); //aprovação de emprestimo
                caixa.put("observacoes", "Aprovação Empréstimo Nº " + c.getId());

                caixaController.postCaixa(caixa);
            }

            //status 9 = cancelado
            if (d.getStatus() == 9) {
                mensagem = c.getObservacoes();

                c.setObservacoes(mensagem + " " + d.getObservacoes());

                Optional<Usuario> u = usuarioService.getById(d.getIdUsuarioAnalise());
                mensagem = texto +
                        "\nUsuário cancelamento: " + u.get().getNome();
                Notificacao n = new Notificacao(0, d.getIdUsuario(), new Date(), "E", "N", "Empréstimo cancelado!", mensagem);
                notificacaoService.insert(n);
            }

            c.setIdUsuarioAnalise(d.getIdUsuarioAnalise());

            repository.save(c);

            EmprestimoDTO emprestimoDTO = new EmprestimoDTO(c);

            if (c.getStatus() == 2) {
                emprestimoDTO.parcelas = parcelaService.geraParcela(c);
            }

            return emprestimoDTO;
        } else {
            throw new RuntimeException("Não foi possível atualizar o registro");
        }
    }

    public boolean delete(Integer id) {
        if (getById(id).isPresent()) {
            repository.deleteById(id);
            return true;
        }

        return false;
    }

}
