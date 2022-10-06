package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.service.UserService;
import jdk.nashorn.internal.ir.CallNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        User user = new User();
        log.info(session.toString());
        String phone = map.get("phone").toString();

        //String code = map.get("code").toString();

        String codeInSession = (String) session.getAttribute(phone);


        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone,phone);
        int count = userService.count(queryWrapper);

        if(count==0){
            user.setPhone(phone);
            userService.save(user);
        }else {
            user = userService.getOne(queryWrapper);
        }

        session.setAttribute("user",user.getId());
        System.out.println(user.getId()+"================");


        return R.success(user);
    }

}
