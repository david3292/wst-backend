package com.altioracorp.wst.xml.factura;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.altioracorp.wst.util.JsonUtilities;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Converter {

//    public static List<Destinatario> getDestinatariosFromJson(final JSONObject jsonObject) throws JsonProcessingException {
//        if (jsonObject == null) {
//            return null;
//        }
//        if (!jsonObject.has("destinatario")) {
//            return null;
//        }
//        final List<Destinatario> lista = new ArrayList<>();
//        if (JsonUtilities.isJsonArray(jsonObject, "destinatario")) {
//            final JSONArray destinatarioJson = JsonUtilities.getArrayFromJson(jsonObject, "destinatario");
//            for (final Object o : destinatarioJson) {
//                if (o instanceof JSONObject) {
//                    final Destinatario destinatario = new Destinatario((JSONObject) o);
//                    lista.add(destinatario);
//                }
//            }
//        } else {
//            final JSONObject detalleJson = JsonUtilities.getObjectFromJson(jsonObject, "destinatario");
//            final Destinatario destinatario = new Destinatario(detalleJson);
//            lista.add(destinatario);
//        }
//        return lista;
//    }

    public static List<CampoAdicional> getInfoAdicionalFromJson(final JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        if (!jsonObject.has("campoAdicional")) {
            return null;
        }
        final List<CampoAdicional> lista = new ArrayList<>();
        if (JsonUtilities.isJsonArray(jsonObject, "campoAdicional")) {
            final int[] cont = {1};
            final JSONArray infoJson = JsonUtilities.getArrayFromJson(jsonObject, "campoAdicional");
            infoJson.toList().forEach(o -> {
                try {
                    final HashMap<String, String> map = (HashMap) o;
                    final CampoAdicional info = new CampoAdicional();
                    if (map.containsValue("Email")) {
                        info.setNombre("EMAIL");
                        info.setValue(map.get("content"));
                    } else {
//                        info.setNombre("INFO".concat(StringUtilities.completarCerosIzquierda(String.valueOf(cont[0]), 2)));
                        info.setNombre(map.get("nombre") instanceof String ? map.get("nombre") : String.valueOf(map.get("nombre")));
                        info.setValue(map.get("content") instanceof String ? map.get("content") : String.valueOf(map.get("content")));
                        cont[0]++;
                    }
                    lista.add(info);
                } catch (final Exception ex) {
                    ex.printStackTrace();
                }
            });
        } else {
            final JSONObject infoJson = JsonUtilities.getObjectFromJson(jsonObject, "campoAdicional");
            final CampoAdicional info = new CampoAdicional();
            if (JsonUtilities.getStringFromJson(infoJson, "nombre").equalsIgnoreCase("EMAIL")) {
                info.setNombre("EMAIL");
            } else {
                info.setNombre(JsonUtilities.getStringFromJson(infoJson, "nombre"));
            }
            info.setValue(JsonUtilities.getStringFromJson(infoJson, "content"));
            lista.add(info);
        }
        return lista;
    }

    public static List<Detalle> getDetallesFromJson(final JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        if (!jsonObject.has("detalle")) {
            return null;
        }
        final List<Detalle> lista = new ArrayList<>();
        if (JsonUtilities.isJsonArray(jsonObject, "detalle")) {
            final JSONArray detalleJson = JsonUtilities.getArrayFromJson(jsonObject, "detalle");
            for (final Object o : detalleJson) {
                if (o instanceof JSONObject) {
                    final Detalle detalle = new Detalle((JSONObject) o);
                    lista.add(detalle);
                }
            }
        } else {
            final JSONObject detalleJson = JsonUtilities.getObjectFromJson(jsonObject, "detalle");
            final Detalle detalle = new Detalle(detalleJson);
            lista.add(detalle);
        }
        return lista;
    }

    public static List<DetAdicional> getInfoAdicionalDetalleFromJson(final JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        final List<DetAdicional> lista = new ArrayList<>();
        jsonObject.keySet().forEach(k -> {
            final JSONObject infoAJson = JsonUtilities.getObjectFromJson(jsonObject, k);
            final DetAdicional det = new DetAdicional();
            det.setNombre(JsonUtilities.getStringFromJson(infoAJson, "nombre"));
            det.setValor(JsonUtilities.getStringFromJson(infoAJson, "valor"));
            lista.add(det);
        });
        return lista;
    }

//    public static List<ImpuestoRetencion> getImpuestosRetencionFromJson(final JSONObject jsonObject) throws JsonProcessingException {
//        if (jsonObject == null) {
//            return null;
//        }
//        final List<ImpuestoRetencion> lista = new ArrayList<>();
//        if (!jsonObject.has("impuesto")) {
//            return null;
//        }
//        final ObjectMapper objectMapper = new ObjectMapper();
//        if (JsonUtilities.isJsonArray(jsonObject, "impuesto")) {
//            final JSONArray impJson = JsonUtilities.getArrayFromJson(jsonObject, "impuesto");
//            for (final Object o : impJson) {
//                if (o instanceof JSONObject) {
//                    final ImpuestoRetencion impuestoRetencion = objectMapper.readValue(o.toString(), ImpuestoRetencion.class);
//                    lista.add(impuestoRetencion);
//                }
//            }
//        } else {
//            final ImpuestoRetencion impuestoRetencion = objectMapper.readValue(jsonObject.get("impuesto").toString(), ImpuestoRetencion.class);
//            lista.add(impuestoRetencion);
//        }
//        return lista;
//    }

//    public static List<Motivo> getMotivosFromJson(final JSONObject jsonObject) throws JsonProcessingException {
//        if (jsonObject == null) {
//            return null;
//        }
//        final List<Motivo> lista = new ArrayList<>();
//        if (!jsonObject.has("motivo")) {
//            return null;
//        }
//        final ObjectMapper objectMapper = new ObjectMapper();
//        if (JsonUtilities.isJsonArray(jsonObject, "motivo")) {
//            final JSONArray impJson = JsonUtilities.getArrayFromJson(jsonObject, "motivo");
//            for (final Object o : impJson) {
//                if (o instanceof JSONObject) {
//                    final Motivo motivo = objectMapper.readValue(o.toString(), Motivo.class);
//                    lista.add(motivo);
//                }
//            }
//        } else {
//            final Motivo motivo = objectMapper.readValue(jsonObject.get("motivo").toString(), Motivo.class);
//            lista.add(motivo);
//        }
//        return lista;
//    }

    public static List<Impuesto> getImpuestosFromJson(final JSONObject jsonObject, final String clave) throws JsonProcessingException {
        if (jsonObject == null) {
            return null;
        }
        final List<Impuesto> lista = new ArrayList<>();
        if (!jsonObject.has(clave)) {
            return null;
        }
        final ObjectMapper objectMapper = new ObjectMapper();
        if (JsonUtilities.isJsonArray(jsonObject, clave)) {
            final JSONArray impJson = JsonUtilities.getArrayFromJson(jsonObject, clave);
            for (final Object o : impJson) {
                if (o instanceof JSONObject) {
                    final Impuesto impuestoComprobante = objectMapper.readValue(o.toString(), Impuesto.class);
                    lista.add(impuestoComprobante);
                }
            }
        } else {
            final Impuesto impuestoComprobante = objectMapper.readValue(jsonObject.get(clave).toString(), Impuesto.class);
            lista.add(impuestoComprobante);
        }
        return lista;
    }

    public static List<Impuesto> getDetalleImpuestosFromJson(final JSONObject jsonObject) throws JsonProcessingException {
        if (jsonObject == null) {
            return null;
        }
        final List<Impuesto> lista = new ArrayList<>();
        if (!jsonObject.has("impuesto")) {
            return null;
        }
        final ObjectMapper objectMapper = new ObjectMapper();
        if (JsonUtilities.isJsonArray(jsonObject, "impuesto")) {
            final JSONArray impJson = JsonUtilities.getArrayFromJson(jsonObject, "impuesto");
            for (final Object o : impJson) {
                if (o instanceof JSONObject) {
                    final Impuesto impuestoComprobante = objectMapper.readValue(o.toString(), Impuesto.class);
                    lista.add(impuestoComprobante);
                }
            }
        } else {
            final Impuesto impuestoComprobante = objectMapper.readValue(jsonObject.get("impuesto").toString(), Impuesto.class);
            lista.add(impuestoComprobante);
        }
        return lista;
    }

    public static List<Pago> getPagosFromJson(final JSONObject jsonObject) throws JsonProcessingException {
        if (jsonObject == null) {
            return null;
        }
        if (!jsonObject.has("pago")) {
            return null;
        }
        final List<Pago> lista = new ArrayList<>();
        final ObjectMapper objectMapper = new ObjectMapper();
        if (JsonUtilities.isJsonArray(jsonObject, "pago")) {
            final JSONArray pagosJson = JsonUtilities.getArrayFromJson(jsonObject, "pago");
            for (final Object o : pagosJson) {
                if (o instanceof JSONObject) {
                    final Pago pago = objectMapper.readValue(o.toString(), Pago.class);
                    lista.add(pago);
                }
            }
        } else {
            final Pago pago = objectMapper.readValue(jsonObject.get("pago").toString(), Pago.class);
            lista.add(pago);
        }
        return lista;
    }

}
