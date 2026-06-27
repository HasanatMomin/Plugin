package com.hasanatmomin.mcpvp.storage;

import java.sql.Connection;

public interface Database {
    void initialize();
    Connection getConnection();
    void close();
}
