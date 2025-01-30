package sharingcalender.calender.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;

public class JwtUtil {

    public static Map<String, String> decodePayload(String token) throws IOException {
        String[] tokenInfo = token.split("\\.");
        if (tokenInfo.length < 3) {
            throw new IllegalArgumentException();
        }
        byte[] payload = Base64.getUrlDecoder().decode(tokenInfo[1]);

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(payload, Map.class);
    }

}
