package cn.itcast.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;

/**
 * Created by yvettee on 2017/11/3.
 */
public class GzipFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;


        //采用增强类传过去
        MyResponse myResponse = new MyResponse(response);
        filterChain.doFilter(request, myResponse);


        //取出缓冲的数据压缩后输出
        byte out[] = myResponse.getBuffer();//得到目标资源的输出

        System.out.println("压缩之前：" + out.length);
        byte gzipout[] = gzip(out);
        System.out.println("压缩之后：" + gzipout.length);
        response.setHeader("content-encoding", "gzip");
        response.setHeader("content-length", gzipout.length + "");

        response.getOutputStream().write(gzipout);
    }

    public byte[] gzip(byte b[]) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        GZIPOutputStream gout = new GZIPOutputStream(bout);
        gout.write(b);
        gout.close();

        return bout.toByteArray();
    }

    class MyResponse extends HttpServletResponseWrapper {

        private ByteArrayOutputStream bout = new ByteArrayOutputStream();//往这个流里写数据
        private HttpServletResponse response;
        private PrintWriter pw;

        public MyResponse(HttpServletResponse response) {
            super(response);
            this.response = response;
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {

            return new MyServletOutputStream(bout);//response.getOutputStream().write("hahah");
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            pw = new PrintWriter(new OutputStreamWriter(bout, response.getCharacterEncoding()));
            return pw;//MyResponse.getWriter("as");
        }

        //数据已经写到bout里，现在需要得到这些数据
        public byte[] getBuffer() {

            if (pw != null) {
                pw.close();
            }
            return bout.toByteArray();
        }
    }

    class MyServletOutputStream extends ServletOutputStream {
        private ByteArrayOutputStream bout;

        //构造函数接收MyResponse里的bout
        public MyServletOutputStream(ByteArrayOutputStream bout) {
            this.bout = bout;
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {

        }

        //当调用write方法写数据时，控制数据进bout里
        @Override
        public void write(int b) throws IOException {
            bout.write(b);
        }
    }

    @Override
    public void destroy() {

    }
}
