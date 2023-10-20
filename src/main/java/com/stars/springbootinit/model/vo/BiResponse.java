package com.stars.springbootinit.model.vo;

import lombok.Data;

/**
 * @author Prometheus
 * @description bi 的返回结果
 * @createDate 2023/8/31
 */
@Data
public class BiResponse {

    private Long chartId;
    private String genChart;
    private String genResult;
}
