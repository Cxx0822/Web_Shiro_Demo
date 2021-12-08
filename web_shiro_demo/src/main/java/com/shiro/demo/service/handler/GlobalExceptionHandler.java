package com.shiro.demo.service.handler;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.shiro.demo.service.result.R;
import com.shiro.demo.service.result.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLSyntaxErrorException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e){
        e.printStackTrace();  // 是否需要在终端显示错误信息
        return R.setResult(ResultCodeEnum.UNKNOWN_REASON);
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public R error(NullPointerException e) {
        e.printStackTrace();
        // log.error("空对象错误");
        // log.error(ExceptionUtil.getMessage(e));
        return R.setResult(ResultCodeEnum.NullPointerException);
    }

    @ExceptionHandler(SQLSyntaxErrorException.class)
    @ResponseBody
    public R error(SQLSyntaxErrorException e) {
        // e.printStackTrace();
        return R.setResult(ResultCodeEnum.SQLSyntaxErrorException);
    }

    @ExceptionHandler(JWTDecodeException.class)
    @ResponseBody
    public R error(JWTDecodeException e){
        // e.printStackTrace();
        return R.setResult(ResultCodeEnum.JWTDecodeException);
    }

    @ExceptionHandler(SignatureVerificationException.class)
    @ResponseBody
    public R error(SignatureVerificationException e){
        //e.printStackTrace();
        return R.setResult(ResultCodeEnum.SignatureVerificationException);
    }

    @ExceptionHandler(TokenExpiredException.class)
    @ResponseBody
    public R error(TokenExpiredException e){
        //e.printStackTrace();
        return R.setResult(ResultCodeEnum.TokenExpiredException);
    }

    @ExceptionHandler(AlgorithmMismatchException.class)
    @ResponseBody
    public R error(AlgorithmMismatchException e){
        //e.printStackTrace();
        return R.setResult(ResultCodeEnum.AlgorithmMismatchException);
    }

    @ExceptionHandler(UnknownAccountException.class)
    @ResponseBody
    public R error(UnknownAccountException e){
        //e.printStackTrace();
        return R.setResult(ResultCodeEnum.UnknownAccountException);
    }

    @ExceptionHandler(IncorrectCredentialsException.class)
    @ResponseBody
    public R error(IncorrectCredentialsException e){
        //e.printStackTrace();
        return R.setResult(ResultCodeEnum.IncorrectCredentialsException);
    }
}