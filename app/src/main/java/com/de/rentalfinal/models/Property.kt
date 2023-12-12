package com.de.rentalfinal.models

import java.util.*

data class Property
    (
            var id:String = UUID.randomUUID().toString(),
            var address: String = "",
            var type: String = "",
            var description: String = "",
            var owner: String = "",
            var available:Boolean = false

            )