package com.cheng.qian.enums;

/**
 * 尺寸下载地址文件夹名称
 * @author chengql
 * @version $Id: SizeImage.java, v 0.1 2017年6月25日 下午1:53:47 chengql Exp $
 */
public enum SizeImage {
                       S480_480("_480x480q90.jpg",
                                "480_480"), S640_640("_640x640q90.jpg",
                                                     "640_640"), S800_800("_800x800q90.jpg",
                                                                          "800_800"), S200_200("_200x200q90.jpg",
                                                                                               "200_200"), S_DETAIl("",
                                                                                                                    "detail"), S_MATERIAL("",
                                                                                                                                          "material");

    private String size;
    private String address;

    private SizeImage(String size, String address) {
        this.size = size;
        this.address = address;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
