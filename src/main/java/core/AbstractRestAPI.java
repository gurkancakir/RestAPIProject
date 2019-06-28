package core;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.LoggingFilter;
import core.exception.APINotFoundException;
import core.exception.HttpStatusException;
import core.interfaces.IRestAPI;
import core.interfaces.RestAPI;

public abstract class AbstractRestAPI<T, V> implements IRestAPI<V> {
    protected static final String CONTENT_TYPE_JSON = "application/json";

    protected RestAPI api;
    protected String  baseUrl;
    protected T       input;

    public abstract void beforeExecute(T input);
    public abstract void afterExecute(V output);
    public abstract void catchException(Exception e);
    public abstract Map<String, String> getHeaders();
    public abstract Map<String, String> getQueryParams();

    public AbstractRestAPI(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public V execute() {
        try
        {
            Class<?> clazz = this.getClass();
            api = clazz.getAnnotation(RestAPI.class);
            if (api == null) {
                throw new APINotFoundException();
            }


            Client client = Client.create();
            WebResource webResource = client.resource(baseUrl + api.endpoint());
            beforeExecute(input);

            // add query params
            Map<String, String> queryParams = getQueryParams();
            if (queryParams != null) {
                for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                    webResource.queryParam(entry.getKey(), entry.getValue());
                }
            }

            WebResource.Builder builder = webResource.type(CONTENT_TYPE_JSON);

            if (input != null) {
                builder.entity(input);
            }

            // add headers
            Map<String, String> headers = getHeaders();
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    builder.header(entry.getKey(), entry.getValue());
                }
            }

            client.addFilter(new LoggingFilter(System.out));
            System.out.println("\nSending '"+ api.requestType().name() +"' request to URL : " +  baseUrl + api.endpoint());
            ClientResponse response = builder.method(api.requestType().name(), ClientResponse.class);

            int responseCode = response.getStatus();
            System.out.println("Response Code : " + responseCode);

            if (responseCode != 200) {
                throw new HttpStatusException(responseCode, baseUrl + api.endpoint(), api.requestType());
            }

            V obj = response.getEntity(getOutputClass());

            afterExecute(obj);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            catchException(e);
        }
        return null;
    }

    private Class<T> getInputClass() throws ClassNotFoundException {
        String className = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0].getTypeName();
        return (Class<T>) Class.forName(className);
    }

    private Class<V> getOutputClass() throws ClassNotFoundException {
        String className = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1].getTypeName();
        return (Class<V>) Class.forName(className);
    }
}
