package com.open.web.bean.to;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description
 * @auther 程佳伟
 * @create 2019-10-17 21:29
 */
@Setter
@Getter
@ToString
public class RegisterUser {
    private String username;
    private String password;
    private String rePassword;
    private Integer age;
    private String gender;
}
