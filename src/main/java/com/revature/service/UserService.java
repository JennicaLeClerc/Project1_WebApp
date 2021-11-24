package com.revature.service;

import com.revature.model.User;
import com.revature.persistence.GenericDao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;
import java.util.stream.*;

public class UserService {
    private final ObjectMapper mapper;
    private final GenericDao genericDao;

    public UserService(){
        this.genericDao = new GenericDao();
        this.mapper = new ObjectMapper();
    }

    /**
     * Creates the User table if it doesn't exist and a row with the given inputs.
     * @param req - Input from Postman
     * @param resp - Output to Postman
     */
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

    /**
     * Reads out all the Users on the table.
     * @param req - Input from Postman
     * @param resp - Output to Postman
     */
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

    /**
     * Reads out the User on the table with the given PKeys (ids).
     * @param req - Input from Postman
     * @param resp - Output to Postman
     * @param ids - List of PKey ids for one User object. Just a list of one user_id.
     */
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

    /**
     * Updates all columns of the User table for with the input's PKeys (user_id).
     * @param req - Input from Postman
     * @param resp - Output to Postman
     */
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

    /**
     * Deletes all columns of the User table with the given PKeys (ids).
     * @param req - Input from Postman
     * @param resp - Output to Postman
     * @param ids - List of PKey ids for one User object. Just a list of one user_id.
     */
    public void deleteUser(HttpServletRequest req, HttpServletResponse resp, List<Integer> ids){
        int user_id = ids.get(0);

        try{
            boolean deleted = genericDao.delete(User.class, ids);

            if(!deleted){
                resp.getWriter().println("User could not be found with id: " + user_id);
                resp.setHeader("Content-Type", "application/json");
                resp.setStatus(418);
                return;
            }

            resp.getWriter().println("Deleted row with User id: " + user_id);
            resp.setHeader("Content-Type", "application/json");
            resp.setStatus(200);
        } catch (IOException e) {
            resp.setStatus(500);
            e.printStackTrace();
        }
    }

    /**
     * @param id - User_id.
     * @return - returns a boolean if the user_id exists.
     */
    private boolean exists(int id){
        List<Integer> ids = Arrays.asList(id);
        return genericDao.readByPKey(User.class, ids) != null;
    }
}
