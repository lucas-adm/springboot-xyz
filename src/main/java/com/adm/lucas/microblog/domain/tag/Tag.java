package com.adm.lucas.microblog.domain.tag;

import com.adm.lucas.microblog.domain.note.Note;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tags")
@NoArgsConstructor
@Data
@JsonIgnoreProperties({"id", "notes"})
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToMany(mappedBy = "tags")
    private List<Note> notes = new ArrayList<>();

    private String name;

    public Tag(String name) {
        this.name = name;
    }

}