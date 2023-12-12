package com.de.rentalfinal.models

import android.location.Address
import java.util.UUID

data class Property
    (
            var id:String = UUID.randomUUID().toString(),
            var address: String,
            var type: String,
            var description: String,
            var owner: String,
            var available:Boolean

            )