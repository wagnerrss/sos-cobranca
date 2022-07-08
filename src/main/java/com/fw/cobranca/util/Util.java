package com.fw.cobranca.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.*;
import java.util.*;
import java.util.regex.Pattern;

public interface Util {

    public default boolean ehFeriado() {
        GregorianCalendar calendar = new GregorianCalendar();

        Integer dai = calendar.get(Calendar.DAY_OF_MONTH);
        Integer mes = calendar.get(Calendar.MONTH) + 1;


        return (dai == 1 && mes == 1) || (dai == 24 && mes == 2) || (dai == 25 && mes == 2) || (dai == 10 && mes == 4) || (dai == 1 && mes == 5)
                || (dai == 11 && mes == 6) || (dai == 7 && mes == 9) || (dai == 12 && mes == 10) || (dai == 15 && mes == 11) || (dai == 25 && mes == 12);


    }

    public default long diferencaDiasData(Date d1, Date d2) {

        if (d1 == null || d2 == null) {
            return -1;
        }

        long dt = (d1.getTime() - d2.getTime()) + 3600000; // 1 hora para compensar horário de verão
        long dias = dt / 86400000L;

        return dias;

    }

    public enum OprEtiquetas {
        COMPRAR_FRETES("checkout"),
        PRE_VISUALIZAR("preview"),
        GERAR("generate"),
        IMPRIMIR("print"),
        VERIFICA_CANCELAMENTO("cancellable"),
        CANCELAR("cancel"),
        RASTREIO("tracking");

        public String strOprEtiquetas;

        OprEtiquetas(String operacao) {
            strOprEtiquetas = operacao;
        }
    }

    //status da ordem (integração cnova)
    public enum OrderStatus {
        NEW("new"),
        APPROVED("approved"),
        CANCELED("canceled"),
        DELIVERED("delivered"),
        PARTIALLY_DELIVERED("partiallyDelivered"),
        PARTIALLY_SENT("partiallySent"),
        SENT("sent");

        public String strOrderStatus;

        OrderStatus(String status) {
            strOrderStatus = status;
        }
    }

    //operações da order trackings (integração cnova)
    public enum OrderTrackingsOpr {
        CANCEL("cancel"),
        DELIVERED("delivered"),
        EXCHANGE("exchange"),
        RETURN("return"),
        SENT("sent");

        public String strOrderTrackingsOpr;

        OrderTrackingsOpr(String opr) {
            strOrderTrackingsOpr = opr;
        }
    }

    public default String formatarDecimais(Float aValue) {
        DecimalFormat df = new DecimalFormat("0.00"); //formatação do float para 2 casas

        String xResult = df.format(aValue);

        return xResult.replace(',', '.');
    }

