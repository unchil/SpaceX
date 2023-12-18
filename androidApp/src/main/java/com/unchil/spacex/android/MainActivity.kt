package com.unchil.spacex.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jetbrains.handson.kmm.shared.entity.RocketLaunch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RocketLaunch()
                }
            }
        }
    }
}


@Composable
fun RocketLaunch() {

    val context = LocalContext.current
    val viewModel = remember { LaunchViewModel(context) }
    val resultState = viewModel.recvResultListState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize().padding(20.dp)

    ) {

        TextButton(
            modifier = Modifier.align(Alignment.TopEnd),
            onClick = {
                viewModel.onEvent(LaunchViewModel.Event.reloadLaunches)
            }) {
            Text("Reload",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                )
        }


        Text(
            text = "SpaceX Launches",
            modifier = Modifier.align(Alignment.TopStart).padding(top = 20.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineMedium,

            )




    when (resultState.value) {
        is LaunchViewModel.LoadableLaunches.Error -> {

            Text(
                text = (resultState.value as LaunchViewModel.LoadableLaunches.Error).message,
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center
            )

        }

        is LaunchViewModel.LoadableLaunches.Loading -> {

            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )

        }

        is LaunchViewModel.LoadableLaunches.Result -> {

            val resultList = (resultState.value as LaunchViewModel.LoadableLaunches.Result).result

            if (resultList.isEmpty()) {


                Text(
                    text = "데이터가 존재하지 않습니다.",
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )


            } else {
                val state = rememberLazyListState()


                LazyColumn(
                    state = state,
                    modifier = Modifier.align(Alignment.Center).padding(top = 60.dp)
                        .clip(ShapeDefaults.Medium)

                ) {
                    items(count = resultList.size) {
                        ItemView(resultList[it])
                        Divider(modifier = Modifier.fillMaxWidth(0.9f))
                    }

                }


            }

        }
    }


}

}



@Composable
fun ItemView(item: RocketLaunch) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .background(color = Color.LightGray.copy(alpha = 0.3f))
            .clip(ShapeDefaults.ExtraSmall)
            .padding(all = 10.dp)
    ) {

        Text(text = "Launch name:  ${item.missionName}")

        Text(
            text =   if (item.launchSuccess == true) "Successful" else "Unsuccessful" ,
            color = if (item.launchSuccess == true) Color.Green else Color.Red
        )

        Text(text = "Launch year:  ${item.launchYear}")

        Text(
            maxLines =  5,
            text = "Launch details:  ${item.details}"
        )

    }

}

