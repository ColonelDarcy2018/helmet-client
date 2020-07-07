package com.itexcelsior.`as`.helmet.client

import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import com.itexcelsior.`as`.helmet.client.activity.BaseActivity
import com.itexcelsior.`as`.helmet.client.activity.HomeActivity
import com.itexcelsior.mbluetooth.callback.PinBlueCallBack
import com.itexcelsior.mbluetooth.pin.Pin

class MainActivity : BaseActivity(), MainActivityLogic.ActivityProvider {

    private var logic: MainActivityLogic? = null

    var listView: ListView? = null

    private val pin: Pin = Pin()


    companion object{
        const val BLUETOOTH_DEVICE: String = "bluetoothDevice"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById<View>(R.id.device_listview) as ListView

        listView?.onItemClickListener = onItemClickListener//点击配对,配对成功后自动连接

        logic = MainActivityLogic(this, savedInstanceState)

        //开启蓝牙扫描
        pin.openBluetooth(logic!!.scanBlueCallBack, context)
    }


    /**
     * 蓝牙列表点击事件
     *
     * @param menu
     * @return
     */
    private val onItemClickListener =
        AdapterView.OnItemClickListener { _, _, position, _ ->
            //显示loading层
            dialogLoadding.showDialog()

            val macId = logic!!.macArrayList[position]

            bluetoothDevice = logic!!.deviceMap[macId]

            val intent = Intent(context, HomeActivity::class.java)

            intent.putExtra(BLUETOOTH_DEVICE, bluetoothDevice)

            if (bluetoothDevice.bondState === BluetoothDevice.BOND_BONDED) {
                startActivity(intent)
                dialogLoadding.closeDialog()
            } else {
                //注册广播监听配对结果
                val pinBlueCallBack = object : PinBlueCallBack {
                    override fun onBondRequest() {}
                    override fun onBondFail(device: BluetoothDevice) {
                        dialogLoadding.closeDialog()
                    }

                    override fun onBonding(device: BluetoothDevice) {}
                    override fun onBondSuccess(device: BluetoothDevice) {
                        startActivity(intent)
                        dialogLoadding.closeDialog()
                    }
                }
                pin.pin(bluetoothDevice, pinBlueCallBack, context)//蓝牙配对
            }
        }


}
