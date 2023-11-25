package com.example.viacep

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.viacep.api.Api
import com.example.viacep.databinding.ActivityMainBinding
import com.example.viacep.model.Endereco
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = Color.parseColor("#0277BD")
        val actionBar = supportActionBar
        actionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#0277BD")))

        //Configurar retrofit
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://viacep.com.br/ws/")
            .build()
            .create(Api::class.java)

        binding.btBuscarCep.setOnClickListener {
            val cep = binding.editCep.text.toString()

            if (cep.isEmpty()) {
                Toast.makeText(this, "Informe o cep!", Toast.LENGTH_SHORT).show()
            } else {

                retrofit.setEndereco(cep).enqueue(object : Callback<Endereco> {
                    override fun onResponse(call: Call<Endereco>, response: Response<Endereco>) {
                       if (response.code() == 200){
                           val logradouro = response.body()?.logradouro.toString()
                           val bairro = response.body()?.bairro.toString()
                           val localidade = response.body()?.localidade.toString()
                           val uf = response.body()?.uf.toString()
                           setFormularios(logradouro, bairro, localidade, uf)
                       }else{
                           Toast.makeText(applicationContext, "Cep inválido!", Toast.LENGTH_SHORT).show()
                       }
                    }

                    override fun onFailure(call: Call<Endereco>, t: Throwable) {
                        Toast.makeText(applicationContext, "Erro inesperado!", Toast.LENGTH_SHORT).show()
                    }

                })
            }

        }
    }

    private fun setFormularios(logradouro: String, bairro: String, localidade: String, uf: String){
        binding.editLogradouro.setText(logradouro)
        binding.editBairro.setText(bairro)
        binding.editCidade.setText(localidade)
        binding.editEstado.setText(uf)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflate = menuInflater
        inflate.inflate(R.menu.menu_principal,menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.reset->{

                val limparEditCep = binding.editCep
                val limparEditLogradouro = binding.editLogradouro
                val limparEditBairro = binding.editBairro
                val limparEditCidade = binding.editCidade
                val limparEditEstado = binding.editEstado

                limparEditCep.setText("")
                limparEditLogradouro.setText("")
                limparEditBairro.setText("")
                limparEditCidade.setText("")
                limparEditEstado.setText("")


            }
        }


        return super.onOptionsItemSelected(item)
    }
}