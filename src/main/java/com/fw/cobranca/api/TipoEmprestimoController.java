package com.fw.cobranca.api;

import com.fw.cobranca.domain.TipoEmprestimo;
import com.fw.cobranca.service.TipoEmprestimoService;
import com.fw.cobranca.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cobranca")
public class TipoEmprestimoController implements Util {

    @Autowired
    TipoEmprestimoService tipoEmprestimoService;

    @GetMapping("/tipo_emprestimo")
    public ResponseEntity<Iterable<TipoEmprestimo>> getTipoEmprestimo() {
        return ResponseEntity.ok(tipoEmprestimoService.getTipoEmprestimos());
    }

    @GetMapping("/tipo_emprestimo/tipo/{tipo}")
    public ResponseEntity<Iterable<TipoEmprestimo>> getTipoEmprestimoPorTipo(@PathVariable("tipo") Integer tipo) {
        return ResponseEntity.ok(tipoEmprestimoService.getTipoEmprestimosPorTipo(tipo));
    }

}
