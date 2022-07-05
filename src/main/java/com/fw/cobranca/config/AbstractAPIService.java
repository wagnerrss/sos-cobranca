package com.fw.cobranca.config;

import org.springframework.http.HttpHeaders;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Spring multi-tenancy implementation.
 * <p>
 * Abstract API service implementation.
 *
 * @author Ranjith Manickam
 * @since 1.0
 */
public abstract class AbstractAPIService {

    /**
     * Locale Brasileiro
     */
    private static final Locale BRAZIL = new Locale("pt", "BR");
    /**
     * Sï¿½mbolos especificos do Real Brasileiro
     */
    private static final DecimalFormatSymbols REAL = new DecimalFormatSymbols(BRAZIL);
    /**
     * Mascara de dinheiro para Real Brasileiro
     */
    public static final DecimalFormat DINHEIRO_REAL = new DecimalFormat("###,###,##0.00", REAL);

    /**
     * To get response headers
     *
     * @return
     */
    protected HttpHeaders getResponseHeaders() {
        HttpHeaders headers = new HttpHeaders();
        return headers;
    }

    /**
     * To get response headers
     *
     * @param ex
     * @return
     */
    protected HttpHeaders getResponseHeaders(Exception ex) {
        HttpHeaders headers = getResponseHeaders();
        headers.add("Error", ex.getMessage());
        return headers;
    }

    protected String formataData(String data, String retorno) {
        try {
            if (data == null || data == "") {
                return "";
            }
            SimpleDateFormat formatServer = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatServerHour = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            SimpleDateFormat formatServerHourExact = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat formatClientBar = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat formatClientBarHour = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            SimpleDateFormat formatClientDash = new SimpleDateFormat("dd-MM-yyyy");

            Pattern p = Pattern.compile("(-)");
            Matcher m = p.matcher(data);
            String dataFormatada = "";

            if (m.find()) {
                switch (retorno) {
                    case "client":
                        dataFormatada = formatClientBar.format(formatServer.parse(data));
                        break;
                    case "clientHour":
                        dataFormatada = formatClientBarHour.format(formatServerHourExact.parse(data));
                        break;
                    case "server":
                        dataFormatada = formatServer.format(formatClientDash.parse(data));
                        break;
                    case "serverHour":
                        dataFormatada = formatServerHour.format(formatClientDash.parse(data));
                        break;
                    case "serverHourExact":
                        dataFormatada = formatServerHourExact.format(formatClientDash.parse(data));
                        break;
                }
            } else {
                switch (retorno) {
                    case "client":
                        dataFormatada = formatClientBar.format(formatServer.parse(data));
                        break;
                    case "clientHour":
                        dataFormatada = formatClientBarHour.format(formatServerHourExact.parse(data));
                        break;
                    case "server":
                        dataFormatada = formatServer.format(formatClientBar.parse(data));
                        break;
                    case "serverHour":
                        dataFormatada = formatServerHour.format(formatClientBar.parse(data));
                        break;
                    case "serverHourExact":
                        dataFormatada = formatServerHourExact.format(formatClientBarHour.parse(data));
                        break;
                }

            }
            return dataFormatada;
        } catch (Exception ex) {
            return ex.toString();
        }
    }

