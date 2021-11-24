package com.revature.servlet;

import com.revature.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/users/")
public class UserServlet extends HttpServlet {

    private final UserService userService = new UserService();

    /**
     * Creates the User table if it doesn't exist and a row with the given inputs.
     * @param req - Input from Postman
     * @param resp - Output to Postman
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        userService.createUser(req, resp);
    }

    /**
     * Reads out the User on the table with the PKeys (ids) of the given inputs if the user_id != 0, else it reads out
     * all the Users on the table if the user_id from the req == 0.
     * @param req - Input from Postman
     * @param resp - Output to Postman
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user_id = req.getParameter("user_id");
        List<Integer> ids = Arrays.asList(Integer.parseInt(user_id));
        if(user_id != null){
            if(Integer.parseInt(user_id) == 0){
                userService.readAllUsers(req, resp);
                return;
            }
            userService.readUserByID(req, resp, ids);
            return;
        }
        userService.readAllUsers(req, resp);
    }

    /**
     * Updates all columns of the User table for with the given inputs.
     * @param req - Input from Postman
     * @param resp - Output to Postman
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        userService.updateUser(req, resp);
    }

    /**
     * Deletes all columns of the User table with the given user_id.
     * @param req - Input from Postman
     * @param resp - Output to Postman
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user_id = req.getParameter("user_id");
        List<Integer> ids = Arrays.asList(Integer.parseInt(user_id));
        if(user_id != null){
            userService.deleteUser(req, resp, ids);
            return;
        }
        resp.getWriter().println("User id does not exist");
        resp.setStatus(418);
    }
}
