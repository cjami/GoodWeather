package che.codes.goodweather.integrationtests

import android.app.Application
import che.codes.goodweather.core.di.AppContextModule
import che.codes.goodweather.core.di.CoreComponent
import che.codes.goodweather.core.di.DaggerCoreComponent

class TestApplication: Application(), CoreComponent.Provider {

    private var coreComponent: CoreComponent? = null

    override fun setCoreComponent(coreComponent: CoreComponent) {
        this.coreComponent = coreComponent
    }

    override fun provideCoreComponent(): CoreComponent {
        if (coreComponent == null) {
            coreComponent = DaggerCoreComponent.builder().appContextModule(AppContextModule(this)).build()
        }
        return coreComponent!!
    }
}