    public static Date fRetornaProximoDiaUtil(Date oData) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(oData);
        while (fVerificaFeriadoFimDeSemana(oData)) {

            calendar.add(Calendar.DAY_OF_MONTH, 1);
            oData = calendar.getTime();
            System.out.println(calendar.getTime().toString());
        }
        return calendar.getTime();
    }

    public static boolean fVerificaFeriadoFimDeSemana(Date oData) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(oData);
        Integer oDia;
        Integer oMes;
        Integer oAno;
        Integer oDiaDaSemana, i;
        List<String> tFeriado = new ArrayList<>();
        tFeriado.add("frCarnaval");
        tFeriado.add("frQuartaCinzas");
        tFeriado.add("frSextaSanta");
        tFeriado.add("frCorpusChristi");
        boolean Result = false;

        // VERIFICA FIM DE SEMANA
        oDiaDaSemana = calendar.get(Calendar.DAY_OF_WEEK);
        if (oDiaDaSemana == 1 || oDiaDaSemana == 7) {
            return true;
        } else {
            oAno = calendar.get(Calendar.YEAR);
            oMes = calendar.get(Calendar.MONTH) + 1;
            oDia = calendar.get(Calendar.DAY_OF_MONTH);
            // VERIFICA FERIADOS FIXOS
            if ((oDia == 01) && (oMes == 01)) {
                return true;
            }
            //21/04 - Tiradentes
            if ((oDia == 21) && (oMes == 04)) {
                return true;
            }
            //21/04 - Tiradentes
            if ((oDia == 21) && (oMes == 04)) {
                return true;
            }
            //01/05 - Dia do Trabalhador
            if ((oDia == 01) && (oMes == 05)) {
                return true;
            }
            //07/09 - Dia da Independência do Brasil
            if ((oDia == 07) && (oMes == 9)) {
                return true;
            }
            //12/10 - Nossa Senhora Aparecida
            if ((oDia == 12) && (oMes == 10)) {
                return true;
            }
            //02/11 - Finados
            if ((oDia == 02) && (oMes == 11)) {
                return true;
            }
            //15/11 - Proclamação da República
            if ((oDia == 15) && (oMes == 11)) {
                return true;
            }
            //25/12 - Natal
            if ((oDia == 25) && (oMes == 12)) {
                return true;
            }

            for (int j = 0; j < 4; j++) {
                if (oData == CalculaFeriado(oAno, tFeriado.get(j))) {
                    return true;
                }
            }

        }

        return false;

    }

    public static Date CalculaFeriado(Integer AAno, String ATipo) {
        Date Aux = CalculaPascoa(AAno);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Aux);

        if (ATipo.equals("frCarnaval")) {
            calendar.add(Calendar.DAY_OF_MONTH, -50);
        } else if (ATipo.equals("frQuartaCinzas")) {
            calendar.add(Calendar.DAY_OF_MONTH, -46);
        } else if (ATipo.equals("frSextaSanta")) {
            calendar.add(Calendar.DAY_OF_MONTH, -2);
        } else if (ATipo.equals("frCorpusChristi")) {
            calendar.add(Calendar.DAY_OF_MONTH, 60);
        }

        Aux = calendar.getTime();

        return Aux;

    }

    public static Date CalculaPascoa(Integer AAno) {
        Integer R1, R2, R3, R4, R5;
        Date FPascoa;
        Integer VJ, VM, VD;
        Calendar c = Calendar.getInstance();

        R1 = AAno % 19;
        R2 = AAno % 4;
        R3 = AAno % 7;
        R4 = (19 * R1 + 24) % 30;
        R5 = (6 * R4 + 4 * R3 + 2 * R2 + 5) % 7;

//        R1 := AAno mod 19;
//        R2 := AAno mod 4;
//        R3 := AAno mod 7;
//        R4 := (19 * R1 + 24) mod 30;
//        R5 := (6 * R4 + 4 * R3 + 2 * R2 + 5) mod 7;
        c.set(AAno, 2, 22, 0, 0);

        c.add(Calendar.DAY_OF_MONTH, R4 + R5);

        FPascoa = c.getTime();

        VJ = c.get(Calendar.YEAR);
        VM = c.get(Calendar.MONTH);
        VD = c.get(Calendar.DAY_OF_MONTH);

        switch (VD) {
            case 26:
                c = new GregorianCalendar(AAno, 3, 19);
                FPascoa = c.getTime();

                break;
            case 25:
                c = new GregorianCalendar(AAno, 3, 18);
                FPascoa = c.getTime();
                break;
        }

        return FPascoa;

    }

    public List<String> refatoraListaCartoes(String value) {
        String result = "";
        int count = 0;
        List<String> listaCartoes = new LinkedList<>();
        for (int i = 0; i < value.length(); i++) {
            result += value.charAt(i);
            count++;
            if (count == 3) {
                listaCartoes.add(result);
                result = "";
                count = 0;
            }
        }
        return listaCartoes;
    }

    public List<String> returnStringMoedaValida(String moevld) {
        String retorno = "";
        List<String> listMoedasValidas = new LinkedList<>();
        for (int i = 0; i < moevld.length(); i++) {
            if (i == 0) {
                retorno += moevld.charAt(i);
            } else if (i % 2 != 0) {
                retorno += moevld.charAt(i);
                listMoedasValidas.add(retorno);
                retorno = "";
            } else {
                retorno += moevld.charAt(i);
            }
        }
        return listMoedasValidas;
    }

    public String returnString(String moevld) {
        String retorno = "";

        for (int i = 0; i < moevld.length(); i++) {
            if (i == 0) {
                retorno += moevld.charAt(i);
            } else if (i % 2 != 0) {
                retorno += moevld.charAt(i);
                retorno += ",";
            } else {
                retorno += moevld.charAt(i);
            }

        }
        String modificada = retorno.substring(0, retorno.length() - 1);
        return modificada;
    }

    protected String stringNull(Object request) {
        return request == null ? "" : request.toString();
    }

    protected Integer integerNull(Object request) {
        return request == null ? 0 : (Integer) request;
    }

    protected Double doubleNull(Object request) {
        return request == null ? 0 : (Double) request;
    }

    protected BigDecimal bigDecimalNull(Object request) {
        return request == null ? BigDecimal.valueOf(0.0) : (BigDecimal) request;
    }

    protected Float floatNull(Object request) {
        return request == null ? 0f : (Float) request;
    }

    protected static String mascaraDinheiro(double valor) {
        return DINHEIRO_REAL.format(valor);
    }

}