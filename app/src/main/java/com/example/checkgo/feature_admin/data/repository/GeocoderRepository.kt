package com.example.checkgo.feature_admin.data.repository

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.resume

class GeocoderRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val geocoder by lazy { Geocoder(context, Locale.getDefault()) }

    suspend fun getAddress(lat: Double, lng: Double): String? {
        if (!Geocoder.isPresent()) return null

        return try {
            val address: Address? =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    suspendCancellableCoroutine<Address?> { cont ->
                        geocoder.getFromLocation(lat, lng, 1, object : Geocoder.GeocodeListener {
                            override fun onGeocode(addresses: MutableList<Address>) {
                                cont.resume(addresses.firstOrNull())
                            }
                            override fun onError(errorMessage: String?) {
                                cont.resume(null)
                            }
                        })
                    }
                } else {
                    withContext(Dispatchers.IO) {
                        @Suppress("DEPRECATION")
                        geocoder.getFromLocation(lat, lng, 1)?.firstOrNull()
                    }
                }

            address?.getAddressLine(0)
        } catch (e: IOException) {
            null // sin red o servicio no disponible
        }
    }
}