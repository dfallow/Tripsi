package com.example.tripsi.utils.stepCounter

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SensorModule {

    @Provides
    @Singleton
    fun provideStepSensor(app: Application): MeasurableSensor {
        return StepCounterSensor(app)
    }
}