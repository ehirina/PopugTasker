package po.pug.tasker;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Base64;

public class TokenUtils {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    // eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWNjZXNzIjp0cnVlLCJpZCI6MSwiaWF0IjoxNjUxODY2NzIzfQ.nr7nxUXorKJBPswaSq0o6VXRwiQrFXLrQQL2z1DLGfU

    public static String getBearerTokenHeader() {
        var attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            var request = attributes.getRequest();
            return request.getHeader(AUTHORIZATION_HEADER).substring(7);
        } else { return null; }
    }

    public static String getRole(String token) {
        return "manager";
    }

    public static Long getAccountId(String token) {
        var payload = getPayload(token);
        var mapper = new ObjectMapper();
        try {
            var parsedPayload = mapper.readValue(payload, JsonPayload.class);
            return parsedPayload.id;
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private static String getPayload(String token) {
        String[] chunks = token.split("\\.");
        var decoder = Base64.getUrlDecoder();
        return new String(decoder.decode(chunks[1]));
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class JsonPayload {
        Long id;
    }
}
