package com.fw.cobranca.service;

import com.fw.cobranca.domain.ColaboradorRegiao;
import com.fw.cobranca.domain.Emprestimo;
import com.fw.cobranca.domain.Usuario;
import com.fw.cobranca.domain.dto.RegiaoDTO;
import com.fw.cobranca.repository.ColaboradorRegiaoRepository;
import com.fw.cobranca.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ColaboradorRegiaoService implements Util {
    @Autowired
    private ColaboradorRegiaoRepository repository;

    @Autowired
    private RegiaoService regiaoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EmprestimoService emprestimoService;

    public Iterable<ColaboradorRegiao> getColaboradoresRegioes() {
        return repository.findAll();
    }

    public Optional<ColaboradorRegiao> getById(Integer id) {
        return repository.findById(id);
    }

    public Iterable<ColaboradorRegiao> getByIdUsuario(Integer id_usuario) {
        return repository.findByIdUsuario(id_usuario);
    }

    public Iterable<ColaboradorRegiao> getByIdRegiao(Integer id_regiao) {
        return repository.findByIdRegiao(id_regiao);
    }

    public ColaboradorRegiao insert(Integer idRegiao, Integer idUsuario) {
        Optional<ColaboradorRegiao> opCr = repository.findByUsuarioRegiao(idUsuario, idRegiao);
        if (opCr.isPresent()){
            return opCr.get();
        }

        RegiaoDTO r = regiaoService.getById(idRegiao);
        Optional<Usuario> u = usuarioService.getById(idUsuario);

        if ((r.id > 0) && (u.isPresent())){
            ColaboradorRegiao cr = new ColaboradorRegiao(0, idUsuario, idRegiao);
            return repository.save(cr);
        }

        return null;
    }

    public Integer deleteByIdRegiao(Integer id_regiao) {
        return repository.deleteByRegiao(id_regiao);
    }

    public void notificaColaboradorRegiao(Integer id_regiao){
        repository.emprestimosSemUsuarioAnalise(id_regiao).forEach(id_emprestimo -> {
            Optional<Emprestimo> emprestimoOptional = emprestimoService.getById(id_emprestimo);
            if (emprestimoOptional.isPresent()){
                emprestimoService.notificaUsuarioNovoEmprestimo(emprestimoOptional.get(), false);
            }
        });
    }

}
