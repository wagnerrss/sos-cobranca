package com.fw.cobranca.service;


import com.fw.cobranca.api.CaixaController;
import com.fw.cobranca.domain.Emprestimo;
import com.fw.cobranca.domain.Notificacao;
import com.fw.cobranca.domain.Parcela;
import com.fw.cobranca.domain.TipoEmprestimo;
import com.fw.cobranca.domain.dto.ParcelaHomeDTO;
import com.fw.cobranca.domain.dto.ParcelaPorDiaHomeDTO;
import com.fw.cobranca.repository.CaixaRepository;
import com.fw.cobranca.repository.ParcelaRepository;
import com.fw.cobranca.upload.FirebaseStorageService;
import com.fw.cobranca.upload.UploadInput;
import com.fw.cobranca.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class ParcelaService implements Util {
    @Autowired
    private ParcelaRepository repository;

    @Autowired
    private CaixaRepository caixaRepository;

    @Autowired
    private TipoEmprestimoService tipoEmprestimoService;

    @Autowired
    private EmprestimoService emprestimoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CaixaController caixaController;

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    @Autowired
    private NotificacaoService notificacaoService;

    public Optional<Parcela> getById(Integer id) {
        return repository.findById(id);
    }

    public Double getTotalParcelasEmAtraso() {
        return repository.findTotalParcelasEmAtraso();
    }

    public Double getTotalParcelasAVencer() {
        return repository.findTotalParcelasAVencer();
    }

    public Double getTotalAguardandoAnalise() {
        return repository.findTotalParcelasAguardandoAnalise();
    }

    public Iterable<ParcelaHomeDTO> getAguardandoAnalise() {
        List<ParcelaHomeDTO> parcelasHomeDTO = new ArrayList<>();

        Iterable<Map<String, Object>> parcelas = repository.findAguardandoAnalise();

        parcelas.forEach(parcela -> {
            ParcelaHomeDTO parcelaHomeDTO = new ParcelaHomeDTO(parcela);
            parcelasHomeDTO.add(parcelaHomeDTO);
        });

        return parcelasHomeDTO;
    }

    public Iterable<ParcelaPorDiaHomeDTO> getPorUsuario(Integer id_usuario) {
        Iterable<Map<String, Object>> parcelas = repository.findEmAbertoPorUsuario(id_usuario);
        List<ParcelaPorDiaHomeDTO> parcelasPorDia = new ArrayList<>();
        parcelas.forEach(parcela -> {
            AtomicBoolean insere = new AtomicBoolean(true);
            parcelasPorDia.forEach(pd -> {
                if (pd.dataVencimento.equals(parcela.get("data_vencimento"))) {
                    insere.set(false);
                }
            });

            if (insere.get()) {
                ParcelaPorDiaHomeDTO parcelaPorDiaHomeDTO = new ParcelaPorDiaHomeDTO();
                parcelaPorDiaHomeDTO.dataVencimento = toStr(parcela.get("data_vencimento"));
                parcelaPorDiaHomeDTO.diaSemana = toStr(parcela.get("dia_semana"));
                parcelaPorDiaHomeDTO.dia = toStr(parcela.get("dia"));
                parcelaPorDiaHomeDTO.mes = toStr(parcela.get("mes"));
                parcelaPorDiaHomeDTO.parcelas = new ArrayList<>();

                parcelasPorDia.add(parcelaPorDiaHomeDTO);
            }
        });

        parcelas.forEach(parcela -> {
            AtomicBoolean insere = new AtomicBoolean(true);
            parcelasPorDia.forEach(pd -> {
                if (pd.dataVencimento.equals(parcela.get("data_vencimento"))) {
                    ParcelaHomeDTO parcelaHomeDTO = new ParcelaHomeDTO(parcela);
                    parcelaHomeDTO.valorVencimento = calculaJurosAtraso(parcelaHomeDTO);
                    pd.parcelas.add(parcelaHomeDTO);
                }
            });
        });

        return parcelasPorDia;
    }

    public Iterable<ParcelaPorDiaHomeDTO> getEmAbertoPorUsuario(Integer id_usuario) {
        Iterable<Map<String, Object>> parcelas = repository.findEmAbertoPorUsuario(id_usuario);
        List<ParcelaPorDiaHomeDTO> parcelasPorDia = new ArrayList<>();
        parcelas.forEach(parcela -> {
            AtomicBoolean insere = new AtomicBoolean(true);
            parcelasPorDia.forEach(pd -> {
                if (pd.dataVencimento.equals(parcela.get("data_vencimento"))) {
                    insere.set(false);
                }
            });

            if (insere.get()) {
                ParcelaPorDiaHomeDTO parcelaPorDiaHomeDTO = new ParcelaPorDiaHomeDTO();
                parcelaPorDiaHomeDTO.dataVencimento = toStr(parcela.get("data_vencimento"));
                parcelaPorDiaHomeDTO.diaSemana = toStr(parcela.get("dia_semana"));
                parcelaPorDiaHomeDTO.dia = toStr(parcela.get("dia"));
                parcelaPorDiaHomeDTO.mes = toStr(parcela.get("mes"));
                parcelaPorDiaHomeDTO.parcelas = new ArrayList<>();

                parcelasPorDia.add(parcelaPorDiaHomeDTO);
            }
        });

        parcelas.forEach(parcela -> {
            AtomicBoolean insere = new AtomicBoolean(true);
            parcelasPorDia.forEach(pd -> {
                if (pd.dataVencimento.equals(parcela.get("data_vencimento"))) {
                    ParcelaHomeDTO parcelaHomeDTO = new ParcelaHomeDTO(parcela);
                    parcelaHomeDTO.valorVencimento = calculaJurosAtraso(parcelaHomeDTO);
                    pd.parcelas.add(parcelaHomeDTO);
                }
            });
        });

        return parcelasPorDia;
    }

    public Iterable<ParcelaPorDiaHomeDTO> getPorEmprestimo(Integer id_emprestimo) {

        Iterable<Map<String, Object>> parcelas = repository.findPorEmprestimo(id_emprestimo);
        List<ParcelaPorDiaHomeDTO> parcelasPorDia = new ArrayList<>();

        //INSERE PRIMEIRO AS DATAS DE VENCIMENTO COM A DESCRIÇÃO DO DIA MES E ANO
        parcelas.forEach(parcela -> {
            AtomicBoolean insere = new AtomicBoolean(true);
            parcelasPorDia.forEach(pd -> {
                if (pd.dataVencimento.equals(parcela.get("data_vencimento"))) {
                    insere.set(false);
                }
            });

            if (insere.get()) {
                ParcelaPorDiaHomeDTO parcelaPorDiaHomeDTO = new ParcelaPorDiaHomeDTO();
                parcelaPorDiaHomeDTO.dataVencimento = toStr(parcela.get("data_vencimento"));
                parcelaPorDiaHomeDTO.diaSemana = toStr(parcela.get("dia_semana"));
                parcelaPorDiaHomeDTO.dia = toStr(parcela.get("dia"));
                parcelaPorDiaHomeDTO.mes = toStr(parcela.get("mes"));
                parcelaPorDiaHomeDTO.parcelas = new ArrayList<>();

                parcelasPorDia.add(parcelaPorDiaHomeDTO);
            }
        });

        //INSERE AS PARCELAS NOS SEUS DIAS
        parcelas.forEach(parcela -> {
            parcelasPorDia.forEach(pd -> {
                if (pd.dataVencimento.equals(parcela.get("data_vencimento"))) {
                    ParcelaHomeDTO parcelaHomeDTO = new ParcelaHomeDTO(parcela);
                    parcelaHomeDTO.valorVencimento = calculaJurosAtraso(parcelaHomeDTO);
                    pd.parcelas.add(parcelaHomeDTO);
                }
            });
        });

        return parcelasPorDia;
    }

    public Parcela insert(Parcela c) {
        if ((c.getId() == null) || (c.getId() <= 0)) {
//            Optional<Emprestimo> optional = getByNome(c.getNome());
//            if (optional.isPresent()) {
//                throw new RuntimeException("Registro já existe");
//            }
        } else {
            throw new RuntimeException("Não foi possível inserir o registro");
        }

        return repository.save(c);
    }

    public ParcelaHomeDTO update(Integer id, Parcela d) throws Exception {
        Assert.notNull(id, "Não foi possível atualizar o registro");

        if (d.getStatus() > 0) {
            verificaParcelaAnteriorEmAberto(d.getIdEmprestimo(), d.getNumero());
        }

        Optional<Parcela> optional = getById(id);
        if (optional.isPresent()) {
            Parcela c = optional.get();

            c.setObservacoes(d.getObservacoes());
            c.setIdUsuario(d.getIdUsuario());

            //avalia se a parcela estava aguardando analise ou aprovada e foi alterada para aguardando pagamento, dai insere notificação
            boolean enviaNotificacaoCancelada = ((c.getStatus() > 0) && (d.getStatus() == 0));
            //notificação quando a parcela é aprovada
            boolean enviaNotificacaoAprovada = ((c.getStatus() == 1) && (d.getStatus() == 2));

            boolean enviaNotificacaoAnalise = false;

            c.setStatus(d.getStatus());

            String comprovanteBase64 = d.getComprovante() == null ? "" : d.getComprovante();

//            //verifica se o estabelecimento que veio da chamada tem base64
            if ((d.getStatus() != 0) && (comprovanteBase64.contains("base64,"))) {
                //salva a imagem base64 no firebase
                UploadInput firebase = new UploadInput();
                firebase.setFilename("pagamento/" + c.getId());
                firebase.setMimeType("image/jpg");
                firebase.setBase64(comprovanteBase64.replace("base64,", ""));
                String urlComprovante = firebaseStorageService.upload(firebase);

                c.setComprovante(urlComprovante);

                enviaNotificacaoAnalise = true;
            }

            switch (d.getStatus()) {
                //aguardando pagamento
                case 0:
                    c.setDataPagamento(null);
                    c.setValorPagamento(0.0);
                    c.setObservacoes(null);
                    c.setComprovante(null);
                    c.setIdUsuario(0);
                    break;
                //aguardando análise, o adm vai verificar o comprovante de pagamento e aprovar ou cancelar
                case 1:
                    c.setDataPagamento(new Date());
                    c.setValorPagamento(c.getValorVencimento());
                    break;
                //pagamento aprovado
                case 2:
                    if (c.getDataPagamento() == null) {
                        c.setDataPagamento(new Date());
                    }

                    c.setValorPagamento(c.getValorVencimento());

                    //insere o valor no caixa do adm
                    Map caixa = new LinkedHashMap();
                    caixa.put("id", 0);
                    caixa.put("idUsuario", 999); //adm
                    caixa.put("idParcela", c.getId());
                    caixa.put("dataMovimento", new Date());
                    caixa.put("valorMovimento", c.getValorPagamento());
                    caixa.put("tipo", "C"); //debito
                    caixa.put("origem", "RP"); //recebimento de parcela
                    caixa.put("observacoes", "Recebimento da Parcela " + c.getNumero() + " do Empréstimo " + c.getIdEmprestimo());

                    caixaController.postCaixa(caixa);

                    break;
            }

            Emprestimo e = emprestimoService.getById(c.getIdEmprestimo()).get();

            String obs = "Empréstimo Nº " + e.getId();
            obs += "\nParcela Nº " + d.getNumero() + " de " + e.getQuantidadeParcelas() + "\n";
            if ((enviaNotificacaoCancelada) || (enviaNotificacaoAprovada) || (enviaNotificacaoAnalise)) {
                List<Integer> idsUsuario = new ArrayList<>();

                String tipo = "";
                String titulo = "";

                idsUsuario.add(e.getIdUsuario());

                if (enviaNotificacaoCancelada) {
                    tipo = "C";
                    titulo = "Parcela cancelada!";
                }

                if (enviaNotificacaoAprovada) {
                    tipo = "A";
                    titulo = "Parcela aprovada!";
                }

                if (enviaNotificacaoAnalise) {
                    tipo = "A";
                    titulo = "Parcela em análise!";

                    //adiciona os usuario administradores.
                    usuarioService.getUsuariosPorTipo("A").forEach((usuario -> {
                        idsUsuario.add(usuario.getId());
                    }));
                }

                String finalTipo = tipo;
                String finalTitulo = titulo;
                String finalObs = obs;

                idsUsuario.forEach((idUsuario -> {
                    Notificacao n = new Notificacao(0, idUsuario, new Date(), finalTipo, "N", finalTitulo, finalObs + d.getObservacoes());
                    notificacaoService.insert(n);
                }));

            }

            ParcelaHomeDTO p = new ParcelaHomeDTO(repository.save(c), e.getQuantidadeParcelas());

            return p;

        } else {
            throw new RuntimeException("Não foi possível atualizar o registro");
        }
    }

    public boolean verificaParcelaAnteriorEmAberto(Integer id_emprestimo, Integer numero) throws Exception {
        Integer totalParcelas = repository.totalizaParcelaAnteriorEmAberto(id_emprestimo, numero);
        if (totalParcelas > 0) {
            throw new Exception("Existem parcelas (" + totalParcelas + ") anteriores em aberto, favor realizar o pagamento dessas parcelas.");
        } else {
            return true;
        }
    }

    public ParcelaHomeDTO cancelamento(Integer id) {
        Assert.notNull(id, "Não foi possível atualizar o registro");

        Optional<Parcela> optional = getById(id);
        if (optional.isPresent()) {
            Parcela c = optional.get();

            if (c.getValorPagamento() > 0) {

                //busca se a somatoria dos caixas com id da parcela é maior q zero
                Double totalParcela = caixaRepository.findByParcela(id);

                if (totalParcela > 0) {
                    //insere o valor no caixa do adm
                    Map caixa = new LinkedHashMap();
                    caixa.put("id", 0);
                    caixa.put("idUsuario", 999); //adm
                    caixa.put("idParcela", c.getId());
                    caixa.put("dataMovimento", new Date());
                    caixa.put("valorMovimento", c.getValorPagamento());
                    caixa.put("tipo", "D"); //debito
                    caixa.put("origem", "CP"); //cancelmento de parcela
                    caixa.put("observacoes", "Cancelamento da Parcela " + c.getNumero() + " do Empréstimo " + c.getIdEmprestimo());

                    caixaController.postCaixa(caixa);
                }
            }

            c.setStatus(0); //aguardando pagamento
            c.setDataPagamento(null);
            c.setValorPagamento(0.0);
            c.setObservacoes(null);
            c.setIdUsuario(0);

            Emprestimo e = emprestimoService.getById(c.getIdEmprestimo()).get();

            ParcelaHomeDTO p = new ParcelaHomeDTO(repository.save(c), e.getQuantidadeParcelas());

            return p;

        } else {
            throw new RuntimeException("Não foi possível cancelar a parcela");
        }
    }

    public List<Parcela> geraParcela(Emprestimo emprestimo) {
        List<Parcela> listaParcelas = new ArrayList<>();
        Optional<TipoEmprestimo> optionalTipoEmprestimo = tipoEmprestimoService.getById(emprestimo.getIdTipo());

        if (optionalTipoEmprestimo.isPresent()) {
            TipoEmprestimo tipoEmprestimo = optionalTipoEmprestimo.get();

            //calculo que o kra passou, voltar depois
            //    Double valorParcela = (((emprestimo.getValorAprovado() * tipoEmprestimo.getJuros()) / 100) + emprestimo.getValorAprovado()) / tipoEmprestimo.getParcelas();

            Double valorParcela = ((emprestimo.getValorAprovado() * (tipoEmprestimo.getJuros() / 100)) * tipoEmprestimo.getParcelas()) / tipoEmprestimo.getParcelas(); //aqui eu tenho o juro
            valorParcela = valorParcela + (emprestimo.getValorAprovado() / tipoEmprestimo.getParcelas());

            Calendar dataVencimento = Calendar.getInstance();
            dataVencimento.setTime(emprestimo.getDataAprovacao());

            for (int i = 1; i <= tipoEmprestimo.getParcelas(); i++) {
                if (emprestimo.getTipo() == 0) {
                    dataVencimento.add(Calendar.DATE, 1);
                    dataVencimento = checaFDS(dataVencimento);
                } else {
                    dataVencimento.add(Calendar.MONTH, 1);
                }

                Parcela parcela = new Parcela(0, emprestimo.getId(), i, dataVencimento.getTime(), valorParcela, null, 0.0, 0, null, null, 0);

                repository.save(parcela);
                listaParcelas.add(parcela);
            }
        }

        return listaParcelas;
    }

    public static DateTimeFormatter DOW_FMT = DateTimeFormatter.ofPattern("E", new Locale("pt", "BR"));

    public static DateTimeFormatter PARSER = DateTimeFormatter.ofPattern("dd/MM/yyyy").withResolverStyle(ResolverStyle.STRICT);

    public static String getDayOfWeek(Date data) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String strDate = dateFormat.format(data);

            return DOW_FMT.format(PARSER.parse(strDate)).toUpperCase();
        } catch (DateTimeException e) {
            e.printStackTrace();
        }

        return "---";
    }

    Double calculaJurosAtraso(ParcelaHomeDTO pc) {
        // System.out.println(this.dataVencimento.substring(0, 10));
        Date dataVenc = toDate(pc.dataVencimento);
        String strDataVenc = new SimpleDateFormat("yyyy-MM-dd").format(dataVenc);
        LocalDate _dataVencimento = LocalDate.parse(strDataVenc.substring(0, 10), DateTimeFormatter.ISO_LOCAL_DATE);

        long diff = ChronoUnit.DAYS.between(_dataVencimento, LocalDate.now());

        Integer diasAtraso = pc.valorPagamento > 0 ? 0 : Math.toIntExact(diff);

        if (diasAtraso > 0) {
            Optional<Emprestimo> emprestimo = emprestimoService.getById(pc.idEmprestimo);

            if (emprestimo.isPresent()) {
                Optional<TipoEmprestimo> tipoEmprestimo = tipoEmprestimoService.getById(emprestimo.get().getIdTipo());

                if (tipoEmprestimo.isPresent()) {
                    Double jurosAtraso = tipoEmprestimo.get().getJurosAtraso();
                    Double valorParcela = pc.valorVencimento;
                    for (int i = 0; i <= diasAtraso; i++) {
                        valorParcela = valorParcela + ((valorParcela / 100) * jurosAtraso);
                    }

                    return valorParcela;
                }
            }
        }

        return pc.valorVencimento;
    }

}
