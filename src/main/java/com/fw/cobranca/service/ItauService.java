package com.fw.cobranca.service;

import com.fw.cobranca.util.RestUtil;
import com.fw.cobranca.util.Util;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItauService implements Util {

    @Autowired
    private RestUtil restUtil;

    public String geraToken(Map value) {

        String body = "grant_type=client_credentials"
                + "&client_id=" + toStr(value.get("client_id"))
                + "&client_secret=" + toStr(value.get("secret_key"));

        Map resultToken = restUtil.postItau(value, true, "", body, "");

        return toStr(resultToken.get("access_token"));
    }


    public Map geraCobranca(Map value) {

        Gson gson = new Gson();

        Map body = new LinkedHashMap();

        body.put("calendario", new HashMap<>());

        Map original = new HashMap();
        original.put("original", toFloat(value.get("valor")).toString());

        body.put("valor", original);
        body.put("chave", toStr(value.get("chave")));

        Map resultQrcode = new HashMap();
        resultQrcode.put("imagem_base64", "");
        resultQrcode.put("txid", "");
        resultQrcode.put("erro", "");

        Map resultCob = restUtil.postItau(value, false, "cob", gson.toJson(body), geraToken(value));

        if (resultCob.isEmpty()) {
            resultQrcode.put("erro", "Erro ao gerar cobrança!");
        } else {

            String params = "cob/" + toStr(resultCob.get("txid")) + "/qrcode";

            resultQrcode = restUtil.getItau(value, params, geraToken(value));

            if (resultQrcode.isEmpty()) {
                resultQrcode.put("erro", "Erro ao gerar QRCode!");
            } else {
                resultQrcode.put("txid", toStr(resultCob.get("txid")));
                resultQrcode.put("erro", resultCob.get("message") != null ? resultCob.get("message") : "");
            }
        }

        return resultQrcode;
    }

    public Map verificaPagamento(Map value){

        Map result = restUtil.getItau(value,  "cob/" + toStr(value.get("txid")), geraToken(value));

        Map retorno = new HashMap();
        retorno.put("pago", false);
        retorno.put("autent", "");
        retorno.put("erro", "");

        if (result.isEmpty()) {
            retorno.put("erro", "Erro ao verificar pagamento!");
        } else {

            retorno.put("pago", toStr(result.get("status")).equals("CONCLUIDA"));

            if (toStr(result.get("status")).equals("CONCLUIDA") && result.get("pix") != null) {

                for (Map pix : (List<Map>) result.get("pix")) {
                    retorno.put("autent", pix.get("endToEndId")); //ID de ponta a ponta
                    break;
                }
            }
        }

        return  retorno;
    }
}
