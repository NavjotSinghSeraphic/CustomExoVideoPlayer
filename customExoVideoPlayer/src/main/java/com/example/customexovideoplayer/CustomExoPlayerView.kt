package com.example.customexovideoplayer

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.common.Tracks
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import com.example.customexovideoplayer.globalEnums.EnumAspectRatio
import com.example.customexovideoplayer.globalEnums.EnumMute
import com.example.customexovideoplayer.globalEnums.EnumRepeatMode
import com.example.customexovideoplayer.globalEnums.EnumResizeMode
import com.example.customexovideoplayer.globalEnums.EnumScreenMode
import com.example.customexovideoplayer.globalInterfaces.CustomExoPlayerListener
import com.example.customexovideoplayer.utils.DoubleClick
import com.example.customexovideoplayer.utils.DoubleClickListener
import com.example.customexovideoplayer.utils.PublicFunctions
import com.example.customexovideoplayer.utils.PublicValues




class CustomExoPlayerView(
    context: Context,
    attributeSet: AttributeSet
) : CustomExoPlayerRoot(context, attributeSet), Player.Listener {

    private lateinit var currSource: String

    private var player: ExoPlayer = ExoPlayer.Builder(context).build()
    private var customPlayerPlayerListener: CustomExoPlayerListener? = null
    private var currPlayWhenReady: Boolean = true
    private var playbackPosition: Long = 0
    private var currentWindow: Int = 0
    private var currVolume: Float = 0f

    override var customClickListener: DoubleClick
        get() = DoubleClick(object : DoubleClickListener {
            override fun onSingleClickEvent(view: View) {
                Log.e("CustomExoPlayerView", "onSingleClickEvent:clicked " )
                when (view.id) {
                    retryButton.id -> {
                        hideRetryView()
                        restartPlayer()
                    }
                    mute.id -> {
                        unMutePlayer()
                    }
                    unMute.id -> {
                        mutePlayer()
                    }
                    enterFullScreen.id -> {
                        setScreenMode(EnumScreenMode.FULLSCREEN)
                    }
                    exitFullScreen.id -> {
                        setScreenMode(EnumScreenMode.MINIMISE)
                    }
                    exoPlay.id->{
                        startPlayer()
                    }
                    exoPause.id->{
                        pausePlayer()
                    }
                    backwardView.id -> {
                        seekBackward()
                    }
                    forwardView.id -> {
                        seekForward()
                    }
                }
            }

            override fun onDoubleClickEvent(view: View) {
                Log.e("CustomExoPlayerView", "onDoubleClickEvent:clicked " )

            }
        })
        set(value) {}

    init {
        inflate(context, R.layout.video_player_exo_controllers_kotlin, this)
        playerView = findViewById(R.id.playerView)
        exoPlay = findViewById(R.id.exo_play)
        exoPause = findViewById(R.id.exo_pause)
        // Add other button initializations here...

        player.addListener(this)
        extractAttrs(attributeSet)
    }

    private fun extractAttrs(attributeSet: AttributeSet?) {

        attributeSet.let {

            val typedArray: TypedArray =
                context.obtainStyledAttributes(it, R.styleable.CustomExoPlayerView)

            if (typedArray.hasValue(R.styleable.CustomExoPlayerView_custom_player_aspect_ratio)) {
                val aspectRatio = typedArray.getInteger(
                    R.styleable.CustomExoPlayerView_custom_player_aspect_ratio,
                    EnumAspectRatio.ASPECT_16_9.value
                )
                setAspectRatio(EnumAspectRatio[aspectRatio])
            }

            if (typedArray.hasValue(R.styleable.CustomExoPlayerView_custom_player_resize_mode)) {
                val resizeMode: Int = typedArray.getInteger(
                    R.styleable.CustomExoPlayerView_custom_player_resize_mode,
                    EnumResizeMode.FILL.value
                )
                setResizeMode(EnumResizeMode[resizeMode])
            }

            if (typedArray.hasValue(R.styleable.CustomExoPlayerView_custom_player_play_when_ready)) {
                setPlayWhenReady(
                    typedArray.getBoolean(
                        R.styleable.CustomExoPlayerView_custom_player_play_when_ready,
                        true
                    )
                )
            }

            if (typedArray.hasValue(R.styleable.CustomExoPlayerView_custom_player_mute)) {
                val muteValue = typedArray.getInteger(
                    R.styleable.CustomExoPlayerView_custom_player_mute,
                    EnumMute.UNMUTE.value
                )
                setMute(EnumMute[muteValue])
            }

            if (typedArray.hasValue(R.styleable.CustomExoPlayerView_custom_player_show_controller)) {
                setShowControllers(
                    typedArray.getBoolean(
                        R.styleable.CustomExoPlayerView_custom_player_show_controller,
                        true
                    )
                )
            }

            if (typedArray.hasValue(R.styleable.CustomExoPlayerView_custom_player_show_full_screen)) {
                setShowFullScreenButton(
                    typedArray.getBoolean(
                        R.styleable.CustomExoPlayerView_custom_player_show_full_screen,
                        true
                    )
                )
            }
            if (typedArray.hasValue(R.styleable.CustomExoPlayerView_showHideFullScreenIcon)) {
                setShowFullScreenIcon(
                    typedArray.getBoolean(
                        R.styleable.CustomExoPlayerView_showHideFullScreenIcon,
                        true
                    )
                )
            }
            if (typedArray.hasValue(R.styleable.CustomExoPlayerView_showHideMute_icon)) {
                setShowMuteIcon(
                    typedArray.getBoolean(
                        R.styleable.CustomExoPlayerView_showHideMute_icon,
                        true
                    )
                )
            }

            typedArray.recycle()
        }
    }
    fun setShowMuteIcon(show: Boolean) {
        if (show) {
            mute.visibility = View.VISIBLE
            unMute.visibility = View.VISIBLE
        } else {
            mute.visibility = View.GONE
            unMute.visibility = View.GONE
        }
    }
     fun setShowFullScreenIcon(show: Boolean) {
        if (show) {
            enterFullScreen.visibility = View.VISIBLE
            exitFullScreen.visibility = View.VISIBLE
        } else {
            enterFullScreen.visibility = View.GONE
            exitFullScreen.visibility = View.GONE
        }
    }


    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
    }

    override fun onTracksChanged(tracks: Tracks) {
    }

    override fun onPlayerError(error: PlaybackException) {
        showRetryView(error.message)
        customPlayerPlayerListener?.let {
            customPlayerPlayerListener!!.onExoPlayerError(errorMessage = error.message)
        }
    }


    @UnstableApi
    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            ExoPlayer.STATE_BUFFERING -> {
                customPlayerPlayerListener?.onExoBuffering()
            }
            ExoPlayer.STATE_ENDED -> {
                customPlayerPlayerListener?.onExoEnded()
                // Update UI: show Play button, hide Pause button
                exoPlay.visibility = View.VISIBLE
                exoPause.visibility = View.GONE
                player.playWhenReady = false
                player.seekTo(0)

            }
            ExoPlayer.STATE_IDLE -> {
                customPlayerPlayerListener?.onExoIdle()
            }
            ExoPlayer.STATE_READY -> {
                customPlayerPlayerListener?.onExoReady()
                // Update UI based on whether the player is playing or paused
                if (player.playWhenReady) {
                    exoPlay.visibility = View.GONE
                    exoPause.visibility = View.VISIBLE
                } else {
                    exoPlay.visibility = View.VISIBLE
                    exoPause.visibility = View.GONE
                }
            }
            else -> { }
        }
    }


    override fun onRepeatModeChanged(repeatMode: Int) {
    }

    override fun onTimelineChanged(timeline: Timeline, reason: Int) {
    }

    @OptIn(UnstableApi::class)
    fun releasePlayer() {
        currPlayWhenReady = player.playWhenReady
        playbackPosition = player.currentPosition
        currentWindow = player.currentMediaItemIndex
        player.stop()
        player.release()
    }

    private fun restartPlayer() {
        player.seekTo(0);
        player.prepare()
    }

    private fun buildMediaItem(source: String): MediaItem {

        return when (PublicFunctions.getMimeType(source)){

            PublicValues.KEY_MP4 -> buildMediaItemMP4(source)
            PublicValues.KEY_M3U8 -> buildMediaHLS(source)
            PublicValues.KEY_MP3 -> buildMediaItemMP4(source)

            else -> buildMediaGlobal(source)
        }
    }

    private fun buildMediaItemMP4(
        source: String
     
    ): MediaItem {
        return MediaItem.Builder()
            .setUri(source)
            .setMimeType(MimeTypes.APPLICATION_MP4)
            .build()
    }

    private fun buildMediaHLS(source: String): MediaItem {
        return MediaItem.Builder()
            .setUri(source)
            .setMimeType(MimeTypes.APPLICATION_M3U8)
            .build()
    }

    private fun buildMediaDash(source: String): MediaItem {
        return MediaItem.Builder()
            .setUri(source)
            .setMimeType(MimeTypes.APPLICATION_MPD)
            .build()
    }

    private fun buildMediaGlobal(source: String): MediaItem {
        return MediaItem.Builder()
            .setUri(source)
            .build()
    }

    fun setCustomPlayerPlayerListener(customPlayerPlayerListener: CustomExoPlayerListener) {
        this.customPlayerPlayerListener = customPlayerPlayerListener
    }

    fun setSource(
        source: String) {

        currSource = source

        val mediaItem = buildMediaItem(source)

        playerView.player = player
        player.playWhenReady = currPlayWhenReady
        player.setMediaItem(mediaItem)
        player.prepare()
    }

    fun seekBackward(backwardValue: Long = 10000) {
        val currentPosition = player.currentPosition
        val seekToPosition = (currentPosition - backwardValue).coerceAtLeast(0)
        player.seekTo(seekToPosition)
        Log.d("CustomExoPlayerView", "Seek backward to: $seekToPosition")
    }

    fun seekForward(forwardValue: Long = 10000) {
        val currentPosition = player.currentPosition
        val seekToPosition = (currentPosition + forwardValue).coerceAtMost(player.duration)
        player.seekTo(seekToPosition)
        Log.d("CustomExoPlayerView", "Seek forward to: $seekToPosition")
    }

    fun setShowControllers(showControllers: Boolean = true) {
        if (showControllers)
            setShowTimeOut(4000)
        else
            setShowTimeOut(0)
    }

    @OptIn(UnstableApi::class)
    fun setShowTimeOut(showTimeoutMs: Int) {
        playerView.controllerShowTimeoutMs = showTimeoutMs
        if (showTimeoutMs == 0)
            playerView.controllerHideOnTouch = false
    }

    fun setMute(mute: EnumMute) {
        when (mute) {
            EnumMute.MUTE -> {
                mutePlayer()
            }
            EnumMute.UNMUTE -> {
                unMutePlayer()
            }
            else -> {
                unMutePlayer()
            }
        }
    }

    fun mutePlayer() {
        currVolume = player.volume
        player.volume = 0f
        showMuteButton()
    }

    fun unMutePlayer() {
        player.volume = currVolume
        showUnMuteButton()
    }

    fun setRepeatMode(repeatMode: EnumRepeatMode = EnumRepeatMode.REPEAT_OFF) {
        this.currRepeatMode = repeatMode
        when (repeatMode) {
            EnumRepeatMode.REPEAT_OFF -> {
                player.repeatMode = Player.REPEAT_MODE_OFF
            }
            EnumRepeatMode.REPEAT_ONE -> {
                player.repeatMode = Player.REPEAT_MODE_ONE
            }
            EnumRepeatMode.REPEAT_ALWAYS -> {
                player.repeatMode = Player.REPEAT_MODE_ALL
            }
            else -> {
                player.repeatMode = Player.REPEAT_MODE_OFF
            }
        }
    }

    @OptIn(UnstableApi::class)
    fun setAspectRatio(aspectRatio: EnumAspectRatio) {
        this.currAspectRatio = aspectRatio
        val value = PublicFunctions.getScreenWidth()
        when (aspectRatio) {
            EnumAspectRatio.ASPECT_1_1 -> playerView.layoutParams =
                FrameLayout.LayoutParams(value, value)
            EnumAspectRatio.ASPECT_4_3 -> playerView.layoutParams =
                FrameLayout.LayoutParams(value, 3 * value / 4)
            EnumAspectRatio.ASPECT_16_9 -> playerView.layoutParams =
                FrameLayout.LayoutParams(value, 9 * value / 16)
            EnumAspectRatio.ASPECT_MATCH -> playerView.layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            EnumAspectRatio.ASPECT_MP3 -> {
                playerView.controllerShowTimeoutMs = 0
                playerView.controllerHideOnTouch = false
                val mp3Height =
                    context.resources.getDimensionPixelSize(R.dimen.player_controller_base_height)
                playerView.layoutParams =
                    FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mp3Height)
            }
            EnumAspectRatio.UNDEFINE -> {
                val baseHeight = resources.getDimension(R.dimen.player_base_height).toInt()
                playerView.layoutParams =
                    FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, baseHeight)
            }
        }
    }

    @OptIn(UnstableApi::class)
    fun setResizeMode(resizeMode: EnumResizeMode) {
        when (resizeMode) {
            EnumResizeMode.FIT -> playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
            EnumResizeMode.FILL -> playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
            EnumResizeMode.ZOOM -> playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            else -> playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
        }
    }

    fun setPlayWhenReady(playWhenReady: Boolean = true) {
        this.currPlayWhenReady = playWhenReady
        player.playWhenReady = playWhenReady
    }
    fun startPlayer() {
        player.playWhenReady = true
        player.playbackState
        // Update UI to show the Pause button and hide the Play button
        exoPlay.visibility = View.GONE
        exoPause.visibility = View.VISIBLE
    }

    fun pausePlayer() {
        player.playWhenReady = false
        player.playbackState
        // Update UI to show the Play button and hide the Pause button
        exoPlay.visibility = View.VISIBLE
        exoPause.visibility = View.GONE
    }




    fun stopPlayer() {
        player.stop()
        player.playbackState
        player.seekTo(0)
    }



    fun setScreenMode(screenMode: EnumScreenMode = EnumScreenMode.MINIMISE) {

        when (screenMode) {
            EnumScreenMode.FULLSCREEN -> {
                getActivity()?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            }
            EnumScreenMode.MINIMISE -> {
                getActivity()?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            }
            else -> {
                getActivity()?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            }
        }

        currScreenMode = screenMode
        setShowScreenModeButton(currScreenMode)
    }

    fun getActivity(): Activity? {
        var context = context
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.baseContext
        }
        return null
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        releasePlayer()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        // Checking the orientation of the screen
        // Checking the orientation of the screen
        if (newConfig!!.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // First Hide other objects (list-view or recyclerview), better hide them using Gone.
            hideSystemUI()
            val params = playerView.layoutParams as FrameLayout.LayoutParams
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
            playerView.layoutParams = params
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // un hide your objects here.
            showSystemUI()
            setAspectRatio(currAspectRatio)
        }
    }

}
