package aboutdevice.com.munir.symphony.mysymphony.model;

import java.util.Date;

/**
 * Created by admin on 1/31/2017.
 */

public class NotificationStore {
    private int id;
    private String notification_title;
    private String notification_content;
    private String activityToBeOpened;
    private String model_sw_version;
    private String t;
    private String b;
    private String link;
    private String image_url;
    private String insertion_date;

    public NotificationStore(){}


    public NotificationStore( int id, String notification_content, String notification_title, String activityToBeOpened, String model_sw_version, String t, String b, String link, String image_url, String insertion_date) {
        this.id = id;
        this.notification_content = notification_content;
        this.notification_title = notification_title;
        this.activityToBeOpened = activityToBeOpened;
        this.model_sw_version = model_sw_version;
        this.t = t;
        this.b = b;
        this.link = link;
        this.image_url = image_url;
        this.insertion_date = insertion_date;
    }

    public NotificationStore( String notification_content, String notification_title, String activityToBeOpened, String model_sw_version, String t, String b, String link, String image_url, String insertion_date) {

        this.notification_title = notification_title;
        this.notification_content = notification_content;
        this.activityToBeOpened = activityToBeOpened;
        this.model_sw_version = model_sw_version;
        this.t = t;
        this.b = b;
        this.link = link;
        this.image_url = image_url;
        this.insertion_date = insertion_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotification_content() {
        return notification_content;
    }

    public void setNotification_content(String notification_content) {
        this.notification_content = notification_content;
    }

    public String getNotification_title() {
        return notification_title;
    }

    public void setNotification_title(String notification_title) {
        this.notification_title = notification_title;
    }

    public String getActivityToBeOpened() {
        return activityToBeOpened;
    }


    public void setActivityToBeOpened(String activityToBeOpened) {
        this.activityToBeOpened = activityToBeOpened;
    }

    public String getModel_sw_version() {
        return model_sw_version;
    }


    public void setModel_sw_version(String model_sw_version) {
        this.model_sw_version = model_sw_version;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getInsertion_date() {
        return insertion_date;
    }

    public void setInsertion_date(String insertion_date) {
        this.insertion_date = insertion_date;
    }
}
