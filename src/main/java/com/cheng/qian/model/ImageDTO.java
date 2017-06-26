package com.cheng.qian.model;

import java.io.Serializable;

public class ImageDTO implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;
    private String            name;
    private String            url;
    private String            saveAddress;
    private String            size;

    public String getName() {
        if (name == null || "".equals(name)) {
            name = this.url;
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSaveAddress() {
        return saveAddress;
    }

    public void setSaveAddress(String saveAddress) {
        this.saveAddress = saveAddress;
    }

}
