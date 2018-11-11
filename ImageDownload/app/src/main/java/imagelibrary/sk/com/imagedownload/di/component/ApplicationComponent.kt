package imagelibrary.sk.com.imagedownload.di.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import imagelibrary.sk.com.imagedownload.EntryPoint
import imagelibrary.sk.com.imagedownload.di.module.ActivityBindingModule
import imagelibrary.sk.com.imagedownload.di.module.ApplicationModule
import javax.inject.Singleton

@Singleton
@Component(modules = [(ApplicationModule::class),
    (ActivityBindingModule::class),
    (AndroidSupportInjectionModule::class)])

interface ApplicationComponent : AndroidInjector<EntryPoint> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: Application): Builder

        fun build(): ApplicationComponent
    }

    override fun inject(instance: EntryPoint)
}