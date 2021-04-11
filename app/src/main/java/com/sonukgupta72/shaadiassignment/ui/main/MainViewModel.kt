package com.sonukgupta72.shaadiassignment.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sonukgupta72.shaadiassignment.corutine.Resource
import com.sonukgupta72.shaadiassignment.db.DataModel
import com.sonukgupta72.shaadiassignment.db.RepositoryManager
import com.sonukgupta72.shaadiassignment.network.ApiService
import com.sonukgupta72.shaadiassignment.network.ProfileResponseDataModule
import com.sonukgupta72.shaadiassignment.network.RtfClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers

class MainViewModel : ViewModel() {

    private val error: MutableLiveData<String> = MutableLiveData()
    private val progressStatus: MutableLiveData<Boolean> = MutableLiveData()
    private val compositeDisposable = CompositeDisposable()
    private val repositoryManager: RepositoryManager = RepositoryManager.getRepositoryManager()
    private var profileList: List<ProfileResponseDataModule.ProfileDataModel>?= null
    private var apiSservice: ApiService = RtfClient.getApiServices()

    fun getProfiles() {
        progressStatus.value = true
        val call = apiSservice.getProfiles(10)
        compositeDisposable.add(
            call.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onSuccess, this::onError)
        )
    }

    fun getProfilesCr() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = apiSservice.getProfilesCr(10)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, msg = exception.message ?: "Error Occurred!"))
        }
    }

    fun onSuccess(profileResponseDataModule: ProfileResponseDataModule) {
        profileList = profileResponseDataModule.results
        repositoryManager.deleteAll()?.let {
            compositeDisposable.add(
                it.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onDeletion, this::onError)
            )
        }
    }

    private fun onDeletion() {
        profileList?.let {
            repositoryManager.insertAll(getProfileDataModel(it))?.let { liveData ->
                compositeDisposable.add(
                    liveData.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onInsertion, this::onError)
                )
            }
        }
    }

    fun updateProfile(dataModel: DataModel, responseMessage: String) {
        repositoryManager.updateDateModel(dataModel, responseMessage)?.let { liveData ->
            compositeDisposable.add(
                liveData.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({}, this::onError)
            )
        }
    }

    private fun onInsertion(){
        progressStatus.value = false
    }

    private fun getProfileDataModel(profileList: List<ProfileResponseDataModule.ProfileDataModel>): List<DataModel> {
        val dataModel = ArrayList<DataModel>()
        for (model in profileList) {
            dataModel.add(DataModel(model, null))
        }
        return dataModel
    }

    private fun onError(t: Throwable) {
        error.value = t.message?: "Api Failure"
        progressStatus.value = false
    }

    private fun onErrorE(e: Exception) {
        error.value = e.message?: "Api Failure"
        progressStatus.value = false
    }

    fun getResultModel(): LiveData<List<DataModel>>? {
        return repositoryManager.getLiveData()
    }

    fun getError(): MutableLiveData<String> {
        return error
    }

    fun getProgress(): MutableLiveData<Boolean> {
        return progressStatus
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
