package com.example.model

import com.example.model.api.MealsWebService
import com.example.model.response.MealsCategoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealsRepository (private  val webService: MealsWebService = MealsWebService()){
    suspend fun getMeals(): MealsCategoryResponse{
        return webService.getMeals()


    }
}