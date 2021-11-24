package com.revature.servlet;

import com.revature.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/users/*")
public class UserServlet extends HttpServlet {

    private final UserService userService = new UserService();

    // CREATE
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        userService.createUser(req, resp);
    }

    // READ
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user_id = req.getParameter("user_id");
        List<Integer> ids = Arrays.asList(Integer.parseInt(user_id));
        if(user_id != null){
            userService.readUserByID(req, resp, ids);
            return;
        }
        userService.readAllUsers(req, resp);
    }

    // UPDATE
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        userService.updateUser(req, resp);
    }

    // DELETE
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
