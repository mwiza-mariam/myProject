package LanguageDao;

import domain.Admin;
import domain.Student;
import domain.Teacher;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//@WebFilter(filterName = "AuthFilter", urlPatterns = { "*.xhtml" })
public class AuthorizationFilter implements Filter {

	public AuthorizationFilter() {
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		try {

			HttpServletRequest reqt = (HttpServletRequest) request;
		
                        HttpServletResponse resp = (HttpServletResponse) response;
			HttpSession ses = reqt.getSession(false);

			String reqURI = reqt.getRequestURI();
                        if (reqURI.contains("/admin/") && (ses == null || ses.getAttribute("s_user") == null || ! (ses.getAttribute("s_user") instanceof Admin) )){
                            resp.sendRedirect(reqt.getContextPath() + "/faces/Login.xhtml");
                        }
                        else if (reqURI.contains("/student/") && (ses == null || ses.getAttribute("s_user") == null || ! (ses.getAttribute("s_user") instanceof Student) )){
                            resp.sendRedirect(reqt.getContextPath() + "/faces/Login.xhtml");
                        }
                        else if (reqURI.contains("/teacher/") && (ses == null || ses.getAttribute("s_user") == null || ! (ses.getAttribute("s_user") instanceof Teacher) )){
                            resp.sendRedirect(reqt.getContextPath() + "/faces/Login.xhtml");
                        }
			chain.doFilter(request, response);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void destroy() {

	}
}