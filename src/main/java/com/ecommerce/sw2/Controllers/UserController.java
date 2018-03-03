package com.ecommerce.sw2.Controllers;

/**
 * Created by Mina_Yousry on 03/03/2018.
 */

import com.ecommerce.*;
import com.ecommerce.sw2.Models.User;
import com.ecommerce.sw2.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.attribute.UserDefinedFileAttributeView;


@Controller
@RequestMapping("/system")
public class UserController {

    @Autowired
    private UserRepo userRepository;

    @RequestMapping("")
    public String index(){
        return "index";
    }

    @RequestMapping("/register")
    public @ResponseBody String addNewUser (@RequestParam("Name") String name
            , @RequestParam("Email") String email , @RequestParam("Username") String username , @RequestParam("password") String password  ) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        User n = new User(name , email , username , password);
        userRepository.save(n);
        return "Saved";
    }

    @RequestMapping("/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }
}
