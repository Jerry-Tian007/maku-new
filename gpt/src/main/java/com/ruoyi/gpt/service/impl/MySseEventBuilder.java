package com.ruoyi.gpt.service.impl;

import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class MySseEventBuilder implements SseEmitter.SseEventBuilder {
    private final Set<ResponseBodyEmitter.DataWithMediaType> dataToSend;

    private static final MediaType TEXT_PLAIN = new MediaType("text", "plain", StandardCharsets.UTF_8);
    @Nullable
    private StringBuilder sb;

    MySseEventBuilder() {
        this.dataToSend = new LinkedHashSet(4);
    }

    public MySseEventBuilder sessionId(String sessionId) {
        this.append("sessionId:").append(sessionId).append('\n');
        return this;
    }

    @Override
    public MySseEventBuilder id(String id) {
        this.append("id:").append(id).append('\n');
        return this;
    }

    @Override
    public MySseEventBuilder name(String eventName) {
        return null;
    }

    public MySseEventBuilder event(String event) {
        this.append("event:").append(event).append('\n');
        return this;
    }

    public MySseEventBuilder data(String data) {
        this.append("data:").append(data).append('\n');
        return this;
    }

    public SseEmitter.SseEventBuilder reconnectTime(long reconnectTimeMillis) {
        this.append("retry:").append(String.valueOf(reconnectTimeMillis)).append('\n');
        return this;
    }

    public SseEmitter.SseEventBuilder comment(String comment) {
        this.append(':').append(comment).append('\n');
        return this;
    }

    public SseEmitter.SseEventBuilder data(Object object) {
        return this.data(object, (MediaType)null);
    }

    public SseEmitter.SseEventBuilder data(Object object, @Nullable MediaType mediaType) {
        this.append("data:");
        this.saveAppendedText();
        this.dataToSend.add(new ResponseBodyEmitter.DataWithMediaType(object, mediaType));
        this.append('\n');
        return this;
    }

    MySseEventBuilder append(String text) {
        if (this.sb == null) {
            this.sb = new StringBuilder();
        }

        this.sb.append(text);
        return this;
    }

    MySseEventBuilder append(char ch) {
        if (this.sb == null) {
            this.sb = new StringBuilder();
        }

        this.sb.append(ch);
        return this;
    }

    public Set<ResponseBodyEmitter.DataWithMediaType> build() {
        if (!StringUtils.hasLength(this.sb) && this.dataToSend.isEmpty()) {
            return Collections.emptySet();
        } else {
            this.append('\n');
            this.saveAppendedText();
            return this.dataToSend;
        }
    }

    private void saveAppendedText() {
        if (this.sb != null) {
            this.dataToSend.add(new ResponseBodyEmitter.DataWithMediaType(this.sb.toString(), TEXT_PLAIN));
            this.sb = null;
        }

    }
}
