package com.dirror.music.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.dirror.music.App
import com.dirror.music.manager.User
import com.dirror.music.music.netease.data.DailyRecommendSongData
import com.dirror.music.music.netease.data.RecognizeMusicData
import com.dirror.music.util.MagicHttp
import com.dirror.music.util.toast
import com.google.gson.Gson

class RecognizemusicViewmodel:ViewModel() {
    fun getRecognizemusic(success: (RecognizeMusicData) -> Unit, failure: (String) -> Unit){
        if(User.hasCookie){
            val url ="${User.neteaseCloudMusicApi}/audio/match?duration=3"+"audioFP="
            MagicHttp.OkHttpManager().getByCache(App.context, url, 600,  {
                // loge(url)
                try {
                    val data = Gson().fromJson(it, RecognizeMusicData::class.java)
                    when (data.code) {
                        200 -> success.invoke(data)
                        301 -> failure.invoke("错误代码：${data.code}，尝试重新登录")
                        else -> failure.invoke("错误代码：${data.code}")
                    }
                } catch (e: Exception) {
                    failure.invoke("获取失败")
                }
            }, {
                toast("网络失败")
            })

        }else{
            toast("UID 离线状态无法获取，请使用手机号登录")
        }
    }



}