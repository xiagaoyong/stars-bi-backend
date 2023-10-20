package com.stars.springbootinit.model.dto.chart;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Prometheus
 * @description
 * @createDate 2023/8/26
 */
@Data
public class GenChartByAiRequest implements Serializable {
    /**
     * 名称
     */
    private String name;

    /**
     * 分析目标
     */
    private String goal;
    /**
     * 图表类型
     */

    private String chartType;








}
