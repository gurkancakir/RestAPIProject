package core;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.exception.APINotFoundException;
import core.interfaces.IRestAPI;
import core.interfaces.RestAPI;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public abstract class AbstractRestAPI<T, V> implements IRestAPI<V> {
    protected RestAPI api;
    protected String baseUrl;
    protected T input;
    protected Gson gson;

    public abstract void beforeExecute(T input);
    public abstract void afterExecute(V output);
    public abstract void catchException(Exception e);

    public AbstractRestAPI(String baseUrl) {
        this.baseUrl = baseUrl;
        gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();
    }

    public V execute() {
        try
        {
            Class<?> clazz = this.getClass();
            api = clazz.getAnnotation(RestAPI.class);
            if (api == null) {
                throw new APINotFoundException();
            }

            HttpUriRequest request = createHttpClient();
            CloseableHttpClient client = HttpClients.createDefault();

            request.setHeader("Accept",       "application/json");
            request.setHeader("Content-type", "application/json");

            beforeExecute(input);
            HttpResponse response = client.execute(request);

            int responseCode = response.getStatusLine().getStatusCode();
            System.out.println("\nSending '"+ api.requestType().name() +"' request to URL : " +  baseUrl + api.endpoint());
            System.out.println("Response Code : " + responseCode);

            if (responseCode != HttpStatus.SC_OK) {
                throw new APINotFoundException();
            }

            V obj = gson.fromJson(new InputStreamReader(response.getEntity().getContent()), getOutputClass());

            afterExecute(obj);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            catchException(e);
        }
        return null;
    }

    private HttpUriRequest createHttpClient() throws UnsupportedEncodingException {
        String url = baseUrl + api.endpoint();
        switch (api.requestType()) {
            case GET:    {
                HttpGet httpGet = new HttpGet(url);
                return httpGet;
            }
            case POST:   {
                HttpPost httpPost = new HttpPost(url);
                if (input != null) {
                    String json = gson.toJson(input);
                    StringEntity entity = new StringEntity(json);
                    httpPost.setEntity(entity);
                }
                return httpPost;
            }
            case PUT:    {
                HttpPut httpPut = new HttpPut(url);
                if (input != null) {
                    String json = gson.toJson(input);
                    StringEntity entity = new StringEntity(json);
                    httpPut.setEntity(entity);
                }
                return httpPut;
            }
            case DELETE: {
                HttpDelete httpDelete = new HttpDelete(url);
                return httpDelete;
            }
            default: return null;
        }
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
