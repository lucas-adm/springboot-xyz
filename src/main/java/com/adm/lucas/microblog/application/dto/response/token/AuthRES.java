package com.adm.lucas.microblog.application.dto.response.token;

import com.adm.lucas.microblog.application.dto.response.user.DetailUserRES;
import com.adm.lucas.microblog.domain.model.Token;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

public record AuthRES(
        UUID refresh_token,
        String access_token,
        String created_at,
        String expires_at,
        DetailUserRES user
) {
    public AuthRES(Token token) {
        this(
                token.getId(),
                token.getAccessToken(),
                token.getCreatedAt().atZone(ZoneId.of("America/Sao_Paulo")).format(DateTimeFormatter.ofPattern("d/MM/yy HH:mm", Locale.of("pt-BR"))),
                token.getExpiresAt().atZone(ZoneId.of("America/Sao_Paulo")).format(DateTimeFormatter.ofPattern("d/MM/yy HH:mm", Locale.of("pt-BR"))),
                new DetailUserRES(
                        token.getUser().getId(),
                        token.getUser().getEmail(),
                        token.getUser().getUsername(),
                        token.getUser().getDisplayName(),
                        token.getUser().getAvatar(),
                        token.getUser().getHost(),
                        token.getUser().isProfilePrivate(),
                        token.getUser().isSponsor(),
                        token.getUser().getScore()
                )
        );
    }
}