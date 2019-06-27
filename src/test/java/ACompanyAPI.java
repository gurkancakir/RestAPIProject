import core.AbstractRestAPI;

import java.util.HashMap;
import java.util.Map;

public abstract class ACompanyAPI<T, V> extends AbstractRestAPI<T, V> {

    public ACompanyAPI() {
        super("http://localhost:8080");
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept",       "application/json");
        headers.put("Content-type", "application/json");
        return headers;
    }
}
