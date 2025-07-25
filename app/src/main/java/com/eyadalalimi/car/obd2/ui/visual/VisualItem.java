package com.eyadalalimi.car.obd2.ui.visual;

public class VisualItem {
    public String name;
    public int imageRes;         // صور محلية
    public String imageUrl;      // صور من الإنترنت

    // للصور المحلية
    public VisualItem(String name, int imageRes) {
        this.name = name;
        this.imageRes = imageRes;
        this.imageUrl = null;
    }

    // للصور من الإنترنت
    public VisualItem(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.imageRes = 0;
    }
}
