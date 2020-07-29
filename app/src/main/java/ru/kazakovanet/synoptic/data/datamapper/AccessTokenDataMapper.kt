package ru.kazakovanet.synoptic.data.datamapper

import ru.kazakovanet.synoptic.data.db.entity.AccessTokenEntity
import ru.kazakovanet.synoptic.data.network.api.dto.auth.AccessTokenDTO

/**
 * Created by NKazakova on 14.07.2020.
 */
object AccessTokenDataMapper {
    fun map(dto: AccessTokenDTO): AccessTokenEntity {
        return AccessTokenEntity(
            dto.accessToken,
            dto.tokenType,
            System.currentTimeMillis() / 1000 + dto.expiresIn,
            dto.refreshToken,
            dto.xOauthYahooGuid
        )
    }
}