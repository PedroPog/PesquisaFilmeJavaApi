package com.aula.projetocosumoapi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aula.projetocosumoapi.models.MovieModel;
import com.aula.projetocosumoapi.models.Rating;
import com.aula.projetocosumoapi.retrofit.ApiService;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    EditText editTextNomeFilme,editTextAno;
    RatingBar ratingbar;
    Spinner spinnerPlot;
    Button buttonPesquisa;
    ImageView imageViewPoster;
    TextView textViewTitle,textViewAno,textViewPlot,textViewTimer;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://www.omdbapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    ApiService apiService = retrofit.create(ApiService.class);

    final List<String> datas = new ArrayList<String>();
    String tipoDeResumo = "none";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        carregar();

        buttonPesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validacao()){
                    spinner();
                    if(tipoDeResumo == "none"){
                        //Log.i("plot","Sem plot");
                        pesquisa();
                    }else{
                        //Log.i("plot","Com plot");
                        plot();
                    }
                }
            }
        });
    }
    private void pesquisa(){
        textViewPlot.setText("");
        textViewTitle.setText("filme a ser exibido...");
        if(editTextAno == null){
            Call<MovieModel> call = apiService.buscarPorTitulo(editTextNomeFilme.getText().toString());
            call.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    MovieModel movieModel = response.body();
                    textViewTitle.setText(movieModel.Title);
                    textViewAno.setText("Lançamento: "+movieModel.Released);
                    textViewTimer.setText(movieModel.Runtime);
                    Picasso.get().load(movieModel.Poster).into(imageViewPoster);

                    for(Rating rating: movieModel.Ratings){
                        if(rating.Source.equals("Internet Movie Database")){
                            //Log.i("Rating",rating.Value);
                            float valorClass = converterClassificacao(rating.Value);
                            ratingbar.setRating(valorClass);

                        }
                    }
                    if(textViewTitle.getText().toString() == ""){
                        toat();
                    }

                }
                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    toat();
                }
            });
        }
        else{
            Call<MovieModel> call = apiService.buscarPorTituloeAno(editTextNomeFilme.getText().toString(), editTextAno.getText().toString());
            call.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    MovieModel movieModel =response.body();
                    textViewTitle.setText(movieModel.Title);
                    textViewAno.setText("Lançamento: "+movieModel.Released);
                    textViewTimer.setText(movieModel.Runtime);
                    Picasso.get().load(movieModel.Poster).into(imageViewPoster);
                    for(Rating rating: movieModel.Ratings){
                        if(rating.Source.equals("Internet Movie Database")){
                            //Log.i("Rating",rating.Value);
                            float valorClass = converterClassificacao(rating.Value);
                            ratingbar.setRating(valorClass);

                        }
                    }
                    if(textViewTitle.getText().toString() == ""){
                        toat();
                    }
                }
                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    toat();
                }
            });
        }
    }

    private void toat(){
        Toast toast = Toast.makeText(this, "Não existe o filme!", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
        toast.show();
        Log.i("plot","Sem filme!");
    }

    private void plot(){
        textViewTitle.setText("filme a ser exibido...");
        textViewPlot.setText("");
        Call<MovieModel> call = apiService.buscarPorTituloeAnoPlot(editTextNomeFilme.getText().toString(), editTextAno.getText().toString(),tipoDeResumo);
        call.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                MovieModel movieModel =response.body();
                textViewTitle.setText(movieModel.Title);
                textViewAno.setText("Lançamento: "+movieModel.Released);

                textViewPlot.setText(movieModel.Plot);
                textViewTimer.setText(movieModel.Runtime);

                Picasso.get().load(movieModel.Poster).into(imageViewPoster);

                for(Rating rating: movieModel.Ratings){
                    if(rating.Source.equals("Internet Movie Database")){
                        //Log.i("Rating",rating.Value);
                        float valorClass = converterClassificacao(rating.Value);
                        ratingbar.setRating(valorClass);
                    }
                }
                if(textViewTitle.getText().toString() == ""){
                    toat();
                }
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
                toat();
            }
        });
    }
    private boolean validacao(){
        if(editTextNomeFilme.getText().length() < 2){
            editTextNomeFilme.setError("O título deve ter pelo menos 2 caracteres!");
            return false;
        }
        if (editTextAno.length() == 0) {
            spinner();
            return true;
        } else if (editTextAno.length() == 4) {
            spinner();
            return true;
        } else {
            editTextAno.setError("Digite o ano completo com 4 dígitos ou deixe em branco!");
            return false;
        }
    }
    private void spinner(){
        spinnerPlot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedValue = adapterView.getItemAtPosition(i).toString();
                if (selectedValue.equals("full")) {
                    tipoDeResumo = selectedValue.toString();
                }else {
                    tipoDeResumo = "none";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                toat();
            }
        });
    }
    private void carregar(){
        editTextNomeFilme = findViewById(R.id.editTextNomeFilme);
        editTextAno = findViewById(R.id.editTextAno);
        buttonPesquisa = findViewById(R.id.buttonPesquisar);
        imageViewPoster = findViewById(R.id.imageViewPoster);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewAno = findViewById(R.id.textViewAno);
        textViewPlot = findViewById(R.id.textViewPlot);
        textViewTimer = findViewById(R.id.textViewTimer);
        spinnerPlot = findViewById(R.id.spinnerPlot);
        ratingbar = findViewById(R.id.ratingbar);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Resumo_do_filme,androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinnerPlot.setAdapter(adapter);
    }

    private float converterClassificacao(String valorClassificacao) {
        // Dividir a classificação em partes (ex: "5.7/10")
        String[] partes = valorClassificacao.split("/");
        if (partes.length == 2) {
            // Converter a parte numérica para float
            float valor = Float.parseFloat(partes[0]);
            // Normalizar para um intervalo de 0 a 5
            return (valor / 10) * 5;
        } else {
            return 0;
        }
    }


}