package com.itexcelsior.as.helmet.client.bean;

import com.itexcelsior.mbluetooth.SocketConstants;

/**
 * @description:
 * @author: zxw
 * @date: 2020/6/29 15:53
 */
public class MSocketConstants extends SocketConstants {

    /**
     * 获取服务端红外摄像参数信息
     */
    public static final int GET_XTHERM_PARAMETER=1001;


    /**
     * 更新服务端红外摄像参数信息
     */
    public static final int UPDATE_XTHERM_PARAMETER=2001;


    /**
     * 功能选择按钮
     */
    public static final int SINGLE_TEMPERATURE_MODE=3001;
    public static final int CROWD_TEMPERATURE_MODE=3002;
    public static final int ARFACE_MODE=3003;
    public static final int ID_CARD_MODE=3004;
    public static final int CAR_NUMBER_MODE=3005;

    /**
     * 背景校正
     */
    public static final int BACKGROUND_CORRECT_MODE=3006;

    /**
     * 批量注册人脸识别照片
     */
    public static final int BATCH_REGISTER_ARFACE=4001;

}
