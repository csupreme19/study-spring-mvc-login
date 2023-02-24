package hello.login.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    private static final String REQUEST_ID = "REQUEST_ID";
    private static final String REQUEST_METHOD = "REQUEST_METHOD";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();

        String requestId = UUID.randomUUID().toString().split("-")[0];
        request.setAttribute(REQUEST_ID, UUID.randomUUID().toString().split("-")[0]);

        String methodName = "";
        if(handler instanceof HandlerMethod handlerMethod) {
            request.setAttribute(REQUEST_METHOD, handlerMethod);
            methodName = handlerMethod.getMethod().getName();
        }

        log.info("REQUEST[{}]={}){} {}", requestId, httpMethod, requestURI, methodName);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle={}", modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String requestId = (String) request.getAttribute(REQUEST_ID);
        String httpMethod = request.getMethod();
        String methodName = ((HandlerMethod) request.getAttribute(REQUEST_METHOD)).getMethod().getName();
        log.info("RESPONSE[{}]={}){} {}", requestId, httpMethod, requestURI, methodName);
        if(ex != null) {
            log.error("{}", ex.getLocalizedMessage(), ex);
        }
    }
}
