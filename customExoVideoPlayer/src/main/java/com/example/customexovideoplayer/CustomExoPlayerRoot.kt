package com.example.customexovideoplayer

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.OptIn
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import com.example.customexovideoplayer.globalEnums.EnumAspectRatio
import com.example.customexovideoplayer.globalEnums.EnumMute
import com.example.customexovideoplayer.globalEnums.EnumPlaybackSpeed
import com.example.customexovideoplayer.globalEnums.EnumPlayerSize
import com.example.customexovideoplayer.globalEnums.EnumRepeatMode
import com.example.customexovideoplayer.globalEnums.EnumResizeMode
import com.example.customexovideoplayer.globalEnums.EnumScreenMode
import com.example.customexovideoplayer.utils.DoubleClick

abstract class CustomExoPlayerRoot @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attributeSet, defStyleAttr) {

    private var inflatedView: View = inflate(context, R.layout.layout_player_base_kotlin, this)

    var playerView: PlayerView
    var retryView: LinearLayout
    var retryViewTitle: TextView
    var retryButton: Button
    var backwardView: ImageView
    var forwardView: ImageView
    var mute: AppCompatImageButton
    var unMute: AppCompatImageButton
    var settingContainer: FrameLayout
    var fullScreenContainer: FrameLayout
    var enterFullScreen: AppCompatImageButton
    var exitFullScreen: AppCompatImageButton
    var exoPlay: AppCompatImageButton
    var exoPause: AppCompatImageButton

    abstract var customClickListener: DoubleClick

    var currAspectRatio: EnumAspectRatio = EnumAspectRatio.ASPECT_16_9
    var currRepeatMode: EnumRepeatMode = EnumRepeatMode.REPEAT_OFF
    var currPlayerSize: EnumPlayerSize = EnumPlayerSize.EXACTLY
    var currResizeMode: EnumResizeMode = EnumResizeMode.FILL
    var currMute: EnumMute = EnumMute.UNMUTE
    var currPlaybackSpeed: EnumPlaybackSpeed = EnumPlaybackSpeed.NORMAL
    var currScreenMode: EnumScreenMode = EnumScreenMode.MINIMISE

    init {
        // Initialize views
        playerView = inflatedView.findViewById(R.id.playerView)
        retryView = inflatedView.findViewById(R.id.retry_view)
        backwardView = inflatedView.findViewById(R.id.exo_backward)
        forwardView = inflatedView.findViewById(R.id.exo_forward)
        exoPlay = inflatedView.findViewById(R.id.exo_play)
        exoPause = inflatedView.findViewById(R.id.exo_pause)
        retryViewTitle = retryView.findViewById(R.id.textView_retry_title)
        retryButton = retryView.findViewById(R.id.button_try_again)
        mute = playerView.findViewById(R.id.exo_mute)
        unMute = playerView.findViewById(R.id.exo_unmute)
        settingContainer = playerView.findViewById(R.id.container_setting)
        fullScreenContainer = playerView.findViewById(R.id.container_fullscreen)
        enterFullScreen = playerView.findViewById(R.id.exo_enter_fullscreen)
        exitFullScreen = playerView.findViewById(R.id.exo_exit_fullscreen)

        // Initialize listeners
        initListeners()
    }

    private fun initListeners() {
        retryButton.setOnClickListener { customClickListener.onClick(retryButton) }
        backwardView.setOnClickListener { customClickListener.onClick(backwardView) }
        forwardView.setOnClickListener { customClickListener.onClick(forwardView) }
        mute.setOnClickListener { customClickListener.onClick(mute) }
        unMute.setOnClickListener { customClickListener.onClick(unMute) }
        fullScreenContainer.setOnClickListener { customClickListener.onClick(fullScreenContainer) }
        enterFullScreen.setOnClickListener { customClickListener.onClick(enterFullScreen) }
        exitFullScreen.setOnClickListener { customClickListener.onClick(exitFullScreen) }
        exoPlay.setOnClickListener { customClickListener.onClick(exoPlay) }
        exoPause.setOnClickListener { customClickListener.onClick(exoPause) }
    }

    protected fun showRetryView(retryTitle: String? = null) {
        retryView.visibility = VISIBLE
        retryTitle?.let { retryViewTitle.text = it }
    }

    protected fun hideRetryView() {
        retryView.visibility = GONE
    }

    protected fun showLoading() {
        hideRetryView()
    }

    protected fun hideLoading() {
        hideRetryView()
    }

    protected fun setShowController(showController: Boolean = true) {
        if (showController) showController() else hideController()
    }

    @OptIn(UnstableApi::class)
    protected fun showController() {
        playerView.showController()
    }

    @OptIn(UnstableApi::class)
    protected fun hideController() {
        playerView.hideController()
    }

    protected fun showUnMuteButton() {
        mute.visibility = GONE
        unMute.visibility = VISIBLE
    }

    protected fun showMuteButton() {
        mute.visibility = VISIBLE
        unMute.visibility = GONE
    }

    protected fun setShowSettingButton(showSetting: Boolean = false) {
        settingContainer.visibility = if (showSetting) VISIBLE else GONE
    }

    protected fun setShowFullScreenButton(showFullscreenButton: Boolean = false) {
        fullScreenContainer.visibility = if (showFullscreenButton) VISIBLE else GONE
    }

    protected fun setShowScreenModeButton(screenMode: EnumScreenMode = EnumScreenMode.MINIMISE) {
        when (screenMode) {
            EnumScreenMode.FULLSCREEN -> {
                enterFullScreen.visibility = GONE
                exitFullScreen.visibility = VISIBLE
            }
            EnumScreenMode.MINIMISE -> {
                enterFullScreen.visibility = VISIBLE
                exitFullScreen.visibility = GONE
            }
            else -> {
                enterFullScreen.visibility = VISIBLE
                exitFullScreen.visibility = GONE
            }
        }
    }

    protected fun showSystemUI() {
        playerView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LOW_PROFILE
                        or View.SYSTEM_UI_FLAG_IMMERSIVE
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                )
    }

    protected fun hideSystemUI() {
        playerView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LOW_PROFILE
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                )
    }
}
