package com.fw.cobranca.api;

import com.fw.cobranca.domain.Parcela;
import com.fw.cobranca.domain.dto.ParcelaHomeDTO;
import com.fw.cobranca.service.*;
import com.fw.cobranca.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/cobranca")
public class ParcelaController implements Util {

    @Autowired
    CobrancaController cobrancaController;

    @Autowired
    ParcelaService parcelaService;

    @GetMapping("/parcela")
    public ResponseEntity getParcelaPorEmprestimo(@RequestParam("id_emprestimo") Integer idEmprestimo) {
        return ResponseEntity.ok(parcelaService.getPorEmprestimo(idEmprestimo));
    }

    @PutMapping("/parcela/{id}")
    public ResponseEntity putParcela(@PathVariable("id") Integer id, @RequestBody Map m) {
        try {
            Parcela d = new Parcela(m);
            ParcelaHomeDTO c = parcelaService.update(id, d);

            return c != null ?
                    ResponseEntity.ok(c) :
                    new ResponseEntity<>(cobrancaController.mapErro("PUT", ""), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(cobrancaController.mapErro("PUT", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/parcela/cancelamento/{id}")
    public ResponseEntity putCancelamentoParcela(@PathVariable("id") Integer id) {
        try {

            ParcelaHomeDTO c = parcelaService.cancelamento(id);

            return c != null ?
                    ResponseEntity.ok(c) :
                    new ResponseEntity<>(cobrancaController.mapErro("PUT", ""), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(cobrancaController.mapErro("PUT", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
