package com.fw.cobranca.api;

import com.fw.cobranca.domain.*;
import com.fw.cobranca.service.*;
import com.fw.cobranca.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/cobranca")
public class UsuarioController implements Util {

    @Autowired
    CobrancaController cobrancaController;

    @Autowired
    CaixaController caixaController;

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/usuario")
    public ResponseEntity<Iterable<Usuario>> getUsuario() {
        return ResponseEntity.ok(usuarioService.getUsuarios());
    }

    @GetMapping("/usuario/busca_id/{id}")
    public ResponseEntity getUsuarioById(@PathVariable("id") Integer id) {
        Optional<Usuario> c = usuarioService.getById(id);

        return c.isPresent() ?
                ResponseEntity.ok(c.get()) :
                new ResponseEntity<>(cobrancaController.mapErro("GET", ""), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/usuario/busca_nome/{nome}")
    public ResponseEntity getUsuarioByNome(@PathVariable("nome") String nome) {
        Iterable<Usuario> c = usuarioService.getUsuariosPorNomeAproximado(nome);

        return ResponseEntity.ok(c);
    }

    @GetMapping("/usuario/busca_tipo/{tipo}")
    public ResponseEntity getUsuarioByTipo(@PathVariable("tipo") String tipo) {
        Iterable<Usuario> c = usuarioService.getUsuariosPorTipo(tipo);

        return ResponseEntity.ok(c);
    }

    @GetMapping("/usuario/busca_nome_tipo/{nome}/{tipo}")
    public ResponseEntity getUsuarioByNome(@PathVariable("nome") String nome, @PathVariable("tipo") String tipo) {
        String[] arrTipo = {"A", "C", "U"};

        Iterable<Usuario> c = (Arrays.asList(arrTipo).indexOf(tipo) >= 0) ?
                usuarioService.getUsuariosPorNomeAproximadoETipo(nome, tipo) :
                usuarioService.getUsuariosPorNomeAproximado(nome);

        return ResponseEntity.ok(c);
    }

    @PostMapping("/usuario")
    public ResponseEntity postUsuario(@RequestBody Usuario d) {
        try {
            Usuario c = usuarioService.insert(d);

            return c != null ?
                    new ResponseEntity<>(c, HttpStatus.CREATED) :
                    new ResponseEntity<>(cobrancaController.mapErro("POST", ""), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(cobrancaController.mapErro("POST", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/usuario/{id}")
    public ResponseEntity putUsuario(@PathVariable("id") Integer id, @RequestBody Map map) {
        try {
            Map m = new LinkedHashMap();
            Map x = new LinkedHashMap();

            if (map.containsKey("usuario")) {
                m = (Map) map.get("usuario");

                if (map.containsKey("caixa")) {
                    x = (Map) map.get("caixa");
                }
            } else {
                m = map;
            }

            Usuario d = new Usuario(m);

            d.setId(id);
            Usuario c = usuarioService.update(id, d);

            if (!x.isEmpty()) {
                caixaController.postCaixa(x);
            }

            return c != null ?
                    ResponseEntity.ok(c) :
                    new ResponseEntity<>(cobrancaController.mapErro("PUT", ""), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(cobrancaController.mapErro("PUT", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/usuario/{id}")
    public ResponseEntity deleteUsuario(@PathVariable("id") Integer id) {
        boolean ok = usuarioService.delete(id);

        return ok ?
                ResponseEntity.ok().build() :
                new ResponseEntity<>(cobrancaController.mapErro("DELETE", ""), HttpStatus.BAD_REQUEST);
    }
}
