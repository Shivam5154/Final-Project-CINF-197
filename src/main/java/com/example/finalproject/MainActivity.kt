import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigator()
        }
    }
}

@Composable
fun AppNavigator() {
    var showLaunchScreen by remember { mutableStateOf(true) }

    if (showLaunchScreen) {
        LaunchScreen(onContinue = { showLaunchScreen = false })
    } else {
        ParkingLotApp()
    }
}

@Composable
fun LaunchScreen(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the Parking Lot Helper!", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))
        Button(
            onClick = onContinue,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
        ) {
            Text("Launch App")
        }
    }
}

@Composable
fun ParkingLotApp() {
    // I struggled with understanding the "mutableStateOf" and had to go through many Stack Overflow
    // projects to understand that it updates the UI and also the state of the thing you are changing.
    var carPlate by remember { mutableStateOf("") }
    var carMake by remember { mutableStateOf("") }
    var openSpots by remember { mutableStateOf(25) }
    // This makes sure  that the cars can't be parked if the lot is full.
    val parkedCars = remember { mutableStateListOf<Pair<String, String>>() }
    var searchLot by remember { mutableStateOf("") }
    //I added this "snackbar" to show user that the car was parked successfully
    var successMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Parking Lot Helper", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        TextField(
            value = carPlate,
            onValueChange = { carPlate = it },
            label = { Text("License Plate") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = carMake,
            onValueChange = { carMake = it },
            label = { Text("Car Model") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Available Spots: $openSpots", fontSize = 18.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    if (openSpots > 0 && carPlate.isNotBlank() && carMake.isNotBlank()) {
                        parkedCars.add(Pair(carPlate, carMake))
                        openSpots--
                        carPlate = ""
                        carMake = ""
                        successMessage = "Car parked in our lot successfully! :) "
                    }
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
                Text("Park Car")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if (parkedCars.isNotEmpty()) {
                        // This function prevents removing cars if the list is empty.
                        parkedCars.removeLast()
                        openSpots++
                        successMessage = ""
                    }
                },
                modifier = Modifier.weight(1f),
                //I used red for the withdraw car button for user readability purposes
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Withdraw Car")
            }
        }

        if (successMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(15.dp))
            Text(successMessage, color = Color.Green, fontSize = 15.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = searchLot,
            onValueChange = { searchLot = it },
            label = { Text("Search Parked Cars") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // This function runs through the make/model, and car license plate to search the lot
        Column(modifier = Modifier.fillMaxHeight()) {
            parkedCars.filter {
                it.first.contains(searchLot, ignoreCase = true) || it.second.contains(searchLot, ignoreCase = true)
            }.forEachIndexed { index, car ->
                Text("${index + 1}. License: ${car.first}, Model: ${car.second}", fontSize = 16.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LaunchScreenPreview() {
    LaunchScreen(onContinue = {})
}

@Preview(showBackground = true)
@Composable
fun ParkingLotAppPreview() {
    ParkingLotApp()
}
