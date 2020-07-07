package com.itexcelsior.as.helmet.client.activity;

import android.os.Bundle;

import com.itexcelsior.as.helmet.client.MainActivity;
import com.itexcelsior.as.helmet.client.R;
import com.itexcelsior.as.helmet.client.activity.logic.HomeActivityLogic;


/**
 * home界面，带有底部导航栏
 */
public class HomeActivity extends BaseActivity implements HomeActivityLogic.ActivityProvider {



    private HomeActivityLogic logic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //绑定逻辑层
        logic = new HomeActivityLogic(this, savedInstanceState);

        //获取蓝牙设备
        bluetoothDevice = getIntent().getParcelableExtra(MainActivity.BLUETOOTH_DEVICE);
    }


}
