package com.itexcelsior.`as`.helmet.client.fragment

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.alibaba.fastjson.JSONObject
import com.itexcelsior.`as`.helmet.client.MainActivity
import com.itexcelsior.`as`.helmet.client.R
import com.itexcelsior.`as`.helmet.client.activity.BaseActivity
import com.itexcelsior.`as`.helmet.client.bean.MSocketConstants
import com.itexcelsior.mbluetooth.BluetoothSocketUtils.exchangeWithServer
import com.itexcelsior.mbluetooth.SocketConstants
import com.itexcelsior.mbluetooth.SocketInfo
import com.itexcelsior.mbluetooth.callback.ConnectBlueCallBack
import com.itexcelsior.mbluetooth.connect.Connect
import com.itexcelsior.mbluetooth.connect.IConnect
import org.devio.`as`.proj.common.ui.component.HiBaseFragment

/**
 * @description:
 * @author: zxw
 * @date: 2020/6/28 09:46
 */
class BackCorrectFragment : HiBaseFragment() {

    private val connect: IConnect = Connect()

    private var activity: BaseActivity?=null

    private fun showDialog() = activity!!.dialogLoadding.showDialog()
    private fun closeDialog() = activity!!.dialogLoadding.closeDialog()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity= getActivity() as BaseActivity

        findViewById<Button>(R.id.backCorrectConfirmBtn).setOnClickListener { connect.connect(activity?.bluetoothDevice,connectBlueCallBack) }

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_back_correct
    }

    private val connectBlueCallBack=object : ConnectBlueCallBack {
        override fun onStartConnect() {
            showDialog()
        }

        override fun onConnectSuccess(
            bluetoothDevice: BluetoothDevice,
            socket: BluetoothSocket
        ) {
            //获取数据，将数据显示到页面上
            val repStr = exchangeWithServer(socket, SocketInfo(MSocketConstants.BACKGROUND_CORRECT_MODE, "黑体校正", null))

            //转换为对象
            val socketInfo = JSONObject.parseObject<SocketInfo>(repStr, SocketInfo::class.java)

            when (socketInfo.code) {
                MSocketConstants.UPDATE_XTHERM_PARAMETER -> activity?.showToast("操作成功")
            }

            closeDialog()
        }

        override fun onConnectFail(bluetoothDevice: BluetoothDevice, message: String) {
            //连接失败
            closeDialog()
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("msg", "蓝牙连接失败，请选择正确的设备，并保持开启！")
            startActivity(intent)
        }
    }

}