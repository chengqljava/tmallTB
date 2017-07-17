package com.cheng.qian.model;

import java.io.Serializable;

/**
 * 图片颜色尺码
 * @author chengql
 * @version $Id: ImageColorSize.java, v 0.1 2017年6月30日 下午5:30:24 chengql Exp $
 */
public class ImageColorSize implements Serializable {
    /**  */
    private static final long serialVersionUID = 1L;
    private String            url;
    private String            color;
    private String            size;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

}
