package com.example.hindilearn
import androidx.navigation3.runtime.NavBackStack
fun reflectNavBackStack() {
    val methods = NavBackStack::class.java.methods
    methods.forEach { println("METHOD: " + it.name) }
}
