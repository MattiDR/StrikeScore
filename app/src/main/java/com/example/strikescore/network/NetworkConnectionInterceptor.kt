package com.example.strikescore.network
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException

/**
 * Interceptor for handling network connectivity checks and adding a custom header to the
 * outgoing requests. This class is designed to be used with OkHttp as an interceptor.
 *
 * @param context The [Context] used to access system services and check network connectivity.
 */
class NetworkConnectionInterceptor(val context: Context) : Interceptor {
    /**
     * Intercepts the request to check for network connectivity. If there is no connection,
     * an [IOException] is thrown. Otherwise, a custom header ("X-Auth-Token") is added to
     * the outgoing request.
     *
     * @param chain The [Interceptor.Chain] representing the request chain.
     * @return The intercepted [Response] after processing.
     * @throws IOException If there is no network connection.
     */
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        if(!isConnected(context=context)){
            Log.i("retrofit", "there is no connection")
            throw IOException()

        }
        else {
            val builder = chain.request().newBuilder().addHeader("X-Auth-Token", "560af3c655694c00a6d973c6012abc31")
            return@run chain.proceed(builder.build())
        }

    }

    /**
     * Checks if the device is currently connected to a network.
     *
     * @param context The [Context] used to access system services and check network connectivity.
     * @return `true` if the device is connected to a network, `false` otherwise.
     */
    fun isConnected(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }
}