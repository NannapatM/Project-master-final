package co.project.client.ui.rssi;

public class Element {

    private String title;
    private String security;
    private String level;
    private String bssid;

    public Element(String title, String security, String level, String bssid){
        this.title = title;
        this.security = security;
        this.level = level;
        this.bssid = bssid;
    }

    public String getTitle() {
        return title;
    }

    public String getSecurity() {
        return security;
    }

    public String getLevel() {
        return level;
    }

    public String getBssid(){return bssid;}
}
