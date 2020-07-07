package com.itexcelsior.`as`.helmet.client.activity.logic

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager
import com.itexcelsior.`as`.helmet.client.R
import com.itexcelsior.`as`.helmet.client.fragment.BackCorrectFragment
import com.itexcelsior.`as`.helmet.client.fragment.ChooseFunctionFragment
import com.itexcelsior.`as`.helmet.client.fragment.ParamSettingsFragment
import org.devio.`as`.proj.common.tab.HiFragmentTabView
import org.devio.`as`.proj.common.tab.HiTabViewAdapter
import org.devio.hi.ui.tab.bottom.HiTabBottomInfo
import org.devio.hi.ui.tab.bottom.HiTabBottomLayout

/**
 * @description: 将MainActivity的一些逻辑内聚在这，让MainActivity更加清爽
 * @author: zxw
 * @date: 2020/6/24 10:34
 */
class HomeActivityLogic(activityProvider: ActivityProvider, savedInstanceState: Bundle?) {

    private var fragmentTabView: HiFragmentTabView? = null

    private var hiTabBottomLayout: HiTabBottomLayout? = null

    private val infoList = mutableListOf<HiTabBottomInfo<*>>()

    /**
     * 与activity绑定
     */
    private var activityProvider: ActivityProvider? = null

    companion object {
        private const val SAVED_CURRENT_ID = "SAVED_CURRENT_ID"
    }


    private var currentItemIndex: Int = 0


    init {
        this.activityProvider = activityProvider

        if (savedInstanceState != null)
            currentItemIndex = savedInstanceState.getInt(SAVED_CURRENT_ID)


        initTabBottom()
    }

    private fun initTabBottom() {
        hiTabBottomLayout = this.activityProvider?.findViewById(R.id.tab_bottom_layout)

        hiTabBottomLayout?.setTabAlpha(0.85f)


        val defaultColor = activityProvider?.getResources()?.getColor(R.color.tabBottomDefaultColor)

        val tintColor = activityProvider?.getResources()?.getColor(R.color.tabBottomTintColor)

        val hiTabChooseFunction = HiTabBottomInfo(
                "选择功能",
                "fonts/iconfont.ttf",
                activityProvider?.getString(R.string.if_home),
                null,
                defaultColor,
                tintColor
        )
        hiTabChooseFunction.fragment = ChooseFunctionFragment::class.java

        val hiTabBackCorrect = HiTabBottomInfo(
                "黑体校正",
                "fonts/iconfont.ttf",
                activityProvider?.getString(R.string.if_address),
                null,
                defaultColor,
                tintColor
        )
        hiTabBackCorrect.fragment = BackCorrectFragment::class.java

        val hiTabParamSettings = HiTabBottomInfo(
                "参数设置",
                "fonts/iconfont.ttf",
                activityProvider?.getString(R.string.if_category),
                null,
                defaultColor,
                tintColor
        )
        hiTabParamSettings.fragment = ParamSettingsFragment::class.java

        infoList.add(hiTabChooseFunction)
        infoList.add(hiTabBackCorrect)
        infoList.add(hiTabParamSettings)
        //填充数据到视图
        hiTabBottomLayout?.inflateInfo(infoList)

        initFragmentTabView()
        //底部导航栏切换监听
        hiTabBottomLayout?.addTabSelectedChangeListener{ index, _, _ ->
            fragmentTabView?.currentItem = index
            this@HomeActivityLogic.currentItemIndex = index
        }
        //默认选中tab
        hiTabBottomLayout?.defaultSelected(hiTabChooseFunction)

    }

    /**
     * 初始化底部导航对应的视图
     */
    private fun initFragmentTabView() {
        val tabViewAdapter = HiTabViewAdapter(activityProvider?.getSupportFragmentManager(), infoList)
        fragmentTabView = activityProvider?.findViewById(R.id.fragment_tab_view)
        fragmentTabView?.adapter = tabViewAdapter
    }


    interface ActivityProvider {
        fun <T : View> findViewById(@IdRes id: Int): T

        fun getResources(): Resources

        fun getSupportFragmentManager(): FragmentManager

        fun getString(@StringRes resId: Int): String
    }
}