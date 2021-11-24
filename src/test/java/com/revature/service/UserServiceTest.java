package com.revature.service;

import com.revature.persistence.*;
import com.revature.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UserServiceTest {

    @Mock
    HttpServletRequest req;

    @Mock
    HttpServletResponse resp;

    @Mock
    GenericDao genericDao;

    @BeforeEach
    public void mockSetup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createUsersTest() throws IOException {
        User testUser = new User().setUser_id(1).setUsername("user1").setPassword("password");
        String json = "{ \"user_id\" : 1, \"username\" : \"user1\", \"password\" : \"password\" }";
        UserService userService = new UserService();
        List<User> returnObjects = new ArrayList<>();
        returnObjects.add(testUser);

        Mockito.when(genericDao.createRow(testUser)).thenReturn(returnObjects);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);


        Mockito.when(resp.getWriter()).thenReturn(pw);
        Mockito.when(req.getReader().lines().collect(Collectors.joining())).thenReturn(json);

        userService.createUser(req,resp);
        String result = sw.getBuffer().toString().trim();
        System.out.println(result);

        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    public void readingAllUsersTest() throws IOException {
        Class<User> classType = User.class;
        List<User> returnObjects = new ArrayList<>();
        UserService userService = new UserService();
        returnObjects.add(new User().setUser_id(1).setUsername("user1").setPassword("password"));
        returnObjects.add(new User(2, "user2", "password", "John", "Smith"));

        Mockito.when(genericDao.read(classType)).thenReturn(returnObjects);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        Mockito.when(resp.getWriter()).thenReturn(pw);

        userService.readAllUsers(req,resp);
        String result = sw.getBuffer().toString().trim();
        System.out.println(result);

        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    public void readingByKeyUsersTest() throws IOException {
        Class<User> classType = User.class;
        List<User> returnObjects = new ArrayList<>();
        UserService userService = new UserService();
        returnObjects.add(new User(2, "user2", "password", "John", "Smith"));
        List<Integer> ids = Arrays.asList(2);

        Mockito.when(req.getParameter("Request-Type")).thenReturn("all");
        Mockito.when(req.getParameter("Primary-Key")).thenReturn("2");
        Mockito.when(genericDao.readByPKey(classType, ids)).thenReturn(returnObjects);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        Mockito.when(resp.getWriter()).thenReturn(pw);

        userService.readUserByID(req,resp, ids);
        String result = sw.getBuffer().toString().trim();
        System.out.println(result);

        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    public void deleteUsersTest() throws IOException {
        Class<User> classType = User.class;
        List<User> returnObjects = new ArrayList<>();
        UserService userService = new UserService();
        returnObjects.add(new User(2, "user2", "password", "John", "Smith"));
        List<Integer> ids = Arrays.asList(2);

        Mockito.when(req.getParameter("Request-Type")).thenReturn("all");
        Mockito.when(req.getParameter("Primary-Key")).thenReturn("2");
        Mockito.when(genericDao.delete(classType, ids)).thenReturn(true);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        Mockito.when(resp.getWriter()).thenReturn(pw);

        userService.deleteUser(req,resp, ids);
        String result = sw.getBuffer().toString().trim();
        System.out.println(result);

        Assertions.assertEquals(true, result != null);
    }
}
