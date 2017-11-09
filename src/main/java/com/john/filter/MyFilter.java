/* 
 * 项目名：	com.john.filter
 * 文件名：	MyFilter
 * 模块说明：	
 * 修改历史：
 * 2017/11/9 - ihui - 创建。
 */

package com.john.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author ihui
 * @date 2017/11/9
 */

/**
 * 自定义 zuul 过滤器
 */
@Component
public class MyFilter extends ZuulFilter {
    private Logger logger = Logger.getLogger(getClass());
    /**
     * 过滤器类型
     * pre：路由之前; routing：路由之时; post： 路由之后; error：发送错误调用
     */
    public enum FilterType {
        PRE("pre"), ROUTING("routing"), POST("post"), ERROR("error");
        private String type;
        private FilterType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    /**
     * 过滤器类型
     * @return
     */
    @Override
    public String filterType() {
        return FilterType.PRE.getType();
    }

    /**
     * 过滤器优先级
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 是否过滤, 可以根据业务做判断返回 true/false 来表明是否需要过滤。
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 具体业务逻辑 - 没有 token 的请求直接返回
     * @return
     */
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        logger.info(String.format("%s >>> %s", request.getMethod(), request.getRequestURL().toString()));
        Object accessToken = request.getParameter("token");
        if(null == accessToken){
            logger.warn("Token Is Empty");
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            try {
                ctx.getResponse().getWriter().write("Token Is Empty");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        logger.info("OK");
        return null;
    }
}
