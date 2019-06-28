package core.exception;

public class APINotFoundException extends Exception {

    public APINotFoundException() {
        super("Kullanılan API sınıfı @RestAPI olarak işaretlenmelidir !");
    }
}
