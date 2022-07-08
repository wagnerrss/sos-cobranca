package com.fw.cobranca.service;

import com.fw.cobranca.domain.Emprestimo;
import com.fw.cobranca.domain.Parcela;
import com.fw.cobranca.domain.dto.EmprestimoDTO;
import com.fw.cobranca.repository.EmprestimoRepository;
import com.fw.cobranca.repository.ParcelaRepository;
import com.fw.cobranca.util.Util;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.text.ParseException;
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
    private EstabelecimentoService estabelecimentoService;

    public Iterable<Emprestimo> getByIdUsuario(Integer id_usuario) {
        return repository.findByIdUsuario(id_usuario);
    }

    public Iterable<Emprestimo> getEmprestimosPorIdUsuario(Integer id_usuario) {
        Iterable<Emprestimo> emprestimos = repository.findEmprestimosPorIdUsuario(id_usuario);
        emprestimos.forEach(emprestimo -> {
            emprestimo.setUsuario(usuarioService.getById(emprestimo.getIdUsuario()).get());
            System.out.println(emprestimo.getUsuario());
            emprestimo.setEstabelecimento(estabelecimentoService.getById(emprestimo.getIdEstabelecimento()).get());
            System.out.println(emprestimo.getEstabelecimento());
        });

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
                repository.findEmprestimosAVencerPorUsuario(idUsuarioAnalise).stream().map(Emprestimo::new).collect(Collectors.toList());

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

        return repository.save(c);
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

            if (d.getStatus() == 2) {
                c.setDataAprovacao(new Date());
                c.setValorAprovado(d.getValorAprovado());
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
