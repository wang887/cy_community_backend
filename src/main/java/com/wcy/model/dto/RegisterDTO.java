package com.wcy.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;


@Data
public class RegisterDTO {

    /**
     * 对DTO的理解
     * 客户端发发过来的字段，实体类的字段是与数据库一致的，DTO是与客户端一致，
     * 客户端不用传所有字段给服务端，因此我们再view里定义一个DTO方便实现
     */
    @NotEmpty(message = "请输入账号")
    @Length(min = 2, max = 15, message = "长度在2-15")
    private String name;

    @NotEmpty(message = "请输入密码")
    @Length(min = 6, max = 20, message = "长度在6-20")
    private String pass;

    @NotEmpty(message = "请再次输入密码")
    @Length(min = 6, max = 20, message = "长度在6-20")
    private String checkPass;

    @NotEmpty(message = "请输入电子邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;
}
