package com.example.customexovideoplayer.model

import androidx.databinding.BaseObservable
import com.example.customexovideoplayer.globalEnums.EnumAspectRatio
import com.example.customexovideoplayer.globalEnums.EnumRepeatMode
import com.example.customexovideoplayer.globalEnums.EnumPlayerSize
import com.example.customexovideoplayer.globalEnums.EnumResizeMode

class PlayerControllerModel : BaseObservable() {

    var mute: Boolean = false
    var enumAspectRatio: EnumAspectRatio = EnumAspectRatio.ASPECT_16_9
    var enumRepeatMode: EnumRepeatMode = EnumRepeatMode.REPEAT_OFF
    var enumPlayerSize: EnumPlayerSize = EnumPlayerSize.EXACTLY
    var enumResizeMode: EnumResizeMode = EnumResizeMode.FILL

}