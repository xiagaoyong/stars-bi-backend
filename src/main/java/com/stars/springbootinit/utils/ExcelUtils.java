package com.stars.springbootinit.utils;


import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Prometheus
 * @description
 * @createDate 2023/8/26
 */
@Slf4j
public class ExcelUtils {
    /**
     * excel 转 csv
     * @param multipartFile
     * @return 数据
     */
    public static String excelToCsv(MultipartFile multipartFile) {
//        File file = null;
//        try {
//            file = ResourceUtils.getFile("classpath:网站数据.xlsx");
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
        List<LinkedHashMap<Integer, String>> list = null;
        try {
            list = EasyExcel.read(multipartFile.getInputStream())
                    //类型
                    .excelType(ExcelTypeEnum.XLSX)
                    //工作薄
                    .sheet()
                    //当读纸时，数一数增加的头的数目。 0 -这个表没有头，因为第一行是数据 1 -该表有一个行头，这是默认的 2 -这张表有两行头，因为第三行是数据
                    .headRowNumber(0)
                    //同步读取返回结果
                    .doReadSync();
        } catch (IOException e) {
            log.error("表格处理错误",e);
        }
        //判断是否为空
        if (CollectionUtil.isEmpty(list))
        {
            return  "";
        }
        //转化为csv
        StringBuilder stringBuilder = new StringBuilder();
        //读取表头
        LinkedHashMap<Integer, String> headerMap = list.get(0);
        stringBuilder.append(StringUtils.join(headerMap.values(),",")).append("\n");
        for (int i = 1; i < list.size(); i++) {
        //读取数据
            LinkedHashMap<Integer, String> dataMap = list.get(i);
            stringBuilder.append(StringUtils.join(dataMap.values(),",")).append("\n");
        }

        return stringBuilder.toString();

    }



}
