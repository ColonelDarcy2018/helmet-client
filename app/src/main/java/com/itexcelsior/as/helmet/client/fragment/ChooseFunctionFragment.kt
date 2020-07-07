package com.itexcelsior.`as`.helmet.client.fragment

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.alibaba.fastjson.JSONObject
import com.itexcelsior.`as`.helmet.client.MainActivity
import com.itexcelsior.`as`.helmet.client.R
import com.itexcelsior.`as`.helmet.client.activity.BaseActivity
import com.itexcelsior.`as`.helmet.client.bean.MSocketConstants
import com.itexcelsior.mbluetooth.BluetoothSocketUtils
import com.itexcelsior.mbluetooth.SocketInfo
import com.itexcelsior.mbluetooth.callback.ConnectBlueCallBack
import com.itexcelsior.mbluetooth.connect.Connect
import com.itexcelsior.mbluetooth.connect.IConnect
import org.devio.`as`.proj.common.ui.component.HiBaseFragment
import java.util.*

/**
 * @description:
 * @author: zxw
 * @date: 2020/6/28 09:32
 */
class ChooseFunctionFragment :HiBaseFragment(){

    private val connect: IConnect = Connect()

    private var activity: BaseActivity?=null

    private val textViews = ArrayList<TextView>()

    private fun showDialog() = activity!!.dialogLoadding.showDialog()
    private fun closeDialog() = activity!!.dialogLoadding.closeDialog()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity= getActivity() as BaseActivity

        if (activity?.bluetoothDevice == null) {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("msg", "蓝牙连接失败，请选择正确的设备，并保持开启！")
            startActivity(intent)
        }


        val sigTempBtn = findViewById<TextView>(R.id.sigTempView)
        val crowdTempBtn = findViewById<TextView>(R.id.crowdTempView)
        val arfaceBtn = findViewById<TextView>(R.id.arcfaceView)
        val carnumBtn = findViewById<TextView>(R.id.carnumView)
//        TextView idcardBtn = findViewById(R.id.idcardView);

        sigTempBtn.setOnClickListener(mOnclickListener)
        crowdTempBtn.setOnClickListener(mOnclickListener)
        arfaceBtn.setOnClickListener(mOnclickListener)
        carnumBtn.setOnClickListener(mOnclickListener)
//        idcardBtn.setOnClickListener(mOnclickListener);

        textViews.add(sigTempBtn)
        textViews.add(crowdTempBtn)
        textViews.add(arfaceBtn)
        textViews.add(carnumBtn)
//        textViews.add(idcardBtn);

    }

    /**
     * 初始化按钮颜色方法
     */
    private fun changeColor(list: List<TextView>) {
        for (i in list.indices) {
            val textView = list[i]
            textView.setBackgroundColor(resources.getColor(R.color.app_color_theme_3))
        }
    }

    private val mOnclickListener = View.OnClickListener { v ->
        changeColor(textViews)

        val textView = v as TextView
        when (v.getId()) {
            R.id.sigTempView -> {
                textView.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                connectManage(
                    SocketInfo(MSocketConstants.SINGLE_TEMPERATURE_MODE, "切换单人测温模式", null)
                )
            }
            R.id.crowdTempView -> {
                textView.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                connectManage(
                    SocketInfo(MSocketConstants.CROWD_TEMPERATURE_MODE, "切换多人测温模式", null)
                )
            }
            R.id.arcfaceView -> {
                textView.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                connectManage(
                    SocketInfo(MSocketConstants.ARFACE_MODE, "切换人脸识别模式", null)
                )
            }
            //                case R.id.idcardView:
            //                    textView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            //                    connectManage(bluetoothDevice, context, new SocketInfo(SocketConstants.ID_CARD_MODE, "切换身份证识别模式", null));
            //                    break;
            R.id.carnumView -> {
                textView.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                connectManage(
                    SocketInfo(MSocketConstants.CAR_NUMBER_MODE, "切换车牌识别模式", null)
                )
            }
        }
    }



    /**
     * 蓝牙连接方法整合
     *
     * @param bluetoothDevice
     * @param context
     * @param requestSocketInfo
     */
    fun connectManage(
        requestSocketInfo: SocketInfo
    ) {

        /**成功后自动进行连接 */
        connect.connect(activity?.bluetoothDevice, object : ConnectBlueCallBack {
            override fun onStartConnect() {
                showDialog()
            }

            override fun onConnectSuccess(
                bluetoothDevice: BluetoothDevice,
                socket: BluetoothSocket
            ) {
                //获取数据，将数据显示到页面上
                val repStr = BluetoothSocketUtils.exchangeWithServer(socket, requestSocketInfo)

                //转换为对象
                val socketInfo = JSONObject.parseObject(repStr, SocketInfo::class.java)

                when (socketInfo.code) {
                    MSocketConstants.GET_XTHERM_PARAMETER -> { }
                    MSocketConstants.UPDATE_XTHERM_PARAMETER -> activity?.showToast("操作成功!")
                }
                closeDialog()
            }

            override fun onConnectFail(bluetoothDevice: BluetoothDevice, message: String) {
                closeDialog()
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("msg", "蓝牙连接失败，请选择正确的设备，并保持开启！")
                startActivity(intent)
            }
        })
    }


    /**
     * 绑定页面layout
     */
    override fun getLayoutId(): Int {
        return R.layout.fragment_choose_function
    }

}