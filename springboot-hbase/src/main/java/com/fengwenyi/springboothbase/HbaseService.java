package com.fengwenyi.springboothbase;

/**
 * @author Erwin Feng
 * @since 2019-03-28 16:16
 */
public interface HbaseService {

    String getValue(String tableName, String rowkey, String family, String column);

}
