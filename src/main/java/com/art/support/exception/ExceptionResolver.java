package com.art.support.exception;

import com.art.support.api.ResultSet;
import com.art.support.common.ReturnCode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

/**
* author never
* createDate 18/7/21
*/

@ControllerAdvice
public class ExceptionResolver {


    Log log = LogFactory.getLog(getClass());

    /**
     * 全局异常捕捉处理
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResultSet errorHandler(Exception ex) {

        log.error(getTrace(ex));

        ResultSet errorResultSet = new ResultSet(ReturnCode.error, "Request failed!");
        errorResultSet.getResult().put("ErrMsg", getTrace(ex));
        return errorResultSet;

    }

    /**
     * api 异常拦截
     * @param apiException
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = ApiException.class)
    public ResultSet apiExceptionHandler(ApiException apiException){

        ResultSet resultSet = new ResultSet(apiException.getRetCode(), apiException.getRetMessage());
        log.error(apiException.toString());
        return resultSet;

    }


    /**
     * api 异常拦截
     * @param businessException
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = BusinessException.class)
    public ResultSet businessExceptionHandler(BusinessException businessException){

        ResultSet resultSet = new ResultSet(businessException.getRetCode(), businessException.getRetMessage());
        log.error(businessException.toString());
        return resultSet;

    }

    /**
     * method argument验证异常拦截
     * @param constraintViolationException
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResultSet constraintViolationExceptionHandler(ConstraintViolationException constraintViolationException) {

        Set<ConstraintViolation<?>> violations = constraintViolationException.getConstraintViolations();

        List<String> errorMsgList = new ArrayList<String>();
        for (ConstraintViolation<?> item : violations) {
            errorMsgList.add(item.getMessage());
        }

        Map<String, Object> errorMap = new HashMap<String, Object>();
        errorMap.put("errors", errorMsgList);

        ResultSet resultSet = new ResultSet(ReturnCode.error, "Parameters Error!");
        resultSet.setResult(errorMap);

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
