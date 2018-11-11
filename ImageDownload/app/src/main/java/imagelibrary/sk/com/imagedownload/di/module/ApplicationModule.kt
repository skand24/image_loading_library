package imagelibrary.sk.com.imagedownload.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import imagelibrary.sk.com.imagedownload.di.network.RemoteDataManager
import imagelibrary.sk.com.imagedownload.di.network.RemoteDataSource
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class ApplicationModule {

    @Provides
    @Singleton
    internal fun bindContext(application: Application): Context = application

    @Provides
    @Singleton
    internal fun getRemoteSource(remoteDataManager: RemoteDataManager): RemoteDataSource = remoteDataManager

}