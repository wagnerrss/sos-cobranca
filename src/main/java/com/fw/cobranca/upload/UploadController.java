package com.fw.cobranca.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UploadController {

    @Autowired
    private FirebaseStorageService uploadService;

//    @PostMapping("/upload")
//    public ResponseEntity upload(@RequestParam String fileName, @RequestParam String base64){
//        String s = "filename: " + fileName + " >> base64 > " + base64;
//
//        return ResponseEntity.ok(s);
//    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/api/v1/image/upload")
    public ResponseEntity upload(@RequestBody UploadInput uploadInput) throws IOException {

        String url = uploadService.upload(uploadInput);

        return ResponseEntity.ok(new UploadOutput(url));
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/api/v1/image/uploadList")
    public ResponseEntity upload(@RequestBody List<UploadInput> uploadInput) throws IOException {

        List<String> url = new ArrayList<>();

        for (UploadInput upload : uploadInput) {
            url.add(uploadService.upload(upload));
        }

        return ResponseEntity.ok(url);
    }

    @CrossOrigin
    @GetMapping("/api/v1/image/delete/{grupo}/{imagem}")
    public ResponseEntity delete(@PathVariable("grupo") String grupo, @PathVariable("imagem") String imagem) throws IOException {
        return ResponseEntity.ok(uploadService.delete(grupo + "/" + imagem));
    }
}
