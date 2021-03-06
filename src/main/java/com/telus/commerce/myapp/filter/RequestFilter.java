package com.telus.commerce.myapp.filter;

//import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class RequestFilter implements Filter {

  //private ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {}

  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    MultiReadHttpServletRequest requestCopy = new MultiReadHttpServletRequest(request);
    try {
        /**
         * use requestCopy to get the information from http request
         * e.g requestCopy.getParameter("queryParameter")
         */
      MDC.put("transactionId", UUID.randomUUID().toString());
      filterChain.doFilter(requestCopy, servletResponse);
    } catch (Throwable e) {
      filterChain.doFilter(requestCopy, servletResponse);
    } finally {
      MDC.clear();
    }
  }

  @Override
  public void destroy() {}

  private static class MultiReadHttpServletRequest extends HttpServletRequestWrapper {
    private ByteArrayOutputStream cachedBytes;

    public MultiReadHttpServletRequest(HttpServletRequest request) {
      super(request);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
      if (cachedBytes == null) cacheInputStream();

      return new CachedServletInputStream();
    }

    @Override
    public BufferedReader getReader() throws IOException {
      return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    private void cacheInputStream() throws IOException {
      /* Cache the inputstream in order to read it multiple times. For
       * convenience, I use apache.commons IOUtils
       */
      cachedBytes = new ByteArrayOutputStream();
      IOUtils.copy(super.getInputStream(), cachedBytes);
    }

    /* An inputstream which reads the cached request body */
    private class CachedServletInputStream extends ServletInputStream {
      private ByteArrayInputStream input;

      public CachedServletInputStream() {
        /* create a new input stream from the cached request body */
        input = new ByteArrayInputStream(cachedBytes.toByteArray());
      }

      @Override
      public int read() throws IOException {
        return input.read();
      }

      @Override
      public boolean isFinished() {
        // TODO Auto-generated method stub
        return false;
      }

      @Override
      public boolean isReady() {
        // TODO Auto-generated method stub
        return false;
      }

      @Override
      public void setReadListener(ReadListener listener) {
        // TODO Auto-generated method stub

      }
    }
  }
}
