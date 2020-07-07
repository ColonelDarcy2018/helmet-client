package com.itexcelsior.as.helmet.client.bean;


/**
 * 蓝牙参数DTO
 */
public class ParamInfo {

    /**
     * correction
     */
    private float fix;
    /**
     * reflection
     */
    private float refltmp;
    /**
     * amb temp
     */
    private float airtmp;
    /**
     * humidity
     */
    private float humi;
    /**
     * emissivity
     */
    private float emiss;
    private short distance;

    /**
     * 色板模式 0铁灰 1彩虹
     */
    private int paletteInt;


    public ParamInfo(float fix, float refltmp, float airtmp, float humi, float emiss, short distance) {
        this.fix = fix;
        this.refltmp = refltmp;
        this.airtmp = airtmp;
        this.humi = humi;
        this.emiss = emiss;
        this.distance = distance;
    }

    public ParamInfo(float fix, float refltmp, float airtmp, float humi, float emiss, short distance, int paletteInt) {
        this.fix = fix;
        this.refltmp = refltmp;
        this.airtmp = airtmp;
        this.humi = humi;
        this.emiss = emiss;
        this.distance = distance;
        this.paletteInt = paletteInt;
    }

    public ParamInfo() {
    }

    /**
     * correction
     */
    public float getFix() {
        return fix;
    }

    public void setFix(float fix) {
        this.fix = fix;
    }

    public float getRefltmp() {
        return refltmp;
    }

    public void setRefltmp(float refltmp) {
        this.refltmp = refltmp;
    }

    public float getAirtmp() {
        return airtmp;
    }

    public void setAirtmp(float airtmp) {
        this.airtmp = airtmp;
    }

    public float getHumi() {
        return humi;
    }

    public void setHumi(float humi) {
        this.humi = humi;
    }

    public float getEmiss() {
        return emiss;
    }

    public void setEmiss(float emiss) {
        this.emiss = emiss;
    }

    public short getDistance() {
        return distance;
    }

    public void setDistance(short distance) {
        this.distance = distance;
    }

    public int getPaletteInt() {
        return paletteInt;
    }

    public void setPaletteInt(int paletteInt) {
        this.paletteInt = paletteInt;
    }
}
