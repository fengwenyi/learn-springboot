//package com.fengwenyi.springbootsecurity;
//
//import org.springframework.util.AntPathMatcher;
//import org.springframework.util.PathMatcher;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//
///**
// * @author Erwin Feng
// * @since 2019-04-23 10:26
// */
//public class VerifyFilter extends OncePerRequestFilter {
//    private static final PathMatcher pathMatcher = new AntPathMatcher();
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        if(isProtectedUrl(request)&&!validateVerify(request.getSession())){
//            request.getRequestDispatcher("/login?Verify").forward(request,response);
//        }else{
//            filterChain.doFilter(request,response);
//        }
//    }
//
//    //判断验证码是否已经通过
//    private boolean validateVerify(HttpSession session){
//        return StringUtils.isEmpty(session.getAttribute(Common.verify))?false :(boolean) session.getAttribute(Common.verify);
//    }
//
//    // 拦截 /login的POST请求
//    private boolean isProtectedUrl(HttpServletRequest request) {
//        return  "POST".equals(request.getMethod()) && pathMatcher.match("/login", request.getServletPath());
//    }
//}
