package dev.abiekaputra.jiwoo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.abiekaputra.jiwoo.ui.JiwooApp
import dev.abiekaputra.jiwoo.ui.theme.JiwooTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val container = (application as JiwooApplication).container
        setContent {
            JiwooTheme {
                val viewModel: JiwooViewModel =
                    viewModel(factory = JiwooViewModel.factory(container))
                JiwooApp(viewModel)
            }
        }
    }
}
