package com.example.learn.responses;

import com.example.learn.models.Maquina;
import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MaquinaResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("error")
    private String error;

    @SerializedName("data")
    private List<Maquina> data;

    public String getMessage() { return message; }
    public String getError() { return error; }
    public List<Maquina> getData() { return data; }

    public static class MaquinaResponseDeserializer implements JsonDeserializer<MaquinaResponse> {
        @Override
        public MaquinaResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            MaquinaResponse response = new MaquinaResponse();
            JsonObject jsonObject = json.getAsJsonObject();

            if (jsonObject.has("message")) {
                response.message = jsonObject.get("message").getAsString();
            }
            if (jsonObject.has("error")) {
                response.error = jsonObject.get("error").getAsString();
            }
            if (jsonObject.has("data")) {
                JsonElement dataElement = jsonObject.get("data");

                if (dataElement.isJsonArray()) {
                    response.data = new Gson().fromJson(dataElement, new TypeToken<List<Maquina>>() {}.getType());
                } else if (dataElement.isJsonObject()) {
                    response.data = new ArrayList<>();
                    response.data.add(new Gson().fromJson(dataElement, Maquina.class));
                }
            }
            return response;
        }
    }
}
