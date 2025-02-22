package com.fz.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author fang
 * @Date 2025/2/21 19:09
 * @注释
 */
@Data
public class PageRequest implements Serializable {
    private static final long serialVersionUID = -3472070870836133413L;
    /**
     * 页面大小
     */
    protected int pageSize = 10;
    /**
     * 当前第几页
     */
    protected int pageNum = 1;

}
