package com.example.customexovideoplayer.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.customexovideoplayer.model.PlayerControllerModel

class PlayerControllerViewModel : ViewModel() {

    private val playerControllerModel = MutableLiveData<PlayerControllerModel>()

    fun getPlayerControllerModel(): MutableLiveData<PlayerControllerModel> {
        return playerControllerModel
    }

    fun setPlayerControllerModel(model: PlayerControllerModel) {
        playerControllerModel.postValue(model)
    }


}