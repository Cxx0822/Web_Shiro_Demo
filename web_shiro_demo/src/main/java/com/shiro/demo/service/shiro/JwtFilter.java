package com.shiro.demo.service.shiro;

import com.alibaba.fastjson.JSON;
import com.shiro.demo.service.result.R;
import com.shiro.demo.service.result.ResultCodeEnum;
import com.shiro.demo.service.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

// 自定义过滤器
public class JwtFilter extends AuthenticatingFilter {

    // 从请求中获取token信息,然后对String类型的token进行转换成JwtToken
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // 前端获取X-Token
        String jwt = request.getHeader("X-Token");
        if (StringUtils.isEmpty(jwt)) {
            return null;
        }

        // 返回shiro中需要的Token格式
        return new JwtToken(jwt);
    }

    // 真正的拦截方法,判断请求是否带有token,没有token即为未登录状态,有token的为登录状态
    // 若没有token直接放行(登录状态或者其他情况)
    // 若有token则需要判断这个token是否合法或者是否失效等
    // 最后调用executeLogin方法,提交给登录
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // 需要和前端的axios的header字段保持一致
        String jwt = request.getHeader("X-Token");
        if (StringUtils.isEmpty(jwt)) {
            return true;
        } else {
            // 校验jwt
            Claims claim = JwtUtil.parseJWT(jwt);
            // 判断token是否为空或者是否过期
            if (claim == null || JwtUtil.isTokenExpired(claim.getExpiration())) {
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                response.setContentType("application/plain;charset=utf-8");
                PrintWriter writer = response.getWriter();
                // 向前端发送过期错误提示
                writer.write(JSON.toJSONString(R.setResult(ResultCodeEnum.TokenExpiredException)));
                return false;
            }
            // 执行shiro中的登录方法
            // 需要执行登录，否则获取接口时没有subject信息
            return executeLogin(servletRequest, servletResponse);
        }
    }

    // 登录失败处理
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        Throwable throwable = e.getCause() == null ? e : e.getCause();

        String json = JSON.toJSONString(R.error().message(e.getMessage()));

        try {
            // 向前端返回错误信息
            httpServletResponse.getWriter().print(json);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return false;
    }

    // 跨域处理
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {

        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
}
