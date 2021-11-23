package com.revature.service;

import com.revature.model.User;
import com.revature.persistence.GenericDao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Method;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.*;

public class UserService {
    private User currentUser;
    private final ObjectMapper mapper;
    private final GenericDao genericDao;

    public UserService(){
        this.genericDao = new GenericDao();
        this.mapper = new ObjectMapper();
    }

    public void createUser(HttpServletRequest req, HttpServletResponse resp){
        // get the user object from the body.
        try {
            User user = null;
            user = mapper.readValue(req.getReader().lines().collect(Collectors.joining()), User.class);

            // persist the object and return it in JSON form
            resp.getWriter().println(mapper.writeValueAsString(genericDao.createRow(user)));
            resp.setHeader("Content-Type", "application/json");
            resp.setStatus(201);

        } catch (IOException e) {
            resp.setStatus(400);
            try {
                resp.getWriter().println(e.getMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public List<User> ReadAllUsers(){
        List<User> AllUsers = genericDao.Read(User.class);
        return AllUsers;
    }

    public User ReadByPKey(List<Integer> ids){
        User user = (User) genericDao.ReadByPKey(User.class,ids);
        return user;
    }

    public void UpdateUser(User user){
        genericDao.update(user);
    }

    public void DeleteUser(List<Integer> ids){
        genericDao.delete(User.class, ids);
    }
}
