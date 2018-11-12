package example.com.nm

import android.app.Application
import com.facebook.stetho.Stetho
import example.com.nm.feature.di.appModule
import example.com.nm.feature.di.prefModule
import example.com.nm.feature.di.remoteDatasourceModule
import org.koin.standalone.StandAloneContext.startKoin
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

class NmApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(listOf(appModule, prefModule, remoteDatasourceModule))

        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
            .setDefaultFontPath("fonts/Gibson.ttf")
            .setFontAttrId(R.attr.fontPath)
            .build())

        if (BuildConfig.DEBUG)
            Stetho.initializeWithDefaults(this)
    }
}