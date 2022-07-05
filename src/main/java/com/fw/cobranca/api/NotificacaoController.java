package com.fw.cobranca.api;

import com.fw.cobranca.domain.Notificacao;
import com.fw.cobranca.service.NotificacaoService;
import com.fw.cobranca.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/cobranca")
public class NotificacaoController implements Util {

    @Autowired
    NotificacaoService notificacaoService;

    @Autowired
    CobrancaController cobrancaController;

    @GetMapping("/notificacao/{id_usuario}")
    public ResponseEntity getNotificacaoByIdUsuario(@PathVariable("id_usuario") Integer idUsuario) {
        Iterable<Notificacao> n = notificacaoService.getByIdUsuario(idUsuario);

        return ResponseEntity.ok(n);
    }

    @PostMapping("/notificacao")
    public ResponseEntity postNotificacao(@RequestBody Notificacao d) {
        try {
            Notificacao c = notificacaoService.insert(d);

            return c != null ? new ResponseEntity<>(c, HttpStatus.CREATED) : new ResponseEntity<>(cobrancaController.mapErro("POST", ""), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(cobrancaController.mapErro("POST", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/notificacao/{id}")
    public ResponseEntity putNotificacao(@PathVariable("id") Integer id, @RequestBody Map map) {
        try {
            Notificacao d = new Notificacao(map);

            d.setId(id);
            Notificacao c = notificacaoService.update(id, d);

            return c != null ? ResponseEntity.ok(c) : new ResponseEntity<>(cobrancaController.mapErro("PUT", ""), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(cobrancaController.mapErro("PUT", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/notificacao/{id}")
    public ResponseEntity deleteNotificacao(@PathVariable("id") Integer id) {
        boolean ok = notificacaoService.delete(id);

        return ok ? ResponseEntity.ok().build() : new ResponseEntity<>(cobrancaController.mapErro("DELETE", ""), HttpStatus.BAD_REQUEST);
    }
}
