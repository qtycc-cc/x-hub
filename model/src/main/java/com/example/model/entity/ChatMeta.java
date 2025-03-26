package com.example.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.example.model.type.ModelType;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ChatMeta {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Id
    private Long id;
    private Long userId;
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String topic;
    private ModelType model;
    private boolean starred;
}
