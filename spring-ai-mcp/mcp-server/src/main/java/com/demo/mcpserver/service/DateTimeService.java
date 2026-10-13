package com.demo.mcpserver.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DateTimeService {
    @Tool(
            name = "get_current_time",
            description = "根据 IANA 时区 ID 查询该时区当前的日期和时间")
    public String getCurrentTime(
            @ToolParam(description = "IANA 时区 ID，例如 Asia/Shanghai、Europe/London、America/New_York")
            String zoneId) {
        try {
            return ZonedDateTime.now(ZoneId.of(zoneId))
                    .format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
        }
        catch (DateTimeException e) {
            throw new IllegalArgumentException(
                    "无效的时区 ID" + zoneId,
                    e
            );
        }
    }
}
