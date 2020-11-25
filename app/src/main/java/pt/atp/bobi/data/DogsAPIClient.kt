package pt.atp.bobi.data

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pt.atp.bobi.data.cb.DataRetrieved
import pt.atp.bobi.data.model.Breed
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

private const val TAG = "DogApi"

object DogsAPIClient {

    private val apiDog by lazy {
        setup()
    }

    fun getListOfBreeds(listener: DataRetrieved? = null) {
        apiDog.getBreedsList().enqueue(object : Callback<List<Breed>> {

            override fun onResponse(call: retrofit2.Call<List<Breed>>, response: Response<List<Breed>>) {
                if (!response.isSuccessful) {
                    Log.e(TAG, "code: " + response.code())
                    return
                }

                listener?.onDataFetchedSuccess(response.body()!!)
            }

            override fun onFailure(call: retrofit2.Call<List<Breed>>, t: Throwable) {
                Log.e(TAG, "Unable to get dogs breed. Error: ${t.message}")
                listener?.onDataFetchedFailed()
            }

        })
    }

    private fun setup(): DogsAPI {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        return retrofit.create()
    }

}