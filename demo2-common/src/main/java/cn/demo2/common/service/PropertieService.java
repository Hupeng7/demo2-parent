package cn.demo2.common.service;

import cn.demo2.common.spring.exetend.PropertyConfig;
import org.springframework.stereotype.Service;

@Service
public class PropertieService {

    @PropertyConfig
    public String REPOSITORY_PATH;
    
    @PropertyConfig
    public String IMAGE_BASE_URL;

}
