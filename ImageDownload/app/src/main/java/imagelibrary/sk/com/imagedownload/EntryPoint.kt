package imagelibrary.sk.com.imagedownload

import android.app.Activity
import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import imagelibrary.sk.com.imagedownload.di.component.DaggerApplicationComponent
import javax.inject.Inject

class EntryPoint : Application(), HasActivityInjector {

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        DaggerApplicationComponent.builder().application(this).build().inject(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)

    }
}