package com.example.learn.responses;

import com.example.learn.models.Classificacao;
import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ClassificacaoResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("error")
    private String error;

    @SerializedName("data")
    private List<Classificacao> data;

    public String getMessage() { return message; }
    public String getError() { return error; }
    public List<Classificacao> getData() { return data; }

    public static class ClassificacaoResponseDeserializer implements JsonDeserializer<ClassificacaoResponse> {
        @Override
        public ClassificacaoResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            ClassificacaoResponse response = new ClassificacaoResponse();
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
                    response.data = new Gson().fromJson(dataElement, new TypeToken<List<Classificacao>>() {}.getType());
                } else if (dataElement.isJsonObject()) {
                    response.data = new ArrayList<>();
                    response.data.add(new Gson().fromJson(dataElement, Classificacao.class));
                }
            }
            return response;
        }
    }
}
