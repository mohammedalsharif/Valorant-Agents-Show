package com.example.valorantagents.ui.agents

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valorantagents.data.AgentsRepository
import com.example.valorantagents.model.Agent
import com.example.valorantagents.utils.MainUiState
import com.example.valorantagents.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgentListScreenViewModel @Inject constructor(val agentsRepository: AgentsRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState get() = _uiState.asStateFlow()
    val list = listOf("")

    init {
        getAllAgents()

    }

    private fun getAllAgents() {
        viewModelScope.launch {
            agentsRepository.getAllAgents().collectLatest {

                when (it) {
                    is Resource.Loading -> {
                        _uiState.update { state ->
                            Log.e("TAG", "getAllAgents: Loaging", )
                            state.copy(
                                isLoading = true, isError = false, agents = it.data?.data  ?: emptyList()
                            )
                        }
                    }


                    is Resource.Success -> {
                        _uiState.update { state ->
                            Log.e("TAG", "getAllAgents: Sucsses", )
                            state.copy(
                                isLoading = false,
                                agents = it.data?.data  ?: emptyList()
                            )
                        }
                    }

                    is Resource.Error -> {
                        Log.e("TAG", "getAllAgents: Error ${it.message}")
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                isError = true,
                                agents = it.data?.data ?: emptyList(),
                                message = it.message
                            )
                        }
                    }
                }

            }
        }
    }

}