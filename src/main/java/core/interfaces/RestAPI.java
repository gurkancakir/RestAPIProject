package core.interfaces;

import core.RequestType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RestAPI {
    String endpoint();
    RequestType requestType() default RequestType.GET;
}
