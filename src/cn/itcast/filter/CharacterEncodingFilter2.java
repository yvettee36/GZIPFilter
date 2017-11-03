package cn.itcast.filter;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by yvettee on 2017/10/31.
 */
//真正解决全站乱码问题
public class CharacterEncodingFilter2 implements Filter {
    private FilterConfig config;
    private String defaultConfig = "UTF-8";//定义一个缺省的字符集

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.config = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String charset = this.config.getInitParameter("charset");
        if (charset == null) {
            charset = defaultConfig;
        }

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        request.setCharacterEncoding(charset);
        response.setCharacterEncoding(charset);
        response.setContentType("text/html;charset = " + charset);

        filterChain.doFilter(new MyRequest(request), response);
    }

    /*
    1.写一个类，实现与被增强对象相同的接口
    2.定义一个变量，记住被增强对象
    3.定义一个构造方法，接收被增强对象
    4.覆盖想增强的方法
    5.对于不想增强的方法，直接调用被增强对象（目标对象）的方法
     */

    //编写自定义类
    class MyRequest extends HttpServletRequestWrapper {

        private HttpServletRequest request;

        public MyRequest(HttpServletRequest request) {
            //传一个被增强对象request进去
            super(request);
            this.request = request;
        }

        @Override
        public String getParameter(String name) {
            String value = request.getParameter(name);
            if (!request.getMethod().equalsIgnoreCase("get")) {
                return value;
            }
            if (value == null) {
                return null;
            }
            try {
                return value = new String(value.getBytes("iso8859-1"), request.getCharacterEncoding());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void destroy() {

    }
}
