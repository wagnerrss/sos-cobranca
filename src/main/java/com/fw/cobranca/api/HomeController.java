package com.fw.cobranca.api;

import com.fw.cobranca.service.*;
import com.fw.cobranca.util.Util;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cobranca")
public class HomeController implements Util {

    @Autowired
    HomeService homeService;

    @GetMapping("/home/{id_usuario}")
    public ResponseEntity getDadosHome(@PathVariable("id_usuario") Integer id_usuario) {
        return ResponseEntity.ok(homeService.getDadosHome(id_usuario));
    }


}
