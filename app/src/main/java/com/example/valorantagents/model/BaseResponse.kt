package com.example.valorantagents.model

data class BaseResponse<T> (
    val status:Int,
    val data:T
)