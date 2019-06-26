package core.exception;

import core.RequestType;

public class HttpStatusException extends Exception {
    private static final String NEW_LINE = "\n";

    public HttpStatusException(int responseCode, String url, RequestType requestType) {
        super("Response Code : " + responseCode
                + NEW_LINE
                + "API Url : " + url
                + NEW_LINE
                + "İstek Tipi : " + requestType
        );
    }
}
