package com.fengwenyi.springbootelasticsearchexamplephone.jsoup;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Erwin
 * @date 2019-03-17 02:16
 */
@Data
@Accessors(chain = true)
public class ColorModeBean implements Serializable {

    private static final long serialVersionUID = -8082160955256532791L;
    private String colorName;

}
