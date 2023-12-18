package com.unchil.spacex.android

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetbrains.handson.kmm.shared.SpaceXSDK
import com.jetbrains.handson.kmm.shared.cache.DatabaseDriverFactory
import com.jetbrains.handson.kmm.shared.entity.RocketLaunch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class LaunchViewModel(context: Context) : ViewModel() {


    private val _recvResultListState: MutableStateFlow<LoadableLaunches>
            = MutableStateFlow(LoadableLaunches.Result(emptyList()))

    val recvResultListState: StateFlow<LoadableLaunches> = _recvResultListState

    val repository = SpaceXSDK(  databaseDriverFactory = DatabaseDriverFactory(context))

    init{
        loadableLaunches(false)
    }

    fun loadableLaunches(forceReload:Boolean){
        viewModelScope.launch {
            try {
                _recvResultListState.value = LoadableLaunches.Result( repository.getLaunches(forceReload))
            } catch (e:Exception){
                e.localizedMessage?.let {
                    _recvResultListState.value = LoadableLaunches.Error(it)
                }

            }
        }
    }


    fun onEvent(event: Event){
        when(event){
            Event.reloadLaunches -> {
                _recvResultListState.value = LoadableLaunches.Loading(true)
                loadableLaunches(true)
            }
        }
    }

    sealed class Event{
        object reloadLaunches: Event()

    }

    sealed class LoadableLaunches {
        data class Loading(val isLoading:Boolean) : LoadableLaunches()
        data class  Result(val result: List<RocketLaunch> ) :  LoadableLaunches()
        data class Error(val message: String) :  LoadableLaunches()
    }

}