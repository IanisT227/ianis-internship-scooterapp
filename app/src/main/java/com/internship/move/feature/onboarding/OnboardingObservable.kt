package com.internship.move.feature.onboarding

import androidx.lifecycle.Observer

class OnboardingObservable<T> {
    private val observerList: MutableList<Observer<T>> = mutableListOf()
    private var lastValue: T? = null

    fun addObserver(observer: Observer<T>) {
        observerList.add(observer)
        // replay the last value to the new observer
        lastValue?.let { observer.onChanged(it) }
    }

    fun removeObserver(observer: Observer<T>) {
        observerList.remove(observer)
    }

    fun onChanged(t: T) {
        notifyObservers(t)
    }

    private fun notifyObservers(t: T) {
        for (observer in observerList) {
            observer.onChanged(t)
        }
    }
}