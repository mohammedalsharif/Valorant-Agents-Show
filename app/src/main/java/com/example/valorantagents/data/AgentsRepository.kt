package com.example.valorantagents.data

import com.example.valorantagents.data.remote.AgentsApiServices
import com.example.valorantagents.model.Agent
import com.example.valorantagents.model.BaseResponse
import com.example.valorantagents.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class AgentsRepository @Inject constructor(val agentsApiServices: AgentsApiServices) {


    fun getAllAgents(): Flow<Resource<BaseResponse<List<Agent>>>> {
        return wrapWithFlow(agentsApiServices::getAllAgents)
    }

    fun <T> wrapWithFlow(function: suspend () -> Response<T>): Flow<Resource<T>> {
        return flow {
            try {
                val result = function()
                emit(Resource.Loading())
                if (result.isSuccessful) {
                    emit(Resource.Success(result.body()))
                } else {

                    emit(Resource.Error(result.message()))
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }
    }
}