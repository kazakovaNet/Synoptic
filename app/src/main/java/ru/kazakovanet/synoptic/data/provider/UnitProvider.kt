package ru.kazakovanet.synoptic.data.provider

import ru.kazakovanet.synoptic.internal.UnitSystem

/**
 * Created by NKazakova on 01.07.2020.
 */
interface UnitProvider {
    fun getUnitSystem(): UnitSystem
}