package co.project.client.injection.module

import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.AppCompatTextView
import android.util.Log
import butterknife.BindView
import butterknife.OnClick
import co.project.client.R

import co.project.client.data.remote.ServerService
import co.project.client.ui.main.MainActivity
import co.project.client.ui.main.MainMvp
import co.project.client.ui.main.MainPresenter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.haha.perflib.Main
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton
import retrofit2.http.Url
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers


@Module
class ApiModule {

    @GET

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient()
    }

    // SERVER IP

    @Provides
  //  @Headers("Connection:close")

    fun provideServerService(okHttpClient: OkHttpClient, gson: Gson): ServerService {



        return Retrofit.Builder()
                .client(okHttpClient)
     //         .baseUrl("http://172.20.10.6:4444/") //172.20.10.5 , 4444  http(s)://[ip]:[port]/
        //      .baseUrl("https://wifi-locator-mock.herokuapp.com/") //172.20.10.2 , 8080
                .baseUrl("http://172.20.10.9:4444/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(ServerService::class.java)


    }
}
