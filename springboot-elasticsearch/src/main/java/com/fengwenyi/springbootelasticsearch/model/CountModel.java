package com.fengwenyi.springbootelasticsearch.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * @author Wenyi Feng
 * @since 2019-03-12
 */
@Data
@Accessors(chain = true)
@Document(indexName = "count", type = "CountModel.class")
public class CountModel implements Serializable {

    private static final long serialVersionUID = -7155794846740723450L;

    @Id
    private String id;

    @Field(type = FieldType.Long)
    @JsonSerialize(using= ToStringSerializer.class)
    private Long count;
}
