package com.ecommerce.sw2.Controllers;

import com.ecommerce.sw2.Models.Domain.*;
//import com.ecommerce.sw2.Models.Domain.StoreOwner;
import com.ecommerce.sw2.Models.Repository.StatisticsRepository;
import com.ecommerce.sw2.Models.Repository.UserRepository;
import com.ecommerce.sw2.Models.Services.StatisticsService;
import com.ecommerce.sw2.Models.Services.StoreService;
import com.ecommerce.sw2.Models.Services.UserService;
import com.ecommerce.sw2.Validators.StoreFormValidator;
import com.ecommerce.sw2.forms.RegisterForm;
import com.ecommerce.sw2.forms.StatisticsForm;
import com.ecommerce.sw2.forms.StoreForm;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Path;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@RestController
public class StoreController {

    @Autowired
    private StoreService storeService;

    @Autowired
    private UserService userService;

    @Autowired
    private StatisticsRepository statisticsRepository;

    @Autowired
    private StoreFormValidator storeFormValidator;

    @Autowired
    private StatisticsService statisticsService;

    @InitBinder("storeForm")
    public void StoreFormInitBinder(WebDataBinder binder) {
        binder.addValidators(storeFormValidator);
    }

    @RequestMapping(value = "/addstore" , method = RequestMethod.POST)
    public ResponseEntity<?> addstore(@Valid @RequestBody StoreForm storeForm , BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());


        Store store = storeService.createStore(storeForm);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", store.getId());
        jsonObject.put("name", store.getName());
        return ResponseEntity.ok().body(jsonObject);
    }

    @RequestMapping(value = "/approvestore",method = RequestMethod.POST)
    public ResponseEntity<?> approvestore(@RequestBody StoreForm storeForm)
    {
//        Long i = Long.getLong(id);
        Store s = storeService.acceptStore(storeForm.getName());
        if (s == null)
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name","null");
            return ResponseEntity.ok().body(jsonObject);
        }
        return ResponseEntity.ok().body(s);
    }

    @RequestMapping(value = "/getstores", method = RequestMethod.POST)
    public Collection<Store> getStoresStoreOwner(@RequestBody RegisterForm RegisterForm)
    {
        return storeService.getStoresforStoreOwner(RegisterForm);
    }

    @RequestMapping(value = "/getappstores", method = RequestMethod.POST)
    public Collection<Store> getStoresAdmin(@RequestBody RegisterForm RegisterForm)
    {
        return storeService.getStoresforAdmin(RegisterForm);
    }

    @RequestMapping(value = "/addcollaborator/{username}/{storename}" , method = RequestMethod.GET)
    public ResponseEntity<?> addCollaborator(@PathVariable String username , @PathVariable String storename)
    {
        User user = storeService.addcollab(username , storename);
        if(user == null)
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name","null");
            return ResponseEntity.ok().body(jsonObject);
        }
        return ResponseEntity.ok().body(user);
    }

    @RequestMapping(value = "/viewcollaborators/{storename}" , method = RequestMethod.GET)
    public Collection<User> getCollaboratores(@PathVariable String storename)
    {
        Collection<User> us = storeService.viewcollab(storename);
        Collection<User> users = new ArrayList<>();
        for(User user : us)
        {
            User form = new User();
            form.setName(user.getName());
            form.setEmail(user.getEmail());
            form.setUsername(user.getUsername());
            form.setPasswordHash(user.getPasswordHash());
            users.add(form);
        }
        return  users;
    }

    @RequestMapping(value = "/viewactions/{storename}" , method = RequestMethod.GET)
    public Collection<ActionHistory> getActions(@PathVariable String storename)
    {
        return storeService.viewactions(storename);
    }

    @RequestMapping(value = "/addStatisticeToStore/{storename}" , method = RequestMethod.POST)
    public ResponseEntity<?> addStatisticToStore(@PathVariable String storename , @RequestBody StatisticsForm statisticsForm)
    {
        Store store = storeService.AddStatToStore(statisticsForm , storename);
        if(store == null)
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name","null");
            return ResponseEntity.ok().body(jsonObject);
        }
        return ResponseEntity.ok().body(store);
    }

    @RequestMapping(value = "/getStoreStats/{storename}" , method = RequestMethod.GET)
    public ResponseEntity<?> GetStoreStats(@PathVariable String storename)
    {
        Collection<Statistics> statistics  = storeService.GetStoreStats(storename);
        if(statistics == null)
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name","null");
            return ResponseEntity.ok().body(jsonObject);
        }
        return ResponseEntity.ok().body(statistics);
    }

    @RequestMapping(value = "/GetValueStat/{storename}" , method = RequestMethod.POST)
    public ResponseEntity<?> statapply(@PathVariable String storename , @RequestBody StatisticsForm statisticsForm)
    {
        return storeService.ApplyStatForProduct(storename , statisticsForm);
    }

    @RequestMapping(value = "/getAllStats" , method = RequestMethod.GET)
    public ResponseEntity<?> all()
    {
        Collection<Statistics> statistics = statisticsService.GetAllStats();
        if(statistics == null)
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name","null");
            return ResponseEntity.ok().body(jsonObject);
        }
        return ResponseEntity.ok().body(statistics);
    }
}