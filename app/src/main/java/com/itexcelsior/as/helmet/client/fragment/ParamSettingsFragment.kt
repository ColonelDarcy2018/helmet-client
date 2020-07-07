package com.itexcelsior.`as`.helmet.client.fragment

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.itexcelsior.`as`.helmet.client.MainActivity
import com.itexcelsior.`as`.helmet.client.R
import com.itexcelsior.`as`.helmet.client.activity.BaseActivity
import com.itexcelsior.`as`.helmet.client.activity.HomeActivity
import com.itexcelsior.`as`.helmet.client.bean.MSocketConstants
import com.itexcelsior.`as`.helmet.client.bean.ParamInfo
import com.itexcelsior.`as`.helmet.client.util.ParamConst
import com.itexcelsior.mbluetooth.BluetoothSocketUtils
import com.itexcelsior.mbluetooth.SocketInfo
import com.itexcelsior.mbluetooth.callback.ConnectBlueCallBack
import com.itexcelsior.mbluetooth.connect.Connect
import com.itexcelsior.mbluetooth.connect.IConnect

import org.devio.`as`.proj.common.ui.component.HiBaseFragment
import java.util.HashMap

/**
 * @description:修改参数页面
 * @author: zxw
 * @date: 2020/6/24 16:27
 */
class ParamSettingsFragment : HiBaseFragment() {


    //1铁灰、2彩虹
    private var rb1: RadioButton? = null
    private var rb2: RadioButton? = null
    private var palette: RadioGroup? = null

    private val connect:IConnect=Connect()

    private var activity:BaseActivity?=null

    private fun showDialog() = activity!!.dialogLoadding.showDialog()
    private fun closeDialog() = activity!!.dialogLoadding.closeDialog()

    private val paramMap = HashMap<String, String>()

    override fun getLayoutId(): Int {
        return R.layout.fragment_settings
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity=getActivity() as BaseActivity


        /**查询当前设备参数信息**/
        val requestSocketInfo = SocketInfo(MSocketConstants.GET_XTHERM_PARAMETER, "第一次链接，获取数据", null)
        connectManage(requestSocketInfo)


        val btnSave = findViewById<Button>(R.id.btnSave)
        btnSave.setOnClickListener(onClickListener)

        palette = findViewById(R.id.paletteGroup)
        rb1 = findViewById<RadioButton>(R.id.grey)//铁灰
        rb2 = findViewById<RadioButton>(R.id.rainbow)//彩虹

    }

    /**
     * 保存按钮
     */
    private val onClickListener = View.OnClickListener {
        val fix = findViewById<EditText>(R.id.fix)
        val refltmp = findViewById<EditText>(R.id.refltmp)
        val airtmpView = findViewById<EditText>(R.id.airtmp)
        val humi = findViewById<EditText>(R.id.humi)
        val emiss = findViewById<EditText>(R.id.emiss)
        val distance = findViewById<EditText>(R.id.distance)

        paramMap[ParamConst.FIX] = fix.text.toString() + ""
        paramMap[ParamConst.REFLTMP] = refltmp.text.toString() + ""
        paramMap[ParamConst.AIRTMP] = airtmpView.text.toString() + ""
        paramMap[ParamConst.HUMI] = humi.text.toString() + ""
        paramMap[ParamConst.EMISS] = emiss.text.toString() + ""
        paramMap[ParamConst.DISTANCE] = distance.text.toString() + ""

        val checkedRadioButtonId = palette?.getCheckedRadioButtonId()
        if (checkedRadioButtonId == rb1?.getId()) {
            paramMap["paletteInt"] = "0"
        } else if (checkedRadioButtonId == rb2?.getId()) {
            paramMap["paletteInt"] = "1"
        }

        saveParamsettings()
    }

