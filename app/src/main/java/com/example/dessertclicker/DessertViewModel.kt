package com.example.dessertclicker

import androidx.lifecycle.ViewModel
import com.example.dessertclicker.data.DessertUiState
import com.example.dessertclicker.model.Dessert
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.example.dessertclicker.data.Datasource

class DessertViewModel: ViewModel() {
    val desserts = Datasource.dessertList
    private val _uiState = MutableStateFlow(
        DessertUiState(
            dessertImageId = desserts.first().imageId,
            currentDessertPrice = desserts.first().price
        )
    )
    val uiState: StateFlow<DessertUiState> = _uiState.asStateFlow()


    private fun determineDessertToShow(
        desserts: List<Dessert>,
        dessertsSold: Int
    ): Dessert {
        var dessertToShow = desserts.first()
        for (dessert in desserts) {
            if (dessertsSold >= dessert.startProductionAmount) {
                dessertToShow = dessert
            } else {
                // The list of desserts is sorted by startProductionAmount. As you sell more desserts,
                // you'll start producing more expensive desserts as determined by startProductionAmount
                // We know to break as soon as we see a dessert who's "startProductionAmount" is greater
                // than the amount sold.
                break
            }
        }

        return dessertToShow
    }

    fun onDessertClicked() {
        _uiState.update { currentState ->
            val newDessertsSold = currentState.dessertsSold + 1
            val currentDessertRevenue = currentState.revenue + currentState.currentDessertPrice
            val newDessert = determineDessertToShow(desserts, newDessertsSold)

            currentState.copy(
                dessertsSold = newDessertsSold,
                revenue = currentDessertRevenue,
                dessertImageId = newDessert.imageId,
                currentDessertPrice = newDessert.price

            )


        }
    }

//    fun onDessertClicked() {
//        _uiState.update { currentState ->
//            revenue += currentDessertPrice
//            dessertsSold++
//
//            // Show the next dessert
//            val dessertToShow = determineDessertToShow(desserts, dessertsSold)
//            currentDessertImageId = dessertToShow.imageId
//            currentDessertPrice = dessertToShow.price
//
//        }
//            // Update the revenue
//    }
}