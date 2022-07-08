package com.fw.cobranca.api;

import com.fw.cobranca.domain.*;
import com.fw.cobranca.domain.dto.EstabelecimentoDTO;
import com.fw.cobranca.service.*;
import com.fw.cobranca.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/cobranca")
public class EstabelecimentoController implements Util {

    @Autowired
    CobrancaController cobrancaController;

    @Autowired
    EstabelecimentoService estabelecimentoService;

    @GetMapping("/estabelecimento")
    public ResponseEntity<Iterable<Estabelecimento>> getEstabelecimento() {
        return ResponseEntity.ok(estabelecimentoService.getEstabelecimentos());
    }

    @GetMapping("/estabelecimento/{id}")
    public ResponseEntity<EstabelecimentoDTO> getEstabelecimentoById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(estabelecimentoService.getEstabelecimentosById(id));
    }

    @PostMapping("/estabelecimento")
    public ResponseEntity postEstabelecimento(@RequestBody Estabelecimento d) {
        try {
            Estabelecimento c = estabelecimentoService.insert(d);

            return c != null ?
                    new ResponseEntity<>(c, HttpStatus.CREATED) :
                    new ResponseEntity<>(cobrancaController.mapErro("POST", ""), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(cobrancaController.mapErro("POST", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/estabelecimento/{id}")
    public ResponseEntity putEstabelecimento(@PathVariable("id") Integer id, @RequestBody Map m) {
        try {
            Estabelecimento d = new Estabelecimento(m);

            d.setId(id);

            Estabelecimento c = estabelecimentoService.update(id, d);

            return c != null ?
                    ResponseEntity.ok(c) :
                    new ResponseEntity<>(cobrancaController.mapErro("PUT", ""), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(cobrancaController.mapErro("PUT", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/estabelecimento/{id}")
    public ResponseEntity deleteEstabelecimento(@PathVariable("id") Integer id) {
        boolean ok = estabelecimentoService.delete(id);

        return ok ?
                ResponseEntity.ok().build() :
                new ResponseEntity<>(cobrancaController.mapErro("DELETE", ""), HttpStatus.BAD_REQUEST);
    }

}
