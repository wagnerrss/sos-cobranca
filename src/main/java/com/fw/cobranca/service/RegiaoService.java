package com.fw.cobranca.service;

import com.fw.cobranca.domain.ColaboradorRegiao;
import com.fw.cobranca.domain.Regiao;
import com.fw.cobranca.domain.dto.RegiaoDTO;
import com.fw.cobranca.repository.RegiaoRepository;
import com.fw.cobranca.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RegiaoService implements Util {
    @Autowired
    private RegiaoRepository repository;

    @Autowired
    private ColaboradorRegiaoService colaboradorRegiaoService;

    public Iterable<Regiao> getRegioes() {
        return repository.findAll();
    }

    public List<RegiaoDTO> getRegioesUsuario() {
        Iterable<Regiao> regioes = repository.findAll();

        List<RegiaoDTO> regiaoDTOS = new ArrayList<>();
        regioes.forEach(regiao -> {
            RegiaoDTO regiaoDTO = new RegiaoDTO(regiao);
            regiaoDTO.usuarios = new ArrayList<>();

            colaboradorRegiaoService.getByIdRegiao(regiao.getId()).forEach(colaborador -> {
                regiaoDTO.usuarios.add(colaborador.getIdUsuario());
            });

            regiaoDTO.ordena = regiaoDTO.usuarios.size() <= 0 ? 0 : 1;

            regiaoDTOS.add(regiaoDTO);
        });

        List<RegiaoDTO> regiaoDTOSOrdenada = new ArrayList<>();
        regiaoDTOS.stream()
                .sorted(Comparator.comparing(l -> l.ordena + l.regiao)).forEach(l -> regiaoDTOSOrdenada.add(
                        new RegiaoDTO(l)
                ));

        return regiaoDTOSOrdenada;
    }

    public RegiaoDTO getById(Integer id) {
        Optional<Regiao> opRegiao = repository.findById(id);
        if (opRegiao.isPresent()) {

            RegiaoDTO regiaoDTO = new RegiaoDTO(opRegiao.get());
            colaboradorRegiaoService.getByIdRegiao(opRegiao.get().getId()).forEach(colaborador -> {
                if (regiaoDTO.usuarios == null) {
                    regiaoDTO.usuarios = new ArrayList<>();
                }

                regiaoDTO.usuarios.add(colaborador.getIdUsuario());
            });

            return regiaoDTO;
        }

        return null;

    }

    public Optional<Regiao> getByRegiao(String regiao) {
        return repository.findByRegiao(regiao);
    }

    public Regiao insert(Regiao c) {
        Optional<Regiao> r = repository.findByRegiao(c.getRegiao());
        if (!r.isPresent()) {
            return repository.save(c);
        }

        return c;
    }

}
