package com.example.learn.responses;

import com.example.learn.models.Operacao;
import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class OperacaoResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("error")
    private String error;

    @SerializedName("data")
    private List<Operacao> data;

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    public List<Operacao> getData() {
        return data;
    }

    public void setData(List<Operacao> data) {
        this.data = data;
    }

    // Adicionamos um Deserializador personalizado para tratar um único objeto ou uma lista
    public static class OperacaoResponseDeserializer implements JsonDeserializer<OperacaoResponse> {
        @Override
        public OperacaoResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            OperacaoResponse response = new OperacaoResponse();
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
                    response.data = new Gson().fromJson(dataElement, new TypeToken<List<Operacao>>() {}.getType());
                } else if (dataElement.isJsonObject()) {
                    response.data = new ArrayList<>();
                    response.data.add(new Gson().fromJson(dataElement, Operacao.class));
                }
            }
            return response;
        }
    }
}
