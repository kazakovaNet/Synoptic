package ru.kazakovanet.synoptic.data.network.api.model

import com.google.gson.annotations.SerializedName

class AccessToken(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("expires_in")
    val expiresIn: Int,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("xoauth_yahoo_guid")
    val xOauthYahooGuid: String
)
