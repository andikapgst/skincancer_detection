package com.dicoding.asclepius.view.home

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    private val _currentImageUri = MutableLiveData<Uri>()
    val currentImageUri: LiveData<Uri> = _currentImageUri

    fun setCurrentImageUri(uri: Uri) {
        _currentImageUri.value = uri
    }
}