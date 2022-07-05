package com.fw.cobranca.api;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class IndexController {

    @GetMapping
    public String get(){
        return "Get API FW para cobranca";
    }
    @PostMapping
    public String post(){
        return "Post API FW para cobranca";
    }
    @PutMapping
    public String put(){
        return "Put API FW para cobranca";
    }
    @DeleteMapping
    public String delete(){
        return "Delete API FW para cobranca";
    }
}