    /**
     * 修改方法
     */
    fun saveParamsettings() {

        //获取页面信息
        //0:fix  1:refltmp  2:airtmp 3:humi  4:emiss 5:distance
        val fix = java.lang.Float.parseFloat(paramMap[ParamConst.FIX]!!)
        val refltmp = java.lang.Float.parseFloat(paramMap[ParamConst.REFLTMP]!!)
        val airtmp = java.lang.Float.parseFloat(paramMap[ParamConst.AIRTMP]!!)
        val humi = java.lang.Float.parseFloat(paramMap[ParamConst.HUMI]!!)
        val emiss = java.lang.Float.parseFloat(paramMap[ParamConst.EMISS]!!)
        val distance = java.lang.Short.parseShort(paramMap[ParamConst.DISTANCE])
        val palette = Integer.parseInt(paramMap[ParamConst.PALETTE]!!)
        val paramInfo = ParamInfo(fix, refltmp, airtmp, humi, emiss, distance, palette)
        val jsonString = JSON.toJSONString(paramInfo)

        val requestSocketInfo = SocketInfo(MSocketConstants.UPDATE_XTHERM_PARAMETER, "保存参数设置", jsonString)
        connectManage(requestSocketInfo)

    }


    /**
     * 蓝牙连接方法整合
     *
     * @param bluetoothDevice
     * @param context
     * @param requestSocketInfo
     */
    fun connectManage(requestSocketInfo: SocketInfo) {

        var connectBlueCallBack=object :ConnectBlueCallBack {
            override fun onStartConnect() {
                showDialog()
            }
            override fun onConnectSuccess(bluetoothDevice: BluetoothDevice, socket: BluetoothSocket) {
                val repStr = BluetoothSocketUtils.exchangeWithServer(socket, requestSocketInfo)
                val socketInfo = JSONObject.parseObject(repStr, SocketInfo::class.java)
                when (socketInfo.code) {
                    MSocketConstants.GET_XTHERM_PARAMETER -> {
                        //展示信息
                        val paramInfo = JSONObject.parseObject(socketInfo.data, ParamInfo::class.java)
                        val declaredFields = paramInfo.javaClass.declaredFields
                        for (i in declaredFields.indices) {
                            val declaredField = declaredFields[i]
                            val fieldName = declaredField.name
                            val fieldValue = getFieldValueByName(fieldName, paramInfo)
                            paramMap.put(fieldName, fieldValue!!.toString() + "")
                        }
                        showViewInfo()
                    }

                    MSocketConstants.UPDATE_XTHERM_PARAMETER -> {
                        activity?.showToast("保存成功！")

                    }
                }
                closeDialog()
            }

            override fun onConnectFail(bluetoothDevice: BluetoothDevice, message: String) {
                closeDialog()
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("msg", "蓝牙连接失败，请选择正确的设备，并保持开启！")
                startActivity(intent)
            }
        }

        /**成功后自动进行连接 */
        connect.connect(activity?.bluetoothDevice,connectBlueCallBack)
    }


    /**
     * 将paramMap中的信息展示到页面上
     */
    private fun showViewInfo() {
        //0:fix  1:refltmp  2:airtmp 3:humi  4:emiss 5:distance 6:paletteInt
        for (entry in paramMap) {
            when (entry.key) {
                ParamConst.FIX -> (findViewById<View>(R.id.fix) as EditText).setText(paramMap.get(ParamConst.FIX))
                ParamConst.REFLTMP -> (findViewById<View>(R.id.refltmp) as EditText).setText(paramMap.get(ParamConst.REFLTMP))
                ParamConst.AIRTMP -> (findViewById<View>(R.id.airtmp) as EditText).setText(paramMap.get(ParamConst.AIRTMP))
                ParamConst.HUMI -> (findViewById<View>(R.id.humi) as EditText).setText(paramMap.get(ParamConst.HUMI))
                ParamConst.EMISS -> (findViewById<View>(R.id.emiss) as EditText).setText(paramMap.get(ParamConst.EMISS))
                ParamConst.DISTANCE -> (findViewById<View>(R.id.distance) as EditText).setText(paramMap[ParamConst.DISTANCE])
                ParamConst.PALETTE -> {
                    val paletteInt = Integer.parseInt(paramMap.get(ParamConst.PALETTE)!!)
                    val btnIndex = if (paletteInt == 0) rb1?.id else rb2?.id
                    (findViewById<View>(R.id.paletteGroup) as RadioGroup).check(btnIndex!!)
                }
            }
        }
    }

    private fun getFieldValueByName(fieldName: String, o: Any): Any? {
        return try {
            val firstLetter = fieldName.substring(0, 1).toUpperCase()
            val getter = "get" + firstLetter + fieldName.substring(1)
            val method = o.javaClass.getMethod(getter, *arrayOf())
            method.invoke(o)
        } catch (e: Exception) {
            println("属性不存在")
            null
        }
    }
}