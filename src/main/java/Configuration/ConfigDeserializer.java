package Configuration;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

class ConfigDeserializer implements JsonDeserializer<Map<String, Object>> {
    @Override
    public Map<String, Object> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Map<String, Object> map = new HashMap<>();
        JsonObject jsonObject = json.getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            JsonElement value = entry.getValue();
            if (value.isJsonPrimitive()) {
                JsonPrimitive primitive = value.getAsJsonPrimitive();
                if (primitive.isNumber()) {
                    if (primitive.getAsNumber().doubleValue() % 1 == 0) {
                        map.put(entry.getKey(), primitive.getAsInt());
                    } else {
                        map.put(entry.getKey(), primitive.getAsDouble());
                    }
                } else if (primitive.isBoolean()) {
                    map.put(entry.getKey(), primitive.getAsBoolean());
                } else if (primitive.isString()) {
                    map.put(entry.getKey(), primitive.getAsString());
                }
            } else if (value.isJsonObject()) {
                map.put(entry.getKey(), context.deserialize(value, Map.class));
            }
        }
        return map;
    }
}
