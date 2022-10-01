package cosmin.dev.stopwatch

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cosmin.dev.stopwatch.service.StopwatchService
import cosmin.dev.stopwatch.ui.theme.StopwatchTheme

class MainActivity : ComponentActivity() {
    private var isBound by mutableStateOf(false)
    @OptIn(ExperimentalAnimationApi::class)
    private lateinit var stopwatchService: StopwatchService
    @OptIn(ExperimentalAnimationApi::class)
    private val connection = object: ServiceConnection{
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as StopwatchService.StopwatchBinder
            stopwatchService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isBound = false
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    override fun onStart() {
        super.onStart()
        Intent(this, StopwatchService::class.java).also { intent->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StopwatchTheme {
                if (isBound){
                    // call the main screen composable
                    MainScreen(stopwatchService = stopwatchService)
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        isBound = false
    }
}

