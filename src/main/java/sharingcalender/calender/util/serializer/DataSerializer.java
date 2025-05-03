package sharingcalender.calender.util.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataSerializer {

    private static final ObjectMapper objectMapper = init();


    private static ObjectMapper init() {
        return new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> T deserialize(String data, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(data, typeReference);

        } catch (JsonProcessingException e) {
            log.error("JSON Parsing Exception When deserializing data={}, typeReference ={}", data,
                typeReference.getType());
            return null;
        }
    }

    public static String serialize(Object object) {

        try {
            return objectMapper.writeValueAsString(object);

        } catch (JsonProcessingException e) {
            log.error("Serializing Exception When object={}", object);
            return null;
        }
    }


}
