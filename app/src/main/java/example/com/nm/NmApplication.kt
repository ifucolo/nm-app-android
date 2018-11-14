package example.com.nm

import android.app.Application
import com.facebook.stetho.Stetho
import example.com.nm.feature.di.appModule
import example.com.nm.feature.di.prefModule
import example.com.nm.feature.di.remoteDataSourceModule
import org.koin.android.ext.android.startKoin
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

class NmApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(prefModule, appModule, remoteDataSourceModule))

        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
            .setDefaultFontPath("fonts/Gibson.ttf")
            .setFontAttrId(R.attr.fontPath)
            .build())

        if (BuildConfig.DEBUG)
            Stetho.initializeWithDefaults(this)
    }
}