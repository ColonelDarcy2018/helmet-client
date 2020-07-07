package com.itexcelsior.as.helmet.client.activity;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.itexcelsior.helmet.dialog.loading.DialogLoadding;

import org.devio.as.proj.common.ui.component.HiBaseActivity;

/**
 * @description:
 * @author: zxw
 * @date: 2020/6/29 13:51
 */
public class BaseActivity extends HiBaseActivity {

    /**
     * 遮罩层
     */
    protected DialogLoadding dialogLoadding;

    /**
     * 蓝牙设备
     */
    public BluetoothDevice bluetoothDevice = null;

    public DialogLoadding getDialogLoadding() {
        return dialogLoadding;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化loading层
        dialogLoadding = new DialogLoadding(context);

    }

}
