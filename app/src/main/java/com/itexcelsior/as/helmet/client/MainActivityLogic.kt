package com.itexcelsior.`as`.helmet.client

import android.bluetooth.BluetoothDevice
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager
import com.itexcelsior.`as`.helmet.client.adapter.listview.ListViewAdapterWithViewHolder
import com.itexcelsior.`as`.helmet.client.bean.DeviceListViewBean
import com.itexcelsior.`as`.helmet.client.util.ListViewConstants
import com.itexcelsior.mbluetooth.callback.ScanBlueCallBack
import java.util.ArrayList
import java.util.HashMap

/**
 * @description:
 * @author: zxw
 * @date: 2020/6/29 13:38
 */
class MainActivityLogic(activityProvider: ActivityProvider, savedInstanceState: Bundle?) {

    private val activityProvider: ActivityProvider = activityProvider

    private val mainActivity: MainActivity = activityProvider as MainActivity

    var deviceMap: MutableMap<String, BluetoothDevice> = HashMap()//蓝牙设备列表

    var macArrayList = ArrayList<String>()//存储蓝牙设备地址

    private val mDatas: MutableList<DeviceListViewBean> = ArrayList()//用于显示蓝牙信息

    init {
        initData()
    }

    /**
     * 自定义适配器，用于显示检测到的蓝牙设备列表
     */
    private var listViewAdapterWithViewHolder: ListViewAdapterWithViewHolder? = null


    val scanBlueCallBack = object : ScanBlueCallBack {
        override fun onScanStarted() {
            Log.i(mainActivity.TAG, "开始扫描")
        }

        override fun onScanFinished() {}

        override fun onScanning(device: BluetoothDevice) {

            Log.i(mainActivity.TAG, "检测到设备：${device.name}")

            //将搜索到的蓝牙名称和地址添加到列表。
            var deviceStatus = "未知状态" + device.bondState//蓝牙绑定状态
            when (device.bondState) {
                BluetoothDevice.BOND_BONDED -> deviceStatus =
                    ListViewConstants.DEVICE_ITEM_LISTVIEW_STATUS_CONNECTED
                BluetoothDevice.BOND_BONDING -> deviceStatus =
                    ListViewConstants.DEVICE_ITEM_LISTVIEW_STATUS_CONNECTING
                BluetoothDevice.BOND_NONE -> deviceStatus =
                    ListViewConstants.DEVICE_ITEM_LISTVIEW_STATUS_UNCONNECTED
            }

            val deviceName = if (device.name == null) "未知设备" else device.name

            val bean = DeviceListViewBean(deviceName, deviceStatus)

            mDatas.add(bean)

            macArrayList.add(device.address)//将搜索到的蓝牙地址添加到列表。
            deviceMap[device.address] = device

            listViewAdapterWithViewHolder?.notifyDataSetChanged()//更新
        }
    }

    //方法；初始化Data
    private fun initData() {

        //为数据绑定适配器
        listViewAdapterWithViewHolder =
            ListViewAdapterWithViewHolder(
                mainActivity,
                mDatas
            )

        mainActivity.listView?.adapter = listViewAdapterWithViewHolder

    }


    interface ActivityProvider {
        fun <T : View> findViewById(@IdRes id: Int): T

        fun getResources(): Resources

        fun getSupportFragmentManager(): FragmentManager

        fun getString(@StringRes resId: Int): String
    }
}