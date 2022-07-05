package com.fw.cobranca.api;

import com.fw.cobranca.service.ItauService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cobranca")
public class ItauController {

    @Autowired
    private ItauService itauService;

    @GetMapping("/pix/itau/ativo")
    public ResponseEntity<String> verificaConexao() {

        try {
            return new ResponseEntity<String>("ATIVO", HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<String>("ATIVO", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/pix/itau/realizaPix")
    public ResponseEntity<Map> pix(@RequestBody Map value) {

        /*
                {
                    "client_id" : "0895fa90-9ce5-4b42-96e3-6640b891fb35",
                    "secret_key" : "05dae797-8026-489d-a8ef-4f64d87ead9f",
                    "chave" : "05961295000186",
                    "cert_pass" : "123456",
                    "valor" : 1.99
                }

         */

        try {
            return new ResponseEntity<Map>(itauService.geraCobranca(value), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<Map>(new HashMap(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/pix/itau/consultaPix")
    public ResponseEntity<Map> consultaPix(@RequestBody Map value) {

        /*
                {
                    "client_id" : "0895fa90-9ce5-4b42-96e3-6640b891fb35",
                    "secret_key" : "05dae797-8026-489d-a8ef-4f64d87ead9f",
                    "chave" : "05961295000186",
                    "cert_pass" : "123456",
                    "txid" : 1.99
                }
         */

        try {
            return new ResponseEntity<Map>(itauService.verificaPagamento(value), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<Map>(new HashMap(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/pix/itau/geraToken")
    public ResponseEntity<String> geraToken(@RequestBody Map value) {

            /*
                    {                                                                         
                        "client_id" : "d7d8f335-0ebe-40f6-b907-1934f976747f",
                        "secret_key" : "436d4f31-98dc-4ff4-af6c-55400d8b2b47",
                        "chave" : "12507741000142",
                        "cert_pass" : "123456"",
                    }
             */

        try {
            return new ResponseEntity<String>(itauService.geraToken(value), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<String>("", HttpStatus.BAD_REQUEST);
        }
    }


}
