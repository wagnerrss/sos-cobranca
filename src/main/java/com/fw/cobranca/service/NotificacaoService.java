package com.fw.cobranca.service;


import com.fw.cobranca.domain.Notificacao;

import com.fw.cobranca.domain.Usuario;
import com.fw.cobranca.repository.NotificacaoRepository;
import com.fw.cobranca.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;

@Service
public class NotificacaoService implements Util {
    @Autowired
    private NotificacaoRepository repository;

    @Autowired
    private UsuarioService usuarioService;

    public Optional<Notificacao> getById(Integer id) {
        return repository.findById(id);
    }

    public Iterable<Notificacao> getByIdUsuario(Integer id_usuario) {
        return repository.findByIdUsuario(id_usuario);
    }

    public Integer getQuantidadeNotificacaoNaoLidaPorUsuario(Integer id_usuario) {
        return repository.quantidadeNotificacaoNaoLidaPorUsuario(id_usuario);
    }

    public Notificacao insert(Notificacao c) {
        Optional<Usuario> usuario = usuarioService.getById(c.getIdUsuario());

        if (!usuario.isPresent()) {
            throw new RuntimeException("Usuário não encontrado");
        }

        c.setLida("N");
        c.setDataNotificacao(new Date());

        return repository.save(c);
    }

    public Notificacao update(Integer id, Notificacao d) {
        Assert.notNull(id, "Não foi possível atualizar o registro");

        Optional<Notificacao> optional = getById(id);
        if (optional.isPresent()) {
            Notificacao n = optional.get();

            n.setLida(d.getLida());

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


}
