package ru.admin.config;

import io.r2dbc.proxy.core.QueryExecutionInfo;
import io.r2dbc.proxy.listener.ProxyExecutionListener;
import io.r2dbc.proxy.support.QueryExecutionInfoFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

@SuppressWarnings("unused")
@Slf4j
public class SqlLogger implements ProxyExecutionListener {
    private final QueryExecutionInfoFormatter formatter = new QueryExecutionInfoFormatter().newLine()
            .showQuery()
            .newLine()
            .showBindings()
            .newLine()
            .showTime();
    @Override
    public void afterQuery(@Nullable QueryExecutionInfo execInfo) {
        if (execInfo == null)
            return;
        log.debug(formatter.format(execInfo));
    }
}
