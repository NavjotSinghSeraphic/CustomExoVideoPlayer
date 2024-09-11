package com.example.customexovideoplayer.globalInterfaces

interface CustomExoPlayerListener {
    fun onExoPlayerError(errorMessage: String?) {}

    fun onExoBuffering() {}

    fun onExoEnded() {}

    fun onExoIdle() {}

    fun onExoReady() {}

}