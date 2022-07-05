package com.fw.cobranca.service;


import com.fw.cobranca.domain.Estabelecimento;
import com.fw.cobranca.domain.Regiao;
import com.fw.cobranca.domain.dto.EmprestimoDTO;
import com.fw.cobranca.domain.dto.EstabelecimentoDTO;
import com.fw.cobranca.repository.EstabelecimentoRepository;
import com.fw.cobranca.upload.FirebaseStorageService;
import com.fw.cobranca.upload.UploadInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
public class EstabelecimentoService {
    @Autowired
    private EstabelecimentoRepository repository;

    @Autowired
    private EmprestimoService emprestimoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RegiaoService regiaoService;

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    public Iterable<Estabelecimento> getEstabelecimentos() {
        return repository.findAll();
    }

    public EstabelecimentoDTO getEstabelecimentosById(Integer id) {
        Optional<Estabelecimento> estabelecimento = repository.findById(id);

        EstabelecimentoDTO estabelecimentoDTO = new EstabelecimentoDTO(estabelecimento.get());

        Iterable<EmprestimoDTO> emprestimos = emprestimoService.getByIdEstabelecimento(id);
        estabelecimentoDTO.emprestimos = emprestimos;
        estabelecimentoDTO.usuario = usuarioService.getById(estabelecimentoDTO.idUsuario).get();

        return estabelecimentoDTO;
    }

    public Iterable<Estabelecimento> getByIdUsuario(Integer id_usuario) {
        Iterable<Estabelecimento> estabelecimentos = repository.findByIdUsuario(id_usuario);

//        estabelecimentos.forEach(estabelecimento -> {
//            Iterable<EmprestimoDTO> emprestimos = emprestimoService.getByIdEstabelecimento(estabelecimento.id);
//            estabelecimento.emprestimos = emprestimos;
//        });

        return estabelecimentos;
    }

    public Optional<Estabelecimento> getById(Integer id) {
        return repository.findById(id);
    }

    public Optional<Estabelecimento> getByNome(String nome) {
        return repository.findByNome(nome);
    }

    public Optional<Estabelecimento> getByCnpj(String cnpj) {
        return repository.findByCnpj(cnpj);
    }

    public Estabelecimento insert(Estabelecimento c) {
        if ((c.getId() == null) || (c.getId() <= 0)) {
//            Optional<Estabelecimento> optional = getByNome(c.getNome());
//            if (optional.isPresent()) {
//                throw new RuntimeException("Registro já existe");
//            }
        } else {
            throw new RuntimeException("Não foi possível inserir o registro");
        }

        String strRegiao = c.getBairro().toUpperCase() + " - " + c.getCidade().toUpperCase() + " - " + c.getEstado().toUpperCase();
        Optional<Regiao> opRegiao = regiaoService.getByRegiao(strRegiao);
        if (!opRegiao.isPresent()) {
            regiaoService.insert(new Regiao(0, strRegiao));
            opRegiao = regiaoService.getByRegiao(strRegiao);
        }
        c.setIdRegiao(opRegiao.get().getId());

        String comprovanteBase64 = c.getComprovante();

        String cnpj = c.soNumeros(c.getCnpj()).trim();
        c.setCnpj(cnpj.isEmpty() ? "00000000000000" : cnpj);
        c.setFone(c.soNumeros(c.getFone()));
        c.setComprovante("");

        c = repository.save(c);

        //verifica se o estabelecimento que veio da chamada tem base64
        if (comprovanteBase64.contains("base64,")) {
            //salva a imagem base64 no firebase
            UploadInput firebase = new UploadInput();
            firebase.setFilename("estabelecimento/" + c.getId());
            firebase.setMimeType("image/jpg");
            firebase.setBase64(comprovanteBase64.replace("base64,", ""));
            String urlComprovante = firebaseStorageService.upload(firebase);

            c.setComprovante(urlComprovante);

            c = repository.save(c);
        }

        return c;
    }

    public Estabelecimento update(Integer id, Estabelecimento d) {
        Assert.notNull(id, "Não foi possível atualizar o registro");

        Optional<Estabelecimento> optional = getById(id);
        if (optional.isPresent()) {
            Estabelecimento c = optional.get();

            Optional opRegiao = regiaoService.getByRegiao(c.getBairro().toUpperCase());
            if (!opRegiao.isPresent()) {
                String strRegiao = c.getBairro().toUpperCase() + " - " + c.getCidade().toUpperCase() + " - " + c.getEstado().toUpperCase();
                regiaoService.insert(new Regiao(0, strRegiao));
            }

            String cnpj = d.soNumeros(d.getCnpj()).trim();
            c.setCnpj(cnpj.isEmpty() ? "00000000000000" : cnpj);
            c.setNome(d.getNome());
            c.setEndereco(d.getEndereco());
            c.setNumero(d.getNumero());
            c.setComplemento(d.getComplemento());
            c.setBairro(d.getBairro());
            c.setCidade(d.getCidade());
            c.setEstado(d.getEstado());
            c.setCep(d.getCep());
            c.setFone(d.soNumeros(d.getFone()));
            c.setObservacoes(d.getObservacoes());

            //não salva o comprovante agora!
            //c.setComprovante(d.getComprovante());

            c = repository.save(c);

            //verifica se o estabelecimento que veio da chamada tem base64
            if (d.getComprovante().contains("base64,")) {
                //salva a imagem base64 no firebase
                UploadInput firebase = new UploadInput();
                firebase.setFilename("estabelecimento/" + c.getId());
                firebase.setMimeType("image/jpg");
                firebase.setBase64(d.getComprovante().replace("base64,", ""));
                String urlComprovante = firebaseStorageService.upload(firebase);

                c.setComprovante(urlComprovante);

                c = repository.save(c);
            }

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
