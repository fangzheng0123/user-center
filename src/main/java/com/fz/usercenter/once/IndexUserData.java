package com.fz.usercenter.once;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author fang
 * @Date 2025/2/13 17:16
 * @注释  需要读取的用户字段信息
 */
@Data
@EqualsAndHashCode
public class IndexUserData {
    @ExcelProperty("id")

    private Long id;
    /**
     * 用名字去匹配，这里需要注意，如果名字重复，会导致只有一个字段读取到数据
     */
    @ExcelProperty("nick_name")  //这里对应的是Excel表中的字段名  还有一种方式就是使用 index = 0 表示字段列从0开始
    private String username;
}
