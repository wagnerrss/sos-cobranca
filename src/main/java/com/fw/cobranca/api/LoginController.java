package com.fw.cobranca.api;

import com.fw.cobranca.domain.*;
import com.fw.cobranca.service.*;
import com.fw.cobranca.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/cobranca")
public class LoginController implements Util {

    @Autowired
    LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity postLogin(@RequestBody Map login) {
        try {
            Usuario u = loginService.findUser(login);

            if ((u == null) || (u.getId() <= 0)) {
                return ResponseEntity.badRequest().body("Usuário não encontrado!");
            }

            return ResponseEntity.ok(u);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Usuário não encontrado!");
        }
    }
}
