package com.example.appioasys.presentation.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appioasys.R
import com.example.appioasys.data.response.HomeListResult
import com.example.appioasys.data.repository.HomeRepository
import com.example.appioasys.data.response.LoginAuthenticationUser
import com.example.appioasys.domain.model.CompanyItem
import java.io.IOException

class HomeViewModel(private val dataSource: HomeRepository) : ViewModel() {
    private val companyListMutableLiveData = MutableLiveData<List<CompanyItem>>()
    val companyListLiveData: LiveData<List<CompanyItem>> get() = companyListMutableLiveData
    private val companyErrorMutableLiveData: MutableLiveData<Int> = MutableLiveData()
    val companyErrorLiveData: LiveData<Int> get() = companyErrorMutableLiveData
    private val companyContentMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val companyLiveDataContent: LiveData<Boolean> get() = companyContentMutableLiveData


    fun getCompanyList(authenticationUser: LoginAuthenticationUser, newText: String?) {
        dataSource.getCompanyList(authenticationUser, newText) { result: HomeListResult ->
            when (result) {
                is HomeListResult.Success -> {
                    if (!result.companyListResult.isNullOrEmpty()){
                        companyListMutableLiveData.postValue(result.companyListResult)
                        companyContentMutableLiveData.postValue(true)
                    }else{
                        companyContentMutableLiveData.postValue(false)
                    }
                }
                is HomeListResult.ApiError -> {
                    if (result.statusCode == 401) {
                        companyErrorMutableLiveData.postValue(R.string.server_error_text)
                    }
                }
                is HomeListResult.SeverError -> {
                    if (result.throwable is IOException) {
                        companyErrorMutableLiveData.postValue(R.string.no_internet_connection_error_text)
                    } else {
                        companyErrorMutableLiveData.postValue(R.string.generic_error_text)
                    }
                }
            }
        }
    }

    class HomeViewModelFactory(private val dataSource: HomeRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return HomeViewModel(dataSource) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}