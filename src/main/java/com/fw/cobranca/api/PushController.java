package com.fw.cobranca.api;

import com.fw.cobranca.service.PushService;
import com.fw.cobranca.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/cobranca")
public class PushController implements Util {

    @Autowired
    CobrancaController cobrancaController;

    @Autowired
    PushService pushService;

    @PostMapping("/push")
    public ResponseEntity postPush(@RequestBody Map map) {
        try {
            String documento = toStr(map.get("documento"));
            String titulo = toStr(map.get("titulo"));
            String mensagem = toStr(map.get("mensagem"));

            Map resp = pushService.enviarPush(documento, titulo, mensagem);

            return resp != null ? ResponseEntity.ok(resp) : new ResponseEntity<>(cobrancaController.mapErro("PUT", ""), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(cobrancaController.mapErro("POST", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

}
