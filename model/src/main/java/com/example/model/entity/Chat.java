package com.example.model.entity;

import java.util.List;

import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(indexName = "chat")
public class Chat extends ChatMeta {
    @Field(type = FieldType.Nested)
    private List<ChatMessage> content;
}
