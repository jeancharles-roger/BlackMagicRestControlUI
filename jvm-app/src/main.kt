import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import bm.ui.AppContext
import bm.ui.MainView

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        MainView(AppContext())
    }
}
