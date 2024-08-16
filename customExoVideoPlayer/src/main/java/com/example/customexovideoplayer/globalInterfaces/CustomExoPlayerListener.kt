package com.example.customexovideoplayer.globalInterfaces

interface CustomExoPlayerListener {

    fun onExoPlayerStart() {}

    fun onExoPlayerFinished() {}

    fun onExoPlayerLoading() {}

    fun onExoPlayerError(errorMessage: String?) {}

    fun onExoBuffering() {}

    fun onExoEnded() {}

    fun onExoIdle() {}

    fun onExoReady() {}

}