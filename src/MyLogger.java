package pain.t;

import java.util.Date;
import java.util.logging.Logger;

public class MyLogger {
    public Logger logger;   
    
    /**
     * MyLogger constructor
     * no params
     */
    public MyLogger() {
        //System.out.println(LogtoString(logger.log()));
    }

    /**
    * Print out log context, message, and time
    * @return string 
    */
    public String LogtoString(Log log) {
        return log.getContext() + ": " + log.getMessage() + " at " + log.getTimestamp();
    }
}