package com.example.chat.listener;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.util.List;

import com.example.chat.mapper.ChatElasticRepository;
import com.example.model.entity.Chat;
import com.example.model.type.ModelType;
import com.example.utils.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;

/**
 * <p>Canal Listener</p>
 * async MySQL to Elastic
 *
 * @author Chat-GPT4O
 */
@Service
@RequiredArgsConstructor
public class CanalListener {

    private final ChatElasticRepository chatElasticRepository;

    @PostConstruct
    public void init() {
        new Thread(this::startCanalListener).start();
    }

    private void startCanalListener() {
        CanalConnector connector = CanalConnectors.newSingleConnector(
                new InetSocketAddress("127.0.0.1", 11111),
                "example", "", "");

        try {
            connector.connect();
            connector.subscribe("xhub_chat.chat");

            while (true) {
                Message message = connector.getWithoutAck(100);
                long batchId = message.getId();
                List<CanalEntry.Entry> entries = message.getEntries();

                if (batchId != -1 && !entries.isEmpty()) {
                    processEntries(entries);
                }

                connector.ack(batchId);
            }
        } finally {
            connector.disconnect();
        }
    }

    private void processEntries(List<CanalEntry.Entry> entries) {
        for (CanalEntry.Entry entry : entries) {
            if (entry.getEntryType() != CanalEntry.EntryType.ROWDATA) {
                continue;
            }

            try {
                CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
                String tableName = entry.getHeader().getTableName();

                for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
                    switch (rowChange.getEventType()) {
                        case INSERT, UPDATE -> handleSave(tableName, rowData);
                        case DELETE -> handleDelete(tableName, rowData);
                        default -> {}
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleSave(String tableName, CanalEntry.RowData rowData) {
        if ("chat".equals(tableName)) {
            Chat chat = convertRowDataToChat(rowData.getAfterColumnsList());
            chatElasticRepository.save(chat);
        }
    }

    private void handleDelete(String tableName, CanalEntry.RowData rowData) {
        if ("chat".equals(tableName)) {
            Long chatId = getIdFromRowData(rowData.getBeforeColumnsList());
            chatElasticRepository.deleteById(chatId);
        }
    }

    private Chat convertRowDataToChat(List<CanalEntry.Column> columns) {
        Chat chat = new Chat();
        for (CanalEntry.Column column : columns) {
            switch (column.getName()) {
                case "id" -> chat.setId(Long.valueOf(column.getValue()));
                case "user_id" -> chat.setUserId(Long.valueOf(column.getValue()));
                case "topic" -> chat.setTopic(column.getValue());
                case "model" -> chat.setModel(ModelType.fromString(column.getValue()));
                case "starred" -> chat.setStarred(Boolean.parseBoolean(column.getValue()));
                case "content" -> chat.setContent(JsonUtil.fromJson(column.getValue(), new TypeReference<List<ChatMessage>>(){}));
                default -> {}
            }
        }
        return chat;
    }

    private Long getIdFromRowData(List<CanalEntry.Column> columns) {
        for (CanalEntry.Column column : columns) {
            if ("id".equals(column.getName())) {
                return Long.valueOf(column.getValue());
            }
        }
        return null;
    }
}
