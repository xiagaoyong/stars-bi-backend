package com.stars.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stars.springbootinit.model.entity.Chart;
import com.sun.corba.se.spi.ior.ObjectKey;
import io.lettuce.core.dynamic.annotation.Key;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
* @author Prometheus
* @description 针对表【chart(图表信息表)】的数据库操作Mapper
* @createDate 2023-08-19 15:03:35
* @Entity com.stars.springbootinit.model.entity.Chart
*/
public interface ChartMapper extends BaseMapper<Chart> {
    /**
     *  查询数据库
     * @param querySql 查询语句
     * @return
     */
    @MapKey("")
    List<Map<String, Object>> queryChartData(@Param("querySql") String querySql);
}
