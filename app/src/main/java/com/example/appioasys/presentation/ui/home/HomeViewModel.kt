package com.example.appioasys.presentation.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appioasys.R
import com.example.appioasys.data.repository.CompanyListRepository
import com.example.appioasys.data.response.HomeListResult
import com.example.appioasys.data.response.LoginAuthenticationUser
import com.example.appioasys.domain.model.CompanyItem
import com.example.appioasys.domain.model.NoInternetException
import com.example.appioasys.domain.model.ServerException
import com.example.appioasys.domain.model.UnauthorizedException

class HomeViewModel(private val dataSource: CompanyListRepository) : ViewModel() {
    private val companyListMutableLiveData = MutableLiveData<List<CompanyItem>>()
    val companyListLiveData: LiveData<List<CompanyItem>> = companyListMutableLiveData
    private val serverErrorMutableLiveData: MutableLiveData<Int> = MutableLiveData()
    val serverErrorLiveData: LiveData<Int> = serverErrorMutableLiveData
    private val isEmptyCompanyListMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val isEmptyCompanyListLiveData: LiveData<Boolean> = isEmptyCompanyListMutableLiveData
    private val actionToHandleMutableLiveData: MutableLiveData<Int> = MutableLiveData()
    val actionToHandleErrorLiveData: LiveData<Int> = actionToHandleMutableLiveData


    fun getCompanyList(authenticationUser: LoginAuthenticationUser, newText: String?) {
        dataSource.getCompanyList(authenticationUser, newText) { result: HomeListResult ->
            when (result) {
                is HomeListResult.Success -> {
                    if (!result.companyListResult.isNullOrEmpty()) {
                        companyListMutableLiveData.postValue(result.companyListResult)
                        isEmptyCompanyListMutableLiveData.postValue(true)
                    } else {
                        isEmptyCompanyListMutableLiveData.postValue(false)
                    }
                }
                is HomeListResult.Error -> {
                    when (result.throwable) {
                        is UnauthorizedException -> {
                            actionToHandleMutableLiveData.postValue(R.string.unauthorized_user_text)
                        }
                        is ServerException -> {
                            actionToHandleMutableLiveData.postValue(R.string.server_error_text)
                        }
                        is NoInternetException -> {
                            serverErrorMutableLiveData.postValue(R.string.no_internet_connection_error_text)
                        }
                        else -> serverErrorMutableLiveData.postValue(R.string.generic_error_text)
                    }
                }
            }
        }
    }

    class HomeViewModelFactory(private val dataSource: CompanyListRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return HomeViewModel(dataSource) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}