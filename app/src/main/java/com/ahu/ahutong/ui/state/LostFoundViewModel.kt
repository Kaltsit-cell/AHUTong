package com.ahu.ahutong.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahu.ahutong.data.AHURepository
import com.ahu.ahutong.data.crawler.model.adwnh.AllCampus
import com.ahu.ahutong.data.crawler.model.adwnh.AllLostFoundType
import com.ahu.ahutong.data.crawler.model.adwnh.LostFoundResponse
import kotlinx.coroutines.launch

class LostFoundViewModel : ViewModel() {
    var allCampus by mutableStateOf<AllCampus?>(null)
    var campusLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var typeLoading by mutableStateOf(false)
    var allLostFoundType by mutableStateOf<AllLostFoundType?>(null)
    var listLoading by mutableStateOf(false)
    var lostFoundList by mutableStateOf<LostFoundResponse?>(null)

    fun getAllCampus() = viewModelScope.launch {
        campusLoading = true
        try {
            val result = AHURepository.getAllCampus()
            if (result.code == 0){
                allCampus = result.data
                errorMessage = null
            } else{
                    errorMessage = result.msg
            }
        } catch (t: Throwable) {
            errorMessage = t.message ?:"获取校区失败"
        }finally {
            campusLoading = false
        }
    }

    fun getAllLostFoundType() = viewModelScope.launch {
        typeLoading = true
        try {
            val result = AHURepository.getAllLostFoundType()
            if (result.code == 0){
                allLostFoundType = result.data
                errorMessage = null
            } else{
                errorMessage = result.msg
            }
        } catch (t: Throwable) {
            errorMessage = t.message ?:"获取类型失败"
        }finally {
            typeLoading = false
        }
    }
    fun getLostFoundList() = viewModelScope.launch {
        listLoading = true
        try {
            val result = AHURepository.getLostFoundList(1,100,1)
            if (result.code == 0){
                lostFoundList = result.data
                errorMessage = null
            } else{
                errorMessage = result.msg
            }
        } catch (t: Throwable) {
            errorMessage = t.message ?:"获取类型失败"
        }finally {
            typeLoading = false
        }
    }
    init {
        getAllCampus()
        getAllLostFoundType()
        getLostFoundList()
    }
}