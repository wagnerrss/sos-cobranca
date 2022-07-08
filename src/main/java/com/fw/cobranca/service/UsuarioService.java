package com.fw.cobranca.service;


import com.fw.cobranca.domain.Usuario;
import com.fw.cobranca.repository.UsuarioRepository;
import com.fw.cobranca.upload.FirebaseStorageService;
import com.fw.cobranca.upload.UploadInput;
import com.fw.cobranca.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UsuarioService implements Util {
    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    public Iterable<Usuario> getUsuarios() {
        return repository.findAll();
    }

    public Iterable<Usuario> getUsuariosPorNomeAproximado(String nome) {
        return repository.findByNomeAproximado("%" + nome + "%");
    }

    public Iterable<Usuario> getUsuariosPorTipo(String tipo) {
        return repository.findByTipo(tipo);
    }

    public Iterable<Usuario> getUsuariosPorNomeAproximadoETipo(String nome, String tipo) {
        return repository.findByNomeAproximadoETipo("%" + nome + "%", tipo);
    }
    public Optional<Usuario> getById(Integer id) {
        return repository.findById(id);
    }

    public Optional<Usuario> getByNome(String nome) {
        return repository.findByNome(nome);
    }

    public Optional<Usuario> getByDocumento(String documento) {
        return repository.findByDocumento(documento);
    }

    public Usuario insert(Usuario c) {
        if ((c.getId() == null) || (c.getId() <= 0)) {
            Optional<Usuario> optional1 = getByDocumento(soNumeros(c.getDocumento()));
            if (optional1.isPresent()) {
                throw new RuntimeException("Usuário já cadastrado!");
            }
        } else {
            throw new RuntimeException("Não foi possível inserir o registro");
        }

        c.setDocumento(soNumeros(c.getDocumento()));
        c.setFone(soNumeros(c.getFone()));

        if (c.getFoto().contains("base64,")) {
            //salva a imagem base64 no firebase
            UploadInput firebase = new UploadInput();
            firebase.setFilename("usuario/" + c.getDocumento());
            firebase.setMimeType("image/jpg");
            firebase.setBase64(c.getFoto().replace("base64,", ""));
            String urlFoto = firebaseStorageService.upload(firebase);

            c.setFoto(urlFoto);
        }

        return repository.save(c);
    }

    public Usuario update(Integer id, Usuario d) {
        Assert.notNull(id, "Não foi possível atualizar o registro");

        Optional<Usuario> optional = getById(id);
        if (optional.isPresent()) {
            Usuario c = optional.get();

            c.setFoto(d.getFoto());
            c.setTipo(d.getTipo());
            c.setDocumento(d.getDocumento());
            c.setNome(d.getNome());
            c.setFone(d.getFone());

            if (c.getFoto().contains("base64,")) {
                //salva a imagem base64 no firebase
                UploadInput firebase = new UploadInput();
                firebase.setFilename("usuario/" + c.getDocumento());
                firebase.setMimeType("image/jpg");
                firebase.setBase64(c.getFoto().replace("base64,", ""));
                String urlFoto = firebaseStorageService.upload(firebase);

                c.setFoto(urlFoto);
            }

            if (!d.getSenha().isEmpty()) {
                c.setSenha(d.getSenha());
            }

            return repository.save(c);
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
