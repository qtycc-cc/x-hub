package com.example.chat.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.example.utils.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;

public class ContentTypeHandler extends BaseTypeHandler<List<ChatMessage>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<ChatMessage> parameter, JdbcType jdbcType)
            throws SQLException {
        String content = JsonUtil.toJson(parameter);
        ps.setString(i, content);
    }

    @Override
    public List<ChatMessage> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String content = rs.getString(columnName);
        List<ChatMessage> messages = JsonUtil.fromJson(content, new TypeReference<>() {
        });
        return messages;
    }

    @Override
    public List<ChatMessage> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String content = rs.getString(columnIndex);
        List<ChatMessage> messages = JsonUtil.fromJson(content, new TypeReference<>() {
        });
        return messages;
    }

    @Override
    public List<ChatMessage> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String content = cs.getString(columnIndex);
        List<ChatMessage> messages = JsonUtil.fromJson(content, new TypeReference<>() {
        });
        return messages;
    }
}
