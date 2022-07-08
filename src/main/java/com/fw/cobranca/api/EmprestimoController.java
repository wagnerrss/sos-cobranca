package com.fw.cobranca.api;

import com.fw.cobranca.domain.Emprestimo;
import com.fw.cobranca.domain.dto.EmprestimoDTO;
import com.fw.cobranca.service.EmprestimoService;
import com.fw.cobranca.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/cobranca")
public class EmprestimoController implements Util {

    @Autowired
    CobrancaController cobrancaController;

    @Autowired
    EmprestimoService emprestimoService;

    @GetMapping("/emprestimo/{id}")
    public ResponseEntity getById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(emprestimoService.getById(id));
    }

    @GetMapping("/emprestimo")
    public ResponseEntity getByIdEstabelecimento(@RequestParam("id_estabelecimento") Integer id_estabelecimento) {
        return ResponseEntity.ok(emprestimoService.getByIdEstabelecimento(id_estabelecimento));
    }

    @PostMapping("/emprestimo")
    public ResponseEntity postEmprestimo(@RequestBody Map map) {
        try {
            Emprestimo d = new Emprestimo(map);
            Emprestimo c = emprestimoService.insert(d);

            return c != null ?
                    new ResponseEntity<>(c, HttpStatus.CREATED) :
                    new ResponseEntity<>(cobrancaController.mapErro("POST", ""), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(cobrancaController.mapErro("POST", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/emprestimo/{id}")
    public ResponseEntity putEmprestimo(@PathVariable("id") Integer id, @RequestBody Map m) {
        try {
            Emprestimo d = new Emprestimo(m);

            d.setId(id);

            EmprestimoDTO c = emprestimoService.update(id, d);

            return c != null ?
                    ResponseEntity.ok(c) :
                    new ResponseEntity<>(cobrancaController.mapErro("PUT", ""), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(cobrancaController.mapErro("PUT", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/emprestimo/{id}")
    public ResponseEntity deleteEmprestimo(@PathVariable("id") Integer id) {
        boolean ok = emprestimoService.delete(id);

        return ok ?
                ResponseEntity.ok().build() :
                new ResponseEntity<>(cobrancaController.mapErro("DELETE", ""), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/emprestimo/verifica")
    public ResponseEntity getVerificaEmprestimoEmAberto(@RequestParam("id_estabelecimento") Integer idEstabelecimento) {
        try {
            return ResponseEntity.ok(emprestimoService.verificaEmprestimoEmAberto(idEstabelecimento));
        } catch (Exception ex) {
            return new ResponseEntity<>(cobrancaController.mapErro("GET", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }


}
