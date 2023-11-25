package com.example.viacep.api

import com.example.viacep.model.Endereco
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {
    @GET("ws/{cep}/json/")
    fun setEndereco(@Path("cep") cep: String): Call<Endereco>
}