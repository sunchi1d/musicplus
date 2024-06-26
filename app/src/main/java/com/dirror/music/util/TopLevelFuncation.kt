package com.dirror.music.util

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.dirror.music.BuildConfig
import com.dirror.music.App
import com.dirror.music.music.standard.data.StandardSongData.StandardArtistData
import java.lang.ref.WeakReference

/**
 * Kotlin 顶层函数
 */

/**
 * 设置状态栏图标颜色
 * @param dark true 为黑色，false 为白色
 */
fun setStatusBarIconColor(activity: Activity, dark: Boolean) {
    StatusbarColorUtils.setStatusBarDarkIcon(activity, dark)
}

var sToastRef: WeakReference<Toast>? = null

/**
 * 全局 toast
 */
fun toast(msg: String) {
    runOnMainThread {
        sToastRef?.get()?.cancel()
        val toast = Toast.makeText(App.context, msg, Toast.LENGTH_SHORT)
        toast.show()
        sToastRef = WeakReference(toast)
    }
}

/**
 * 运行在主线程，更新 UI
 */
fun runOnMainThread(runnable: Runnable) {
    Handler(Looper.getMainLooper()).post(runnable)
}

/**
 * 全局 log
 */
@JvmOverloads
fun loge(msg: String, tag: String = "Default") {
    if (Secure.isDebug()) {
        runOnMainThread {
            Log.e(tag, "【$msg】")
        }
    }
}

/**
 * dp 转 px
 */
fun dp2px(dp: Float): Float = dp * App.context.resources.displayMetrics.density

/**
 * 获取系统当前时间
 */
fun getCurrentTime() : Long {
    return System.currentTimeMillis()
}

/**
 * 标准歌手数组转文本
 * @param artistList 歌手数组
 * @return 文本
 */
fun parseArtist(artistList: ArrayList<StandardArtistData>): String {
    var artist = ""
    for (artistName in 0..artistList.lastIndex) {
        if (artistName != 0) {
            artist += " / "
        }
        artist += artistList[artistName].name
    }
    return artist
}

/**
 * 通过浏览器打开网页
 * @param context
 * @url 网址
 */
fun openUrlByBrowser(context: Context, url: String) {
    if (url != "") {
        try {
            val intent = Intent()
            intent.action = "android.intent.action.VIEW"
            val contentUrl = Uri.parse(url)
            intent.data = contentUrl
            startActivity(context, intent, Bundle())
        } catch (e: Exception) {
            toast("启动外部浏览器失败，请点击更新详情链接手动更新~")
        }
    }
}

/**
 * 毫秒转日期
 */
fun msTimeToFormatDate(msTime: Long): String {
    return TimeUtil.msTimeToFormatDate(msTime)
}

/**
 * 获取状态栏高度
 * @return px 值
 */
fun getStatusBarHeight(window: Window, context: Context): Int {
    return StatusBarUtil.getStatusBarHeight(window, context)
}

/**
 * 获取底部导航栏高度
 */
fun getNavigationBarHeight(activity: Activity): Int {
    return ScreenUtil.getNavigationBarHeight(activity)
}

/**
 * 获取版本号
 */
fun getVisionCode(): Int {
    return BuildConfig.VERSION_CODE
}

/**
 * 获取版本名
 */
fun getVisionName(): String {
    return BuildConfig.VERSION_NAME
}


fun defaultTypeface(context: Context): Typeface {
    return Typeface.createFromAsset(context.assets, "fonts/Moriafly-Regular.ttf")
}


var lastClickTime = 0L
fun singleClick(during: Long = 200L, callBack: () -> Unit) {
    if (getCurrentTime() - lastClickTime > during) {
        callBack()
    }
    lastClickTime = getCurrentTime()
}