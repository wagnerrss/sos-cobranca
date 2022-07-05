package com.fw.cobranca.service;

import com.fw.cobranca.util.RestUtil;
import com.fw.cobranca.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PushService implements Util {
   @Autowired
   RestUtil restUtil;

   public Map enviarPush(String documento, String titulo, String mensagem){
      return restUtil.postPush(documento, titulo, mensagem);
   }
}
