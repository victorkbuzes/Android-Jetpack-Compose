package com.example.mealapp.ui.meals

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.MealsRepository
import com.example.model.response.MealsCategoryResponse
import com.example.model.response.MealsResponse
import kotlinx.coroutines.*

class MealsCategoriesViewModel (private  val repository: MealsRepository = MealsRepository()):ViewModel() {

    init {
            viewModelScope.launch(Dispatchers.IO) {

            val meals = getMeals()
            mealsState.value = meals
        }


    }
    val mealsState: MutableState<List<MealsResponse>> = mutableStateOf(emptyList<MealsResponse>())


    private suspend fun getMeals(): List<MealsResponse> {
        return repository.getMeals().categories
    }
}