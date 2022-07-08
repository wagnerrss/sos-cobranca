package com.fw.cobranca.service;

import com.fw.cobranca.domain.Caixa;
import com.fw.cobranca.domain.Usuario;
import com.fw.cobranca.domain.dto.CaixaPorDiaDTO;
import com.fw.cobranca.repository.CaixaRepository;
import com.fw.cobranca.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CaixaService implements Util {
    @Autowired
    private CaixaRepository repository;

    @Autowired
    private UsuarioService usuarioService;

    public Optional<Caixa> getById(Integer id) {
        return repository.findById(id);
    }

    public Iterable<Caixa> getByIdUsuario(Integer id_usuario) {
        return repository.findByIdUsuario(id_usuario);
    }

    public Iterable<CaixaPorDiaDTO> getPorDiaUsuario(Integer dias, Integer id_usuario) {
        Iterable<Map<String, Object>> caixas = id_usuario > 0 ?
                repository.findPorDiaUsuario(dias, id_usuario) :
                repository.findPorDia(dias);

        List<CaixaPorDiaDTO> caixasPorDia = new ArrayList<>();
        caixas.forEach(caixa -> {
            AtomicBoolean insere = new AtomicBoolean(true);
            caixasPorDia.forEach(cd -> {
                if (cd.dataMovimento.equals(caixa.get("data_movimento"))) {
                    insere.set(false);
                }
            });

            if (insere.get()) {
                CaixaPorDiaDTO caixaPorDiaDTO = new CaixaPorDiaDTO();
                caixaPorDiaDTO.dataMovimento = toStr(caixa.get("data_movimento"));
                caixaPorDiaDTO.diaSemana = toStr(caixa.get("dia_semana"));
                caixaPorDiaDTO.dia = toStr(caixa.get("dia"));
                caixaPorDiaDTO.mes = toStr(caixa.get("mes"));
                caixaPorDiaDTO.caixas = new ArrayList<>();

                caixasPorDia.add(caixaPorDiaDTO);
            }
        });

        caixas.forEach(caixa -> {
            caixasPorDia.forEach(cd -> {
                if (cd.dataMovimento.equals(caixa.get("data_movimento"))) {
                    Caixa cx = new Caixa(caixa);
                    cd.caixas.add(cx);
                }
            });
        });

        return caixasPorDia;
    }

    public Caixa insert(Caixa c) {
        Integer idUsuario = c.getIdUsuario();

        if (idUsuario != 999) {
            Optional<Usuario> usuario = usuarioService.getById(c.getIdUsuario());

            if ((usuario.isPresent()) && (usuario.get().getTipo().equals("U"))) {
                throw new RuntimeException("Tipo de usuário não permite movimentação de caixa");
            }

            idUsuario = c.getIdUsuario();

            if ((usuario.isPresent()) && (usuario.get().getTipo().equals("A"))) {
                idUsuario = 999; //Admninistrador grava no caixa central.
            }
        }

        if ((c.getId() == null) || (c.getId() <= 0)) {
//            Optional<Emprestimo> optional = getByNome(c.getNome());
//            if (optional.isPresent()) {
//                throw new RuntimeException("Registro já existe");
//            }

            c.setIdUsuario(idUsuario);
            c.setDataMovimento(new Date());
        } else {
            throw new RuntimeException("Não foi possível inserir o registro");
        }

        return repository.save(c);
    }

    public Caixa update(Integer id, Caixa d) {
        Assert.notNull(id, "Não foi possível atualizar o registro");

        Optional<Caixa> optional = getById(id);
        if (optional.isPresent()) {
            Caixa n = optional.get();

            //n.setId(d.getId());
            //n.setIdUsuario(d.getIdUsuario());
            //n.setIdParcela(d.getIdParcela());
            //n.setDataMovimento(d.getDataMovimento());
            n.setObservacoes(d.getObservacoes());
            //n.setOrigem(d.getOrigem());
            n.setTipo(d.getTipo());
            n.setValorMovimento(d.getValorMovimento());

            return repository.save(n);
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

    public double totalizaSaldo(Integer id_usuario) {
        AtomicReference<Double> saldo = new AtomicReference<>(0.00);

        Iterable<Caixa> caixas = repository.findByIdUsuario(id_usuario);
        caixas.forEach(caixa -> {
            saldo.updateAndGet(v -> new Double((double) (v + (caixa.getTipo().equals("D") ? -caixa.getValorMovimento() : caixa.getValorMovimento()))));
        });

        return saldo.get();
    }

    public double totalizaSaldoAdm() {
        AtomicReference<Double> saldo = new AtomicReference<>(0.00);

        Iterable<Caixa> caixas = repository.findByUsuarioAdm();
        caixas.forEach(caixa -> {
            saldo.updateAndGet(v -> new Double((double) (v + (caixa.getTipo().equals("D") ? -caixa.getValorMovimento() : caixa.getValorMovimento()))));
        });

        return saldo.get();
    }


}
