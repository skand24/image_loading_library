package imagelibrary.sk.com.imagedownload.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import imagelibrary.sk.com.imagedownload.di.key.ViewModelKey
import imagelibrary.sk.com.imagedownload.mvvm.MainViewmodel

@Module(includes = [FragmentBindingModule::class])
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewmodel::class)
    abstract fun bindLoginViewModel(mainViewmodel: MainViewmodel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory
}