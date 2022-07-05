package com.fw.cobranca.api;

import com.fw.cobranca.domain.ColaboradorRegiao;
import com.fw.cobranca.domain.dto.RegiaoDTO;
import com.fw.cobranca.service.ColaboradorRegiaoService;
import com.fw.cobranca.service.RegiaoService;
import com.fw.cobranca.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cobranca")
public class RegiaoController implements Util {

    @Autowired
    CobrancaController cobrancaController;

    @Autowired
    RegiaoService regiaoService;

    @Autowired
    ColaboradorRegiaoService colaboradorRegiaoService;

    @GetMapping("/regiao")
    public ResponseEntity getRegiao() {
        return ResponseEntity.ok(regiaoService.getRegioesUsuario());
    }

    @PostMapping("/regiao_usuario")
    public ResponseEntity postRegiaoColaborador(@RequestBody Map map) {

        try {
            Integer idRegiao = toInt(map.get("id_regiao"));
            List<Integer> usuarios = (List) map.get("usuarios");

            colaboradorRegiaoService.deleteByIdRegiao(idRegiao);

            usuarios.forEach(idUsuario -> {
                ColaboradorRegiao c = colaboradorRegiaoService.insert(idRegiao, idUsuario);
            });

            RegiaoDTO regiaoDTO = regiaoService.getById(idRegiao);

            colaboradorRegiaoService.notificaColaboradorRegiao(idRegiao);

            return regiaoDTO != null ?
                    new ResponseEntity<>(regiaoDTO, HttpStatus.CREATED) :
                    new ResponseEntity<>(cobrancaController.mapErro("POST", ""), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(cobrancaController.mapErro("POST", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
