package com.example.licenta_mobile.util

import androidx.databinding.Observable


@Suppress("UNCHECKED_CAST")
inline  fun <T: Observable> T.addOnPropertyChanged(crossinline callback: (T) -> Unit) =
            addOnPropertyChangedCallback(
                    object: Observable.OnPropertyChangedCallback() {
                        override fun onPropertyChanged(
                                observable: Observable?, i: Int) =
                                callback(observable as T)
                    })