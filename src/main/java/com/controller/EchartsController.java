package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/echarts")
public class EchartsController {
    @RequestMapping("/toECharts")
    public String echart(){
        return "echarts/toECharts";
    }
}
