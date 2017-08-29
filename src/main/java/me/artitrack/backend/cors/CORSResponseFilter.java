package me.artitrack.backend.cors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CORSResponseFilter implements Filter {

  @Value("${server.cors.origins}")
  private String[] corsOrigins;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    // Not needed
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;
    String requestOrigin = req.getHeader("Origin");
    for (String origin : corsOrigins) {
      if (requestOrigin != null && requestOrigin.startsWith(origin)) {
        res.setHeader("Access-Control-Allow-Origin", origin);
        break;
      }
    }
    res.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
    res.setHeader("Access-Control-Allow-Headers", "x-requested-with");
    res.setHeader("Access-Control-Max-Age", "3600");
    res.setHeader("Access-Control-Allow-Credentials", "true");
    chain.doFilter(request, res);
  }

  @Override
  public void destroy() {
    // Not needed
  }
}
