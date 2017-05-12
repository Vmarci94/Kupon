package hu.bme.vmarci94.homeworok.kupon.data;

import android.widget.ImageView;

import com.orm.SugarRecord;

/**
 * Created by vmarci94 on 2017.05.05..
 */

public class Kupon extends SugarRecord<Kupon> {
    private String company;
    private String sale;
    private String description;
    private double latitude;
    private double longitude;

    private ImageView imgKupon;

    public Kupon(){}

    public Kupon(String key, String email, String sale, String description) {
        this.sale = sale;
        this.description = description;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getSale() {
        return this.sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ImageView getImgKupon() {
        return imgKupon;
    }

    public void setImgKupon(ImageView imgKupon) {
        this.imgKupon = imgKupon;
    }
}
