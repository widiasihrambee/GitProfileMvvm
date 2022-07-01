package com.example.githubprofile.activities

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubprofile.helpers.Config
import com.example.githubprofile.models.GithubUser
import com.example.githubprofile.services.GithubUserService
import com.example.githubprofile.services.ServiceBuilder
import retrofit2.Call

class MainViewModel: ViewModel() {
    companion object {
        val TAG: String = MainViewModel::class.java.simpleName
    }
    private val _githubUser = MutableLiveData<GithubUser>()
    val githubUser: LiveData<GithubUser> = _githubUser
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    init {
        searchUser(Config.DEFAULT_USER_LOGIN)
    }
    fun searchUser(query: String){
        _isLoading.value = true
        Log.d(TAG, "getDataUserProfileFromAPI: start...")
        val githubUserService: GithubUserService = ServiceBuilder.buildService(GithubUserService::class.java)
        val requestCall: Call<GithubUser> = githubUserService.loginUser(Config.PERSONAL_ACCESS_TOKEN, query)
        Log.d(TAG, "getDataUserFromAPI: ${requestCall.request().url}")
        requestCall.enqueue(object : retrofit2.Callback<GithubUser> {
            override fun onResponse(call: Call<GithubUser>, response: retrofit2.Response<GithubUser>) {
                _isLoading.value = false
                if(response.isSuccessful){
                    val result = response.body()
                    Log.d(TAG, result.toString())
                    _githubUser.postValue(result)
                    Log.d(TAG, "getDataUserFromAPI: onResponse finish...")
                }else{
                    Log.d(TAG, "getDataUserFromAPI: onResponse failed...")
                }
            }
            override fun onFailure(call: Call<GithubUser>, t: Throwable) {
                _isLoading.value = false
                Log.d(TAG, "getDataUserFromAPI: onFailure ${t.message}...")
            }
        })
    }
}

