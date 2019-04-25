package com.art.support.exception.advice;

import com.art.support.api.ResultSet;
import com.art.support.common.ReturnCode;
import com.art.support.exception.AbstractException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *
 * 全局异常处理
* author never
* createDate 19/04/25
*/
@RestControllerAdvice
public class ExceptionControllerAdvice {

    private Log log = LogFactory.getLog(getClass());

    @ExceptionHandler(Exception.class)
    public @ResponseBody
    ResultSet errorHandler(HttpServletRequest request, Exception ex){

        ResultSet resultSet = new ResultSet();

        if (ex instanceof AbstractException){
            resultSet.setRetCode(((AbstractException) ex).getRetCode());
            resultSet.setRetMessage(((AbstractException) ex).getRetMessage());
        }else{
            resultSet.setRetCode(ReturnCode.error);
            resultSet.setRetMessage("请求失败!");
        }


        String callApi = request.getRequestURI();
        log.error("请求接口："+callApi);
        log.error(getTrace(ex));

        return resultSet;

    }


    /**
     * 获取错误信息
     * @author never
     * @param t
     * @return
     */
    private String getTrace(Throwable t) {
        StringWriter stringWriter= new StringWriter();
        PrintWriter writer= new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        StringBuffer buffer= stringWriter.getBuffer();
        return buffer.toString();
    }

}
