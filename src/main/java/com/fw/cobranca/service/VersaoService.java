package com.fw.cobranca.service;

import com.fw.cobranca.domain.Versao;
import com.fw.cobranca.repository.VersaoRepository;
import com.fw.cobranca.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

@Service
public class VersaoService implements Util {
    @Autowired
    private VersaoRepository repository;

    public Versao getVersao() {
        Iterable<Versao> versaos = repository.findAll();

        AtomicReference<Versao> versao = new AtomicReference<>();
        versaos.forEach(v -> {
            versao.set(v);
        });

        return versao.get();
    }


}
