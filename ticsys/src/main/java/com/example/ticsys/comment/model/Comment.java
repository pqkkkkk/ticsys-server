package com.example.ticsys.comment.model;

import java.sql.Time;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Comment {
    int id;
    String content;
    String senderId;
    int eventId;
    int parentId;
    LocalDate dateCreatedAt;
    Time timeCreatedAt;
    int childCount;
}
