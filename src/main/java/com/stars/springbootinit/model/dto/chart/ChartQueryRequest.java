package com.stars.springbootinit.model.dto.chart;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.stars.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.bind.annotation.RequestPart;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 查询请求
 *
 * 
 * 
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChartQueryRequest extends PageRequest implements Serializable {

    private Long id;

    /**
     * 分析目标
     */
    private String goal;

    /**
     * 名称
     */
    private String name;

    /**
     * 图表类型
     */
    private String chartType;
    /**
    用户 id
     */
    private Long userId;


    private static final long serialVersionUID = 1L;
}