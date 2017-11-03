package cn.itcast.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

/**
 * Created by yvettee on 2017/11/3.
 */
@WebServlet(name = "ZipServletTest",urlPatterns = "/zipServletTest")
public class ZipServletTest extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String data = "aaaaaa";

        ByteArrayOutputStream bout = new ByteArrayOutputStream();//捕获内存缓冲区的数据，转换成字节数组。
        GZIPOutputStream gout = new GZIPOutputStream(bout);//定义压缩流,压缩之后写到缓冲区
        gout.write(data.getBytes());

       /*
       gout内部会维护一个缓冲，调用gout.write()时，
       默认数据会写到缓冲里去,缓冲没写满是不会往底层bout写
        */
        gout.close();

        byte gzip[] = bout.toByteArray();
        response.setHeader("content-encoding", "gzip");
        response.setHeader("content-length", gzip.length + "");

        response.getOutputStream().write(gzip);
    }
}
