package com.mini_mall.controller;

import com.mini_mall.dto.UserDTO;
import com.mini_mall.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/user/*")
public class UserController extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getPathInfo();

        switch (path) {
            case "/register":
                req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
                break;
            case "/login":
                req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
                break;
            case "/logout":
                HttpSession session = req.getSession(false);
                if (session != null) session.invalidate();
                resp.sendRedirect(req.getContextPath() + "/user/login");
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String path = req.getPathInfo();

        switch (path) {
            case "/register":
                handleRegister(req, resp);
                break;
            case "/login":
                handleLogin(req, resp);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // 회원가입 
    private void handleRegister(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        UserDTO user = new UserDTO();
        user.setLoginId(req.getParameter("loginId"));
        user.setPassword(req.getParameter("password"));
        user.setName(req.getParameter("name"));

        boolean success = userService.register(user);

        if (success) {
            resp.sendRedirect(req.getContextPath() + "/user/login");
        } else {
            req.setAttribute("errorMsg", "이미 사용 중인 아이디입니다.");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
        }
    }

    // 로그인 
    private void handleLogin(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String loginId  = req.getParameter("loginId");
        String password = req.getParameter("password");

        UserDTO loginUser = userService.login(loginId, password);

        if (loginUser != null) {
            HttpSession session = req.getSession();
            session.setAttribute("loginUser", loginUser);
            resp.sendRedirect(req.getContextPath() + "/main");
        } else {
            req.setAttribute("errorMsg", "아이디 또는 비밀번호가 틀렸습니다.");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        }
    }
}