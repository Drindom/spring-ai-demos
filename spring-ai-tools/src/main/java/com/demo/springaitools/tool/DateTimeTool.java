package com.demo.springaitools.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateTimeTool {
    @Tool(description = "查询指定时区的当前日期和时间")
    public String getCurrentDateTime(
            @ToolParam(description = "IANA 时区 ID，例如 Asia/Shanghai")
            String timezoneId) {
        return ZonedDateTime.now(ZoneId.of(timezoneId)).toString();
    }
}
