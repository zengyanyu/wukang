package com.manlong.wukang.aspect;

import com.manlong.wukang.annotion.DuplicateSubmitToken;
import com.manlong.wukang.exception.DuplicateSubmitException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Aspect
@Component
@Slf4j
public class DuplicateSubmitAspect {

    public static final String  DUPLICATE_TOKEN_KEY="duplicate_token_key";

    @Pointcut("execution(public * com.manlong.wukang.controller..*(..))")

    public void webLog() {
    }

    /**
     * 获取重复提交key-->duplicate_token_key+','+请求方法名
     * @param joinPoint
     * @return
     */
    public String getDuplicateTokenKey(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        StringBuilder key=new StringBuilder(DUPLICATE_TOKEN_KEY);
        key.append(",").append(methodName);
        return key.toString();
    }

    @Before("webLog() && @annotation(token)")
    public void before(final JoinPoint joinPoint, DuplicateSubmitToken token){
        if (token!=null) {
            Object[] args = joinPoint.getArgs();
            HttpServletRequest request = null;
            HttpServletResponse response = null;
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof HttpServletRequest) {
                    request = (HttpServletRequest) args[i];
                }
                if (args[i] instanceof HttpServletResponse) {
                    response = (HttpServletResponse) args[i];
                }
            }

            boolean isSaveSession = token.save();
            if (isSaveSession) {
                Object t = request.getSession().getAttribute(DUPLICATE_TOKEN_KEY);
                if (null == t) {
                    String uuid = UUID.randomUUID().toString();
                    request.getSession().setAttribute(DUPLICATE_TOKEN_KEY, uuid);
                    log.info("进入方法：token=" + uuid);
                } else {
                    log.info("重复请求！");
                    //System.out.println("请不要重复请求！");
                    //throw new DuplicateSubmitException("请不要重复请求！");
                }
            }
        }
    }

        @AfterReturning("webLog() && @annotation(token)")
        public void doAfterReturning(JoinPoint joinPoint,DuplicateSubmitToken token) {
            // 处理完请求，返回内容
            log.info("出来方法：");
            if (token!=null){
                Object[]args=joinPoint.getArgs();
                HttpServletRequest request=null;
                for (int i = 0; i < args.length; i++) {
                    if (args[i] instanceof HttpServletRequest){
                        request= (HttpServletRequest) args[i];
                    }
                }
                boolean isSaveSession=token.save();
                if (isSaveSession){
                    Object t = request.getSession().getAttribute(DUPLICATE_TOKEN_KEY);
                    if (null!=t){
                        //方法执行完毕移除请求重复标记
                        request.getSession(false).removeAttribute(DUPLICATE_TOKEN_KEY);
                        log.info("方法执行完毕移除请求重复标记！");
                    }
                }
            }
        }

    @AfterThrowing(pointcut = "webLog()&& @annotation(token)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e, DuplicateSubmitToken token) {
        if (null!=token
                && e instanceof DuplicateSubmitException==false){
            //处理处理重复提交本身之外的异常
            Object[]args=joinPoint.getArgs();
            HttpServletRequest request=null;
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof HttpServletRequest){
                    request= (HttpServletRequest) args[i];
                }
            }
            boolean isSaveSession=token.save();
            //获得方法名称
            if (isSaveSession){
                String key=getDuplicateTokenKey(joinPoint);
                Object t = request.getSession().getAttribute(key);
                if (null!=t){
                    //方法执行完毕移除请求重复标记
                    request.getSession(false).removeAttribute(key);
                    log.info("异常情况--移除请求重复标记！");
                }
            }
        }
    }
}
