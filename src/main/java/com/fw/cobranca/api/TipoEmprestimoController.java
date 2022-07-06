package com.fw.cobranca.api;

import com.fw.cobranca.domain.TipoEmprestimo;
import com.fw.cobranca.service.*;
import com.fw.cobranca.util.Util;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cobranca")
public class TipoEmprestimoController implements Util {

    @Autowired
    TipoEmprestimoService tipoEmprestimoService;

    @GetMapping("/tipo_emprestimo")
    public ResponseEntity<Iterable<TipoEmprestimo>> getTipoEmprestimo() {
        return ResponseEntity.ok(tipoEmprestimoService.getTipoEmprestimos());
    }

}
