package com.ipgan.cienmilsaboresandroid.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipgan.cienmilsaboresandroid.model.Post
import com.ipgan.cienmilsaboresandroid.repository.PostRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class PostViewModel() : ViewModel() {
    private val repository = PostRepository()
    private val _post = mutableStateListOf<Post>()
    val posts: List<Post> = _post
    var mensaje by mutableStateOf<String>("")
    fun recuperarPost(){
        viewModelScope.launch {
            try {
                var data = repository.getPosts()
                if (data != null) {
                    _post.clear()
                    _post.addAll(data)
                } else {
                    mensaje = "problemas de comunicacion"
                }
            } catch (e: Exception) {
                mensaje = "error : ${e.message}"
            }
        }
    }

    init {
        recuperarPost()
    }


}