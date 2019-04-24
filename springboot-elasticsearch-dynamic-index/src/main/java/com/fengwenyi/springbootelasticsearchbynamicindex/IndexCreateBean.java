package com.fengwenyi.springbootelasticsearchbynamicindex;

import com.fengwenyi.javalib.util.DateTimeUtil;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Erwin Feng
 * @since 2019-04-24 11:09
 */
@Data
@Component("indexCreateBean")
public class IndexCreateBean {

    public String index = DateTimeUtil.toString(new Date(), "yyyy-MM-dd");

}
