package com.aula.projetocosumoapi.retrofit;

import com.aula.projetocosumoapi.models.MovieModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("?apikey=2785f90b")
    Call<MovieModel> buscarPorTitulo(@Query("t") String titulo);

    @GET("?apikey=2785f90b")
    Call<MovieModel> buscarPorTituloeAno(@Query("t") String titulo,@Query("y") String ano);

    @GET("?apikey=2785f90b")
    Call<MovieModel> buscarPorTituloeAnoPlot(
            @Query("t") String titulo,
            @Query("y") String ano,
            @Query("plot") String plot);

}
