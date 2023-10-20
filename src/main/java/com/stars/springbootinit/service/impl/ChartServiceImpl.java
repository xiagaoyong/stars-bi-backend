package com.stars.springbootinit.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stars.springbootinit.model.entity.Chart;
import com.stars.springbootinit.service.ChartService;
import com.stars.springbootinit.mapper.ChartMapper;
import org.springframework.stereotype.Service;

/**
* @author Prometheus
* @description 针对表【chart(图表信息表)】的数据库操作Service实现
* @createDate 2023-08-19 15:03:35
*/
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
implements ChartService{

}
