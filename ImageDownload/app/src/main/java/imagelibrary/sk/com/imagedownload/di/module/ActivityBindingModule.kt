package imagelibrary.sk.com.imagedownload.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import imagelibrary.sk.com.imagedownload.di.key.ActivityScoped
import imagelibrary.sk.com.imagedownload.mvvm.MainActivity

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector
    @ActivityScoped
    abstract fun mainActivity(): MainActivity
}