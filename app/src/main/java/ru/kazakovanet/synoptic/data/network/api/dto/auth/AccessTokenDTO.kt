package ru.kazakovanet.synoptic.data.network.api.dto.auth

import com.google.gson.annotations.SerializedName

class AccessTokenDTO(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("expires_in")
    val expiresIn: Int,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("xoauth_yahoo_guid")
    val xOauthYahooGuid: String?
)