    public default Map<String, Object> converteClasseParaMap(Object obj) {
        Map<String, Object> mapResult = new HashMap<>();

        ObjectMapper oMapper = new ObjectMapper();

        Map<String, Object> map = oMapper.convertValue(obj, new TypeReference<Map<String, Object>>() {
        });

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof Float) {
                mapResult.put(entry.getKey().toUpperCase(), (Float) entry.getValue());
            } else {
                if (entry.getValue() instanceof Double) {
                    mapResult.put(entry.getKey().toUpperCase(), ((Double) entry.getValue()).floatValue());
                } else {
                    mapResult.put(entry.getKey().toUpperCase(), entry.getValue());
                }
            }
        }

        return mapResult;
    }


    /**
     * Converte Objeto em Integer.
     *
     * @param Value Objeto a ser convertido
     */
    public default Integer toInt(Object Value) {

        try {

            if (Value != null) {
                return Integer.valueOf(Value.toString());
            } else {
                return 0;
            }

        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Converte Objeto em formato monetário '###,###,##0.00'.
     *
     * @param Value Objeto a ser convertido
     */
    public default Double toCurr(Object Value) {

        DecimalFormat f = new DecimalFormat("###,###,##0.00");

        try {

            if (Value != null) {               //Quando Value já é String necessário converter para Double pois gerar erros Format
                return Double.valueOf(f.format(Value instanceof String ? Double.valueOf((String) Value) : Value).replace(".", "").replace(',', '.'));
            } else {
                return 0d;
            }

        } catch (NumberFormatException e) {
            return 0d;
        }
    }

    /**
     * Converte Objeto em Double.
     */
    public default Double toDouble(Object Value) {

        try {

            if (Value != null) {
                return Double.valueOf(Value.toString().replace(',', '.'));
            } else {
                return 0d;
            }

        } catch (NumberFormatException e) {
            return 0d;
        }
    }

    /**
     * Converte Objeto em Float.
     */
    public default Float toFloat(Object Value) {

        try {

            if (Value != null) {
                return Float.valueOf(Value.toString().replace(',', '.'));
            } else {
                return 0f;
            }

        } catch (NumberFormatException e) {
            return 0f;
        }
    }

    /**
     * Converte Objeto em String.
     */
    public default String toStr(Object Value) {

        try {

            if (Value != null) {
                return Value.toString();
            } else {
                return "";
            }

        } catch (Exception e) {
            return "";
        }
    }

    public default List<Map> toList(Object value) {
        try {

            if (value != null) {
                return (List<Map>) value;
            } else {
                return new ArrayList<>();
            }

        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * Converte Objeto tipo Date/String. retorna data no padrão Calendar para
     * não haver divergência ao comparar. É zerado hh:mm:ss para evitar erros de
     * comparação: before, equals, after
     */
    public default Date toDate(Object Value) {

        Calendar calendar = Calendar.getInstance();

        try {
            if (Value != null) {

                calendar.setTime((Date) Value);

                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                return calendar.getTime();
            } else {
                return null;
            }
        } catch (Exception e) {
            if (toStr(Value).contains("/")) {
                try {
                    return new SimpleDateFormat("dd/MM/yyyy").parse(toStr(Value));
                } catch (Exception e1) {
                    e1.printStackTrace();
                    return null;
                }
            }

            if (toStr(Value).contains("-")) {
                try {
                    return new SimpleDateFormat("yyyy-MM-dd").parse(toStr(Value));
                } catch (Exception e2) {
                    e2.printStackTrace();
                    return null;
                }
            }

            e.printStackTrace();
            return null;
        }
    }

    public default String toParam(Object Value) {

        if (Value instanceof Number) {
            return Value.toString().replace(',', '.');
        } else if (Value instanceof String || Value instanceof Character) {
            return "'" + Value + "'";
        } else if (Value instanceof Date) {
            return "'" + formatDate("MM/dd/yyyy HH:mm", Value) + "'";
        } else {
            return "NULL";
        }
    }

    /**
     * Verificar se objeto existe no array
     *
     * @param Value  Objeto a ser verificado
     * @param Values Ex: contains(Objeto, new Object[]{123, 25, 15})
     * @return true ou false
     */
    public default Boolean contains(Object Value, Object[] Values) {

        try {

            for (Object I : Values) {

                if (I.equals(Value)) {
                    return true;
                }
            }

        } catch (Exception e) {
            return false;
        }

        return false;
    }

    /**
     * Concatenar String
     *
     * @param Values    Ex: concatStr(new Object[]{"JOSÉ", "MARIA"}, "-")
     * @param Delimiter Delimitador para separar as Strings
     * @return String concatenada
     */
    public default String concatStr(Object Values[], String Delimiter) {

        try {
            List<String> listStr = new ArrayList<>();

            for (Object Value : Values) {

                if ((Value != null) && (!Value.toString().trim().equals("")) && !listStr.contains(Value.toString())) {
                    listStr.add(Value.toString().trim());
                }
            }

            return listStr.isEmpty() ? "" : String.join(Delimiter != null ? Delimiter : "", listStr);

        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Formata Object (Float, Double...) em formato decimal ou monetário
     *
     * @param Format Formato a ser aplicado
     * @param Value  Valor a ser formatado
     * @return String formatada OBS: Format default '###,###,##0.00'
     */
    public default String formatCurr(String Format, Object Value) {

        try {
            DecimalFormat f = new DecimalFormat(Format.trim().equals("") ? "###,###,##0.00" : Format);

            return f.format(Value);

        } catch (Exception e) {
            return toStr(Value);
        }
    }

    /**
     * Formata Object (Float, Double...) e valor monetário com mascara
     *
     * @param Value Valor a ser formatado
     * @return String formatada com simbolo ex: "523.34" => "R$ 523,34"
     */
    public default String maskCurr(Object Value) {

        try {
            NumberFormat f = NumberFormat.getCurrencyInstance(); //Retorna a instância de um objeto com base no formato da localidade padrão.

            return f.format(Value instanceof Number ? Value : toDouble(Value)); //Só formata Object Number se não for necessário converter

        } catch (Exception e) {
            return toStr(Value);
        }
    }

    /**
     * Converte Objeto em Data e Hora, retorna data no padrão Calendar para não
     * haver divergência ao comparar
     */
    public default Date toDateTime(Object Value) {

        Calendar calendar = Calendar.getInstance();

        try {
            if (Value != null) {

                calendar.setTime((Date) Value);

                return calendar.getTime();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Incrementar/Decrementar dia de uma Data
     *
     * @param Value Data a ser manipulada
     * @param Days  Dia(s) a (+) incrementar/ (-) decrementar
     * @return Data e Hora manipulada.
     */
    public default Date incDay(Object Value, Integer Days) {

        try {
            Calendar calendar = Calendar.getInstance();

            calendar.setTime(toDateTime(Value));

            calendar.add(Calendar.DAY_OF_MONTH, Days);

            return calendar.getTime();

        } catch (Exception e) {
            return toDateTime(Value);
        }
    }

    public default Date stringToDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * Incrementar/Decrementar dia de uma Data
     *
     * @param Value  Data a ser manipulada
     * @param Months Mese(s) a (+) incrementar/ (-) decrementar
     * @return Data e Hora manipulada.
     */
    public default Date incMonth(Object Value, Integer Months) {

        try {
            Calendar calendar = Calendar.getInstance();

            calendar.setTime(toDateTime(Value));

            calendar.add(Calendar.DAY_OF_MONTH, Months);

            return calendar.getTime();

        } catch (Exception e) {
            return toDateTime(Value);
        }
    }

    /**
     * Incrementar/Decrementar dia de uma Data
     *
     * @param Value Data a ser manipulada
     * @param Years Ano(s) a (+) incrementar/ (-) decrementar
     * @return Data e Hora manipulada.
     */
    public default Date incYear(Object Value, Integer Years) {

        try {
            Calendar calendar = Calendar.getInstance();

            calendar.setTime(toDateTime(Value));

            calendar.add(Calendar.YEAR, Years);

            return calendar.getTime();

        } catch (Exception e) {
            return toDateTime(Value);
        }
    }

    /**
     * Calcula o intervalo em dias entre duas datas
     *
     * @param ValueIni         Data inicial
     * @param ValueFin         Data Final
     * @param negativeInterval Considera intervalo negativo
     * @return Total de dias entre as duas datas
     */
    public default Integer daysBetween(Object ValueIni, Object ValueFin, Boolean negativeInterval) {

        try {
            Days days = Days.daysBetween(new DateTime(ValueIni), new DateTime(ValueFin));

            return days.getDays() > 0 ? days.getDays() : (negativeInterval ? days.getDays() : days.getDays() * -1);

        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Formata Object (Date) em formato Data ou Data e Hora
     *
     * @param Format Formato a ser aplicado
     * @param Value  Valor a ser formatado
     * @return String formatada Format em branco, assim o método usa o padrão
     * OBS: Format default 'dd/MM/yyyy'
     */
    public default String formatDate(String Format, Object Value) {

        try {
            SimpleDateFormat f = new SimpleDateFormat(Format.trim().equals("") ? "dd/MM/yyyy" : Format);

            return f.format(toDate(Value));

        } catch (Exception e) {
            return toStr(Value);
        }
    }

    /**
     * Copy Função baseda no Delphi
     *
     * @param Value  Objeto String a ter a parte copiada
     * @param Pos    Posição Inicial
     * @param Length Quant. de caractere a ser copiado
     * @return Parte copiada da Strig
     */
    public default String Copy(Object Value, int Pos, int Length) {

        Value = (Value != null ? Value : "");

        if ((Pos + Length) > Value.toString().length()) {
            Value = Value.toString() + StringUtils.repeat(" ", (Pos + Length));
        }

        return Value.toString().substring(Pos, (Pos + Length));
    }

    public default String formatDateFirebird(Object Value) {

        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


            return fmt.format(toDate(Value));

        } catch (Exception e) {
            return toStr(Value);
        }
    }

    /**
     * Verificar se a Object 'Date' é uma Data válida.
     *
     * @param Value Data a ser validada
     * @return true se a data for válida
     */
    public default Boolean checkDate(Object Value) {

        if (Value != null) {

            Calendar cal = Calendar.getInstance();

            cal.setTime(toDate(Value));

            if (cal.get(Calendar.YEAR) >= 1900) {

                try {
                    SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");

                    f.setLenient(false); //Desabilitar tolerância datas inválidas, tratado como equivalente

                    f.parse(f.format(toDate(Value)));

                    return true;

                } catch (ParseException e) {
                    return false;
                }

            } else {
                return false;
            }

        } else {
            return false;
        }
    }

    /**
     * Data do Servidor
     *
     * @return Date Data no formato dd/MM/yyyy sem HH:mm:ss
     */
    public default Date dateServer() {

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    /**
     * Data e Hora do Servidor
     *
     * @return Data no formato dd/MM/yyyy HH:mm:ss
     */
    public default Date dateTimeServer() {

        Calendar calendar = Calendar.getInstance();

        return calendar.getTime();
    }

    /**
     * Hora do Servidor
     *
     * @param Seconds true para trazer os segundos HH:mm:ss
     * @return Hora do Servidor no formato int Ex: 09:08:42 => 90842 | 10:01:05
     * => 100105
     */
    public default int timeServer(Boolean Seconds) {

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());

        return toInt(StringUtils.leftPad(toStr(calendar.get(Calendar.HOUR_OF_DAY - 1)), 2, "0")
                + StringUtils.leftPad(toStr(calendar.get(Calendar.MINUTE)), 2, "0")
                + (Seconds ? StringUtils.leftPad(toStr(calendar.get(Calendar.SECOND)), 2, "0") : "00"));
    }

    /**
     * Evitar List NULL.
     *
     * @param Data fonte de dados que vai gerar o list Ex: generic.selectList
     * @return List<Map>
     */
    public default List<Map> fullList(List Data) {
        return Data != null ? Data : new ArrayList<>();
    }

    /**
     * Evitar Map NULL.
     *
     * @param Data fonte de dados que vai gerar o map Ex: generic.selectMap
     * @return Map
     */
    public default Map fullMap(Map Data) {
        return Data != null ? Data : new HashMap();
    }

    /**
     * Colocar todos os campos no Map.
     *
     * @param Pattern   Map padrão onde será colocado ou atualizado campos
     * @param newFields Map que contem campos atualizados ou novos
     */
    public default void putAll(Map Pattern, Map newFields) {

        if (newFields != null) {
            Pattern.putAll(newFields);
        }
    }

    /**
     * Colocar campos cheios não 'null' no Map.
     *
     * @param Pattern   Map padrão onde será colocado ou atualizado campos
     * @param newFileds Map que contem campos atualizados ou novos
     */
    public default void putFull(Map Pattern, Map newFileds) {

        if (newFileds != null) {

            Set<String> Keys = newFileds.keySet();

            for (String Key : Keys) {

                if (newFileds.get(Key) != null) {
                    Pattern.put(Key, newFileds.get(Key));
                }
            }
        }
    }

    public default Map jsonObjectToMap(Object jo) {

        try {
            return new ObjectMapper().readValue(jo.toString(), new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public default Object convertNodesFromXml(String xml) throws Exception {

        InputStream is = new ByteArrayInputStream(xml.getBytes());
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(is);
        return createMap(document.getDocumentElement());
    }

    public default Object createMap(Node node) {
        Map<String, Object> map = new HashMap<String, Object>();
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            String name = currentNode.getNodeName();
            Object value = null;
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                value = createMap(currentNode);
            } else if (currentNode.getNodeType() == Node.TEXT_NODE) {
                return currentNode.getTextContent();
            }
            if (map.containsKey(name)) {
                Object os = map.get(name);
                if (os instanceof List) {
                    ((List<Object>) os).add(value);
                } else {
                    List<Object> objs = new LinkedList<Object>();
                    objs.add(os);
                    objs.add(value);
                    map.put(name, objs);
                }
            } else {
                map.put(name, value);
            }
        }
        return map;
    }

    public default String removeAcentos(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    /**
     * Retorna um valor para ordenação da lista de tamanhos
     */
    public default Integer tamanhoParaCodigo(String tamanho) {
        switch (tamanho.trim()) {
            case "RN":
                return 100;
            case "3P":
                return 200;
            case "UN":
                return 300;
            case "PP":
                return 400;
            case "P":
                return 500;
            case "S":
                return 600;
            case "M":
                return 700;
            case "G":
                return 800;
            case "L":
                return 900;
            case "GG":
                return 1000;
            case "XL":
                return 1100;
            case "GGG":
                return 1200;
            case "GG1":
                return 1300;
            case "GG2":
                return 1400;
            case "GG3":
                return 1500;
            case "GG4":
                return 1600;
            case "GG5":
                return 1700;
            case "3G":
                return 1800;
            case "4G":
                return 1900;
            case "5G":
                return 2000;
            case "6G":
                return 2100;
            case "XG":
                return 2200;
            case "XGG":
                return 2300;
            case "EG":
                return 2400;
            case "XXL":
                return 2500;
            default:
                return toInt(soNumeros(tamanho.trim()));
        }
    }


    /**
     * Método para retornar somente numeros da String
     *
     * @param Value - String a ser verificada
     * @return Texto normalizado sem acentuação
     */
    public default String soNumeros(Object Value) {

        try {
            return Value.toString().replaceAll("[^0-9]*", "");
        } catch (Exception e) {
            return "";
        }
    }

    public default Integer UFtocUf(String UF) {

        String[] arrUF = {"AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO", "EX"};
        Integer[] arrCodigo = {27, 16, 13, 29, 23, 53, 32, 52, 21, 51, 50, 31, 15, 25, 41, 26, 22, 33, 24, 43, 11, 14, 42, 35, 28, 17, 99};

        return arrCodigo[Arrays.asList(arrUF).indexOf(UF) - 1];
    }

    public default String TPagToStr(String tPag) {
        String[] arrTPag = {"01", "02", "03", "04", "05", "10", "11", "12", "13", "90", "99"};
        String[] arrStr = {"Dinheiro", "Cheque", "Cartão de Crédito", "Cartão de Débito", "Crédito Loja", "Vale Alimentação", "Vale Refeição", "Vale Presente", "Vale Combustível", "Sem Pagamento", "Outros"};
        return arrStr[Arrays.asList(arrTPag).indexOf(tPag)];
    }


    public default String formataCartaoCredito(String cartao) {


        String primeiro = cartao.substring(0, 6);
        String meio = "******";
        String fin = cartao.substring(12, 16);

        return primeiro + meio + fin;

    }

    public default Calendar checaFDS(Calendar data) {
        // se for domingo
        if (data.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            data.add(Calendar.DATE, 1);
            //   System.out.println("Eh domingo, mudando data para +1 dias");
        }
        // se for sábado
        else if (data.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            data.add(Calendar.DATE, 2);
            //        System.out.println("Eh sabado, mudando data para +2 dias");
        } else {
            //          System.out.println("Eh dia de semana, mantem data");
        }

        return data;
    }

}
