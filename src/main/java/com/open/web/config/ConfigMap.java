package com.open.web.config;

import com.open.web.utils.PropertiesUtil;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @auther 程佳伟
 * @create 2019-10-15 19:56
 */
@Component
@Log4j2
public class ConfigMap {

    @Getter
    private static Map<String,String> map = new HashMap<>();
    @Getter
    private static List<String> white = new ArrayList<>();
    @Getter
    private static List<String> notEnctryptUser = new ArrayList<>();

    @PostConstruct
    public static void loadConfig(){
        map = PropertiesUtil.getValues("config.properties");
        log.info("[|||||||] - Loading config map :{}",map);
        if(null != map.get("white.list")) {
            String[] split = map.get("white.list").split(",");
            for(String s : split){
                white.add(s);
            }
            log.info("[|||||||] - Loading white list :{}",white);
        }

        if(null != map.get("not.encryptPassword.user")) {
            String[] split = map.get("not.encryptPassword.user").split(",");
            for(String s : split){
                notEnctryptUser.add(s);
            }
            log.info("[|||||||] - Loading not.encryptPassword.user list :{}",notEnctryptUser);
        }
    }
}
