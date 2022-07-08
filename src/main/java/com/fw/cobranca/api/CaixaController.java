package com.fw.cobranca.api;

import com.fw.cobranca.domain.*;
import com.fw.cobranca.domain.dto.EstabelecimentoDTO;
import com.fw.cobranca.service.*;
import com.fw.cobranca.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/cobranca")
public class CaixaController implements Util {

    @Autowired
    CobrancaController cobrancaController;

    @Autowired
    CaixaService caixaService;

    @GetMapping("/caixa/{dias}/{id_usuario}")
    public ResponseEntity getCaixaPorDiaUsuario(@PathVariable("dias") Integer dias, @PathVariable("id_usuario") Integer id_usuario) {
        return ResponseEntity.ok(caixaService.getPorDiaUsuario(dias, id_usuario));
    }

    @PostMapping("/caixa")
    public ResponseEntity postCaixa(@RequestBody Map map) {
        try {
            Caixa d = new Caixa(map);
            Caixa c = caixaService.insert(d);

            return c != null ?
                    new ResponseEntity<>(c, HttpStatus.CREATED) :
                    new ResponseEntity<>(cobrancaController.mapErro("POST", ""), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(cobrancaController.mapErro("POST", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/caixa/{id}")
    public ResponseEntity putCaixa(@PathVariable("id") Integer id, @RequestBody Map map) {
        try {
            Caixa d = new Caixa(map);

            d.setId(id);
            Caixa c = caixaService.update(id, d);

            return c != null ? ResponseEntity.ok(c) : new ResponseEntity<>(cobrancaController.mapErro("PUT", ""), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(cobrancaController.mapErro("PUT", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/caixa/{id}")
    public ResponseEntity deleteCaixa(@PathVariable("id") Integer id) {
        boolean ok = caixaService.delete(id);

        return ok ? ResponseEntity.ok().build() : new ResponseEntity<>(cobrancaController.mapErro("DELETE", ""), HttpStatus.BAD_REQUEST);
    }

}
