package com.example.valorantagents.utils

import com.example.valorantagents.model.Agent

data class MainUiState(
    val isLoading:Boolean,
    val isError:Boolean,
    val agents:List<Agent>,
    val message:String?
){

    constructor(): this (
        isLoading = true,
        isError = false,
        agents = mutableListOf(),
        message = null
    )
}