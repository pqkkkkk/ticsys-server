package com.example.ticsys.event.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.example.ticsys.event.dto.TimelyEventDataDto;

public class TimelyEventDataRowMapper implements RowMapper<TimelyEventDataDto> {

    @Override
    @Nullable
    public TimelyEventDataDto mapRow(@NonNull ResultSet arg0, int arg1) throws SQLException {
        TimelyEventDataDto result = TimelyEventDataDto.builder()
                .label(arg0.getString("label"))
                .value(arg0.getInt("value"))
                .build();

        return result;
    }

}
