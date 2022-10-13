package com.example.tripsi.screens.travelHistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripsi.functionality.TripDbViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TravelHistoryViewModel: ViewModel() {

     suspend fun deleteTrip (tripId: Int, tripDbViewModel: TripDbViewModel) {
      withContext(Dispatchers.IO) {
            tripDbViewModel.deleteTripById(tripId)
       }
    }
}