package com.example.chat.mapper;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.example.model.entity.Chat;

@Repository
public interface ChatElasticRepository extends ElasticsearchRepository<Chat, Long> {
    /**
     * <p>find by Keyword</p>
     * match one of topic or content or reasoningContent can get the result
     * @param keyword
     * @return matched chats {@link Chat}
     */
    @Query("{\"bool\": {\"should\": [" +
            "{\"match\": {\"topic\": \"?0\"}}," +
            "{\"nested\": {\"path\": \"content\"," +
            "\"query\": {\"bool\": {\"should\": [" +
                "{\"match\": {\"content.content\": \"?0\"}}," +
                "{\"match\": {\"content.reasoningContent\": \"?0\"}}" +
            "], \"minimum_should_match\": 1}}}}]," +
            "\"minimum_should_match\": 1}}")
    List<Chat> findByKeyword(String keyword);
}
