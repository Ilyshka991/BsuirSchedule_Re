package com.pechuro.bsuirschedule.di.module

import android.util.Log
import com.pechuro.bsuirschedule.BuildConfig
import com.pechuro.bsuirschedule.di.annotations.AppScope
import com.pechuro.bsuirschedule.remote.api.EmployeeApi
import com.pechuro.bsuirschedule.remote.api.GroupApi
import com.pechuro.bsuirschedule.remote.api.ScheduleApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {

    companion object {
        private const val BASE_URL = "https://journal.bsuir.by/api/v1/"
        private const val CONNECT_TIMEOUT = 60L
    }

    @Provides
    @AppScope
    fun provideRetrofit(
            httpClient: OkHttpClient,
            converterFactory: Converter.Factory
    ): Retrofit =
            Retrofit.Builder()
                    .client(httpClient)
                    .addConverterFactory(converterFactory)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(BASE_URL)
                    .build()

    @Provides
    @AppScope
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
            OkHttpClient.Builder()
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    .build()

    @Provides
    @AppScope
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor(
                HttpLoggingInterceptor.Logger { Log.i("Retrofit ", it) })
        if (BuildConfig.DEBUG) {
            interceptor.level = BODY
        }
        return interceptor
    }

    @Provides
    @AppScope
    fun provideGsonConverterFactory(): Converter.Factory =
            MoshiConverterFactory.create()

    @Provides
    @AppScope
    fun provideScheduleApi(retrofit: Retrofit): ScheduleApi =
            retrofit.create(ScheduleApi::class.java)

    @Provides
    @AppScope
    fun provideEmployeeApi(retrofit: Retrofit): EmployeeApi =
            retrofit.create(EmployeeApi::class.java)

    @Provides
    @AppScope
    fun provideGroupApi(retrofit: Retrofit): GroupApi =
            retrofit.create(GroupApi::class.java)
}