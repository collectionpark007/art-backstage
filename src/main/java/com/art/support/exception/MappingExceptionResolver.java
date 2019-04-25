package com.art.support.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

public class MappingExceptionResolver extends SimpleMappingExceptionResolver {
	
	  Log log = LogFactory.getLog(getClass());
	
	  private String defaultErrorView;

	  /** 接口异常处理 */
	  private String jsonErrorView;

	  /** 后台页面异常处理 */
	  private String adminErrorView;
	
	  @Override
	  protected String determineViewName(Exception ex, HttpServletRequest request) {
		  	
	  	String viewName = null;
	  	String callApi = request.getRequestURI();
	  	if ((callApi.startsWith("/api") || callApi.startsWith("/merchant") || callApi.startsWith("/b2cx")) && callApi.endsWith(".json")) {
	  		viewName = this.jsonErrorView;
		}else if (callApi.startsWith("/admin")){
			viewName = this.adminErrorView;
	  	}else{
			viewName = this.defaultErrorView;
		}
	  	//日志记录错误信息
	  	log.error("请求路径："+callApi);
	  	log.error(getTrace(ex));
        return viewName;
	  }

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

		String callApi = request.getRequestURI();
		if ((callApi.startsWith("/api") || callApi.startsWith("/merchant") || callApi.startsWith("/b2cx")) && callApi.endsWith(".json")) {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=utf-8");
		}


		return super.doResolveException(request, response, handler, ex);
	}

	  public void setDefaultErrorView(String defaultErrorView) {
			this.defaultErrorView = defaultErrorView;
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


	public void setJsonErrorView(String jsonErrorView) {
		this.jsonErrorView = jsonErrorView;
	}

	public void setAdminErrorView(String adminErrorView) {
		this.adminErrorView = adminErrorView;
	}
}
