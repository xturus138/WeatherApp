import com.example.weatherapp.source.response.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("forecast.json")
    fun getWeather(
        @Query("key") apiKey: String,
        @Query("q") location: String,
        @Query("days") days: Int
    ): Call<WeatherResponse>
}
