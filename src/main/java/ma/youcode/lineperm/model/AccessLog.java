package ma.youcode.lineperm.model;

public class AccessLog {

    private String date;
    private String time;
    private String user;
    private String action;
    private String filename;
    private String result;

    public AccessLog(String date, String time, String user, String action, String filename, String result) {
        this.date = date;
        this.time = time;
        this.user = user;
        this.action = action;
        this.filename = filename;
        this.result = result;
    }
    
    public String getDate() {
        return date;
    }
    public String getTime() {
        return time;
    }
    public String getUser() {
        return user;
    }
    public String getAction() {
        return action;
    }
    public String getFilename() {
        return filename;
    }
    public String getResult() {
        return result;
    }
    
}
