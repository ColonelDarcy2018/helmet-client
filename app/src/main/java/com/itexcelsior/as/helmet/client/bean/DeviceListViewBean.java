package com.itexcelsior.as.helmet.client.bean;


/**
 * @author zxw
 */
public class DeviceListViewBean {
    private String title;
    private String phone;//状态显示

    public DeviceListViewBean() {
    }

    public DeviceListViewBean(String title, String phone) {
        this.title = title;

        this.phone = phone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
