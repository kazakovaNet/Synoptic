package ru.kazakovanet.synoptic.internal

import java.io.IOException

/**
 * Created by NKazakova on 27.06.2020.
 */

class NoConnectivityException : IOException()

class LocationPermissionNotGrantedException : Exception()

class DateNotFoundException : Exception()

class AuthCodeNotFoundException : Exception()

class AccessTokenNotFoundException : Exception()