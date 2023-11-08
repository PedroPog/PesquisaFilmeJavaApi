package com.aula.projetocosumoapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.aula.projetocosumoapi.adapter.CustomAdapter;
import com.aula.projetocosumoapi.models.ApiResponse;
import com.aula.projetocosumoapi.models.MovieModel;
import com.aula.projetocosumoapi.models.MoviesModel;
import com.aula.projetocosumoapi.models.Rating;
import com.aula.projetocosumoapi.retrofit.ApiService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListaFilmeActivity extends AppCompatActivity {

    EditText editTextNomeFilme;
    Button buttonPesquisar;

    ListView listViewFilme;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://www.omdbapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    ApiService apiService = retrofit.create(ApiService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_filme);
        carregarXml();
        buttonPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gerar();
            }
        });



    }

    private void gerar(){
        //editTextNomeFilme.setText("");
            Call<ApiResponse> call = apiService.buscarTodos(editTextNomeFilme.getText().toString());
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        ApiResponse apiResponse = response.body();
                        if (apiResponse != null) {
                            ArrayList<MoviesModel> apiResponses =  apiResponse.Search;
                            if(apiResponses != null){
                                CustomAdapter adapter = new CustomAdapter(ListaFilmeActivity.this, apiResponses);
                                listViewFilme.setAdapter(adapter);
                            }else{
                                Log.e("Teste","Erro null");
                            }
                           Log.e("Teste","Ok");
                        }
                    editTextNomeFilme.setText("");
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    // Trate a falha aqui
                    t.printStackTrace(); // Esta linha é opcional e imprime o rastreamento da pilha da exceção
                }

            });
    }
    private void carregarXml(){
        editTextNomeFilme = findViewById(R.id.editTextNomeFilme);
        buttonPesquisar = findViewById(R.id.buttonPesquisar);
        listViewFilme = findViewById(R.id.listViewFilme);
    }
}