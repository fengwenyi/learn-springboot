package com.fengwenyi.springbootelasticsearchexamplephone.jsoup;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author Erwin
 * @date 2019-03-17 02:13
 */
@Data
@Accessors(chain = true)
public class HuaWeiPhoneBean implements Serializable {

    private static final long serialVersionUID = 8336475299715913053L;

    private String productName;

    private List<ColorModeBean> colorsItemMode;

    private List<String> sellingPoints;



}
