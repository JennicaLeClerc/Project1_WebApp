package com.revature.service;

import com.revature.model.User;
import com.revature.persistence.GenericDao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Arrays;
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

    // CREATE
    public void createUser(HttpServletRequest req, HttpServletResponse resp){
        // get the user object from the body.
        try {
            User user = mapper.readValue(req.getReader().lines().collect(Collectors.joining()), User.class);

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

    // READ
    public void readAllUsers(HttpServletRequest req, HttpServletResponse resp){
        try {
            resp.getWriter().print(mapper.writeValueAsString(genericDao.read(User.class)));
            resp.setHeader("Content-Type", "application/json");
            resp.setStatus(200); // ok
        } catch (JsonProcessingException e) {
            resp.setStatus(500);
            try {
                resp.getWriter().println(e.getMessage());
            } catch (IOException ioException) {
                resp.setStatus(500);
                ioException.printStackTrace();
            }
            e.printStackTrace();
        } catch (IOException e){
            resp.setStatus(500);
            e.printStackTrace();
        }
    }

    public void readUserByID(HttpServletRequest req, HttpServletResponse resp, List<Integer> ids){
        try{
            User user = (User) genericDao.readByPKey(User.class, ids);

            if(user == null){
                resp.setStatus(418);
                resp.getWriter().println("User could not be found with id: " + ids.get(0));
                return;
            }

            resp.getWriter().println(mapper.writeValueAsString(user));
            resp.setHeader("Content-Type", "application/json");
            resp.setStatus(200);
        } catch (IOException e) {
            resp.setStatus(500);
            e.printStackTrace();
        }
    }

    // UPDATE
    public void updateUser(HttpServletRequest req, HttpServletResponse resp){
        try {
            User user = mapper.readValue(req.getReader().lines().collect(Collectors.joining()), User.class);

            // Can only update that which exists
            if(exists(user.getUser_id())){
                // persist the object and return it in JSON form
                resp.getWriter().println(mapper.writeValueAsString(genericDao.update(user)));
                resp.setHeader("Content-Type", "application/json");
                resp.setStatus(200);
            }else{
                resp.getWriter().println("User id " + user.getUser_id() + " does not exist. Nothing to update.");
                resp.setStatus(418);
            }

        } catch (IOException e) {
            resp.setStatus(400);
            try {
                resp.getWriter().println(e.getMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    // DELETE
    public void deleteUser(HttpServletRequest req, HttpServletResponse resp, List<Integer> ids){
        genericDao.delete(User.class, ids);
        try{
            boolean deleted = (boolean) genericDao.readByPKey(User.class, ids);

            if(!deleted){
                resp.setStatus(418);
                resp.getWriter().println("User could not be found with id: " + ids.get(0));
                return;
            }

            resp.getWriter().println("Deleted row with User id: " + ids.get(0));
            resp.setHeader("Content-Type", "application/json");
            resp.setStatus(200);
        } catch (IOException e) {
            resp.setStatus(500);
            e.printStackTrace();
        }
    }

    private boolean exists(int id){
        List<Integer> ids = Arrays.asList(id);
        return genericDao.readByPKey(User.class, ids) != null;
    }
}
