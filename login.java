//package com.uniquedeveloper.registeration;
//
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//
//import javax.servlet.RequestDispatcher;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
///**
// * Servlet implementation class login
// */
//@WebServlet("/login")
//public class login extends HttpServlet {
//    private static final long serialVersionUID = 1L;
//   
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String uemail = request.getParameter("username");
//        String upwd = request.getParameter("password");
//        HttpSession session = request.getSession();
//        RequestDispatcher dispatcher = null;
//        
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//
//            // Establish connection
//            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/youtube?useSSL=false", "root", "root");
//
//            // Prepare SQL statement
//            String sql = "SELECT * FROM users WHERE uemail=? AND upwd=?";
//            PreparedStatement pst = con.prepareStatement(sql);
//            pst.setString(1, uemail);
//            pst.setString(2, upwd);
//            
//            ResultSet rs = pst.executeQuery();
//            if (rs.next()) {
//                session.setAttribute("name", rs.getString("uname"));
//                dispatcher = request.getRequestDispatcher("index.jsp");
//            } else {
//                request.setAttribute("status", "failed");
//                dispatcher = request.getRequestDispatcher("login.jsp");
//            }
//            dispatcher.forward(request, response);
//            
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}


package com.uniquedeveloper.registeration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class login extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uemail = request.getParameter("username");
        String upwd = request.getParameter("password");
        
        HttpSession session = request.getSession();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/youtube?useSSL=false", "root", "root");

            // Prepare SQL statement
            String sql = "SELECT * FROM users WHERE uemail=? AND upwd=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, uemail);
            pst.setString(2, upwd);

            // Execute SQL statement
            rs = pst.executeQuery();
            
            if (rs.next()) {
                // Set session attribute for the logged-in user
                session.setAttribute("username", uemail);  // Assuming uemail is unique and can serve as username
                // Redirect to welcome page
                response.sendRedirect("welcome.jsp");
            } else {
                // Set status attribute for login failure
                request.setAttribute("status", "failed");
                RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
                dispatcher.forward(request, response);
            }
            
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            // Forward to error page or handle error accordingly
            request.setAttribute("status", "error");
            RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
            dispatcher.forward(request, response);
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
