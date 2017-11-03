package cn.itcast.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by yvettee on 2017/11/3.
 */
@WebServlet(name = "ZipServlet", urlPatterns = "/zipServlet")
public class ZipServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String data = "qsadf微博等级开车门，曹禺搜索";
        // response.getOutputStream().write(data.getBytes("UTF-8"));
        response.getWriter().write(data);
    }
}
