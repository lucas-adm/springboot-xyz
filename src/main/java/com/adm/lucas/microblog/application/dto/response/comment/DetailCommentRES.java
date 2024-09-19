package com.adm.lucas.microblog.application.dto.response.comment;

import com.adm.lucas.microblog.application.dto.response.user.DetailUserRES;
import com.adm.lucas.microblog.domain.comment.Comment;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

public record DetailCommentRES(
        UUID id,
        String created_at,
        String text,
        boolean modified,
        DetailUserRES user,
        int answers
) {
    public DetailCommentRES(Comment comment) {
        this(
                comment.getId(),
                comment.getCreatedAt().atZone(ZoneId.of("America/Sao_Paulo")).format(DateTimeFormatter.ofPattern("d/M/yy HH:mm", Locale.of("pt-BR"))),
                comment.getText(),
                comment.isModified(),
                new DetailUserRES(comment.getUser()),
                comment.getAnswers().size()
        );
    }
}