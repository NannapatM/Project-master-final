package co.project.client.injection.component

import javax.inject.Singleton

import dagger.Component
import co.project.client.injection.module.ApplicationTestModule

@Singleton
@Component(modules = arrayOf(ApplicationTestModule::class))
interface TestComponent : ApplicationComponent
