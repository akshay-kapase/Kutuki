package com.example.kutuki.di
import android.util.Log
import com.example.kutuki.data.remote.CategoryService
import com.example.kutuki.data.remote.VideoService
import com.example.kutuki.model.*
import com.example.kutuki.utils.Constants.Companion.BASE_URL
import com.google.gson.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import org.json.JSONObject
import timber.log.Timber
import java.lang.reflect.Type
import com.google.gson.JsonElement
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    @Named("categoryConverter")
    fun provideConverterFactory(): GsonConverterFactory{
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(CategoryResponse::class.java, CategoryDeserializer())
        return GsonConverterFactory.create(gsonBuilder.create())
    }

    @Singleton
    @Provides
    fun provideScalarsConverterFactory(): ScalarsConverterFactory =
        ScalarsConverterFactory.create()

    class CategoryDeserializer : JsonDeserializer<CategoryResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): CategoryResponse {
            Timber.d("RetrofitDateSerializer $json")
            val body = JSONObject(json.toString())
            val response = body["response"].toString()
            val categoriesJson = JSONObject(response)
            val categories = categoriesJson.get("videoCategories")
            val gson = Gson()
            val categoryList = mutableListOf<CategoryModel>()
            val jsonObject = JSONObject(categories.toString())
            val keys: Iterator<String> = jsonObject.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                if (jsonObject.get(key) is JSONObject) {
                    val value: CategoryModel = gson.fromJson(jsonObject.get(key).toString(), CategoryModel::class.java)
                    categoryList.add(value)
                }
            }
            return CategoryResponse(body["code"].toString(), ResponseModel(categoryList))
        }
    }

    @Singleton
    @Provides
    @Named("categoryRetrofit")
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        @Named("categoryConverter") gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideCategoryService(@Named("categoryRetrofit") retrofit: Retrofit): CategoryService =
        retrofit.create(CategoryService::class.java)


    class VideoDeserializer : JsonDeserializer<ThumbnailResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): ThumbnailResponse {
            Timber.d("RetrofitDateSerializer $json")
            val body = JSONObject(json.toString())
            val response = body["response"].toString()
            val categoriesJson = JSONObject(response)
            val categories = categoriesJson.get("videos")
            val gson = Gson()
            val videos = mutableListOf<VideosModel>()
            val jsonObject = JSONObject(categories.toString())
            val keys: Iterator<String> = jsonObject.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                if (jsonObject.get(key) is JSONObject) {
                    val value: VideosModel = gson.fromJson(jsonObject.get(key).toString(), VideosModel::class.java)
                    videos.add(value)
                }
            }
            return ThumbnailResponse(body["code"].toString(), VideosModelResponse(videos))
        }
    }

    @Singleton
    @Provides
    @Named("videoConverter")
    fun provideVideoConverterFactory(): GsonConverterFactory{
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(ThumbnailResponse::class.java, VideoDeserializer())
        return GsonConverterFactory.create(gsonBuilder.create())
    }

    @Singleton
    @Provides
    @Named("videoRetrofit")
    fun provideVideoRetrofit(
        okHttpClient: OkHttpClient,
        @Named("videoConverter") videoConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(videoConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideVideoService(@Named("videoRetrofit") videoRetrofit: Retrofit): VideoService =
        videoRetrofit.create(VideoService::class.java)

}



