package pain.t;

import java.util.Date;

public class Log {
    private  Date timestamp;
    private  String context;
    private  String message;
    
    public Log(String context, String msg) {
        this.timestamp = new Date();
        this.context = context;
        this.message = msg;
    }
        
    public  Date getTimestamp() {
        return timestamp;
    }

    public  String getContext() {
        return context;
    }

    public  String getMessage() {
        return message;
    }    
} 