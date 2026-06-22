package com.onlyburger.app.data.remote

/**
 * Network configuration.
 *
 * BASE_URL uses 10.0.2.2, which is how the Android emulator reaches the host machine's
 * localhost. If you run on a physical device, change this to your PC's LAN IP (for
 * example "http://192.168.1.20:5169/") and add that IP to network_security_config.xml.
 */
object ApiConfig {
    const val BASE_URL = "http://192.168.0.26:5169/"
}
