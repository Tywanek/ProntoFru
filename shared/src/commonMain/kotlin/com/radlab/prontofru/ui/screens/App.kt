package com.radlab.prontofru.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.radlab.prontofru.di.sharedModule
import com.radlab.prontofru.domain.model.Flight
import com.radlab.prontofru.ui.viewmodel.FlightUiState
import com.radlab.prontofru.ui.viewmodel.FlightViewModel
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel
import org.koin.dsl.koinConfiguration

@Composable
fun App() {
    KoinApplication(
        configuration = koinConfiguration {
            modules(sharedModule)
        },
    ) {
        AppContent()
    }
}

@Composable
private fun AppContent() {
    val viewModel = koinViewModel<FlightViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    MaterialTheme {
        Column(
            modifier = Modifier
                .background(Color.White)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = { viewModel.fetchFlights() },
                modifier = Modifier.padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
            ) {
                Text("Search Weekend Flights", color = Color.White)
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                AnimatedVisibility(
                    visible = uiState is FlightUiState.Loading,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(top = 50.dp),
                        color = Color(0xFF1976D2)
                    )
                }

                AnimatedVisibility(
                    visible = uiState is FlightUiState.Success,
                    enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn(),
                    exit = fadeOut()
                ) {
                    val flights = (uiState as? FlightUiState.Success)?.flights ?: emptyList()

                    if (flights.isEmpty()) {
                        Text(
                            "No flights found. Try searching!",
                            modifier = Modifier.padding(top = 50.dp),
                            color = Color.Gray
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            items(flights) { flight ->
                                FlightCard(flight)
                            }
                        }
                    }
                }

                AnimatedVisibility(
                    visible = uiState is FlightUiState.Error,
                    enter = fadeIn()
                ) {
                    val message = (uiState as? FlightUiState.Error)?.message ?: "Error"
                    Text(
                        text = "Oops! $message",
                        color = Color.Red,
                        modifier = Modifier.padding(top = 50.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun FlightCard(flight: Flight) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = flight.origin,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = " -> ",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF1976D2)
                    )
                    Text(
                        text = flight.destination,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Out: ${flight.departureDate}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = "Ret: ${flight.returnDate}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${flight.price} PLN",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF1976D2),
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "per person",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.LightGray
                )
            }
        }
    }
}
