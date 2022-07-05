package com.fw.cobranca.api;

import com.fw.cobranca.service.HomeService;
import com.fw.cobranca.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cobranca")
public class HomeController implements Util {

    @Autowired
    HomeService homeService;

    @Autowired
    CobrancaController cobrancaController;

    @GetMapping("/home/{id_usuario}")
    public ResponseEntity getDadosHome(@PathVariable("id_usuario") Integer id_usuario) throws Exception {
        try {
            return ResponseEntity.ok(homeService.getDadosHome(id_usuario));
        } catch (Exception ex) {
            return new ResponseEntity<>(cobrancaController.mapErro("GET", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }


}
