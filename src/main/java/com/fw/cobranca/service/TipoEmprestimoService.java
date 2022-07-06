package com.fw.cobranca.service;


import com.fw.cobranca.domain.TipoEmprestimo;
import com.fw.cobranca.repository.TipoEmprestimoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
public class TipoEmprestimoService {
    @Autowired
    private TipoEmprestimoRepository repository;

    public Iterable<TipoEmprestimo> getTipoEmprestimos() {
        return repository.findAll();
    }

    public Optional<TipoEmprestimo> getById(Integer id) {
        return repository.findById(id);
    }

    public Optional<TipoEmprestimo> getByDescricao(String descricao) {
        return repository.findByDescricao(descricao);
    }

    public TipoEmprestimo insert(TipoEmprestimo c) {
        if ((c.getId() == null) || (c.getId() <= 0)) {
            Optional<TipoEmprestimo> optional = getByDescricao(c.getDescricao());
            if (optional.isPresent()) {
                throw new RuntimeException("Registro já existe");
            }
        } else {
            throw new RuntimeException("Não foi possível inserir o registro");
        }

        return repository.save(c);
    }

    public TipoEmprestimo update(Integer id, TipoEmprestimo d) {
        Assert.notNull(id, "Não foi possível atualizar o registro");

        Optional<TipoEmprestimo> optional = getById(id);
        if (optional.isPresent()) {
            TipoEmprestimo c = optional.get();

            c.setDescricao(d.getDescricao());
            c.setParcelas(d.getParcelas());
            c.setJuros(d.getJuros());

            repository.save(c);

            return c;
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
