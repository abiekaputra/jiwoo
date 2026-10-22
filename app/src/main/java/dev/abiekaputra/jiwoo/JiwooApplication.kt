package dev.abiekaputra.jiwoo

import android.app.Application

class JiwooApplication : Application() {
    val container by lazy { AppContainer(this) }
}
