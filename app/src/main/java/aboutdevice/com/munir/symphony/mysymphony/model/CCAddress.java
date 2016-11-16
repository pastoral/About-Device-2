package aboutdevice.com.munir.symphony.mysymphony.model;

import java.util.Date;

/**
 * Created by munirul.hoque on 11/16/2016.
 */

public class CCAddress {
    private String name;
    private String district;
    private String address;
    private boolean cc ;
    private float lat;
    private float lan;
    private String country;
    private Date created;
    private Date last_modified;


    public CCAddress(){}

    public CCAddress(String name, String created_by, Date last_modified, String country, Date created, float lan, float lat, boolean cc, String address, String district) {
        this.name = name;
        this.created_by = created_by;
        this.last_modified = last_modified;
        this.country = country;
        this.created = created;
        this.lan = lan;
        this.lat = lat;
        this.cc = cc;
        this.address = address;
        this.district = district;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public Date getLast_modified() {
        return last_modified;
    }

    public void setLast_modified(Date last_modified) {
        this.last_modified = last_modified;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public float getLan() {
        return lan;
    }

    public void setLan(float lan) {
        this.lan = lan;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public boolean isCc() {
        return cc;
    }

    public void setCc(boolean cc) {
        this.cc = cc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    private String created_by;

}
