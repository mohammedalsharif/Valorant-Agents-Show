package com.example.valorantagents.data.remote

import com.example.valorantagents.model.Agent
import com.example.valorantagents.model.BaseResponse
import com.example.valorantagents.utils.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET


interface AgentsApiServices {

    @GET("agents")
    suspend fun getAllAgents(): Response<BaseResponse<List<Agent>>>
}