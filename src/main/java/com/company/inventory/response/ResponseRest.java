package com.company.inventory.response;
//1
//vamos a crear toda la estructura de metadata de nuestra respuesta (de nuestro servicio)


import java.util.ArrayList;
import java.util.HashMap;

public class ResponseRest {

    private ArrayList<HashMap<String, String>> metadata = new ArrayList<>();

    //vamos a construir los metodos con set metadata y get metadata
    //no usaremos lombock porque lo vamos a customizar


    public ArrayList<HashMap<String, String>> getMetadata() {
        return metadata;
    }

    public void setMetadata( String type, String code, String date) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("type", type);
        map.put("code", code);
        map.put("date", date);

        metadata.add(map);
    }
}
