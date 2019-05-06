package kr.co.ex.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import kr.co.ex.annoation.Loggable;
import lombok.extern.log4j.Log4j2;

@Aspect
@Component
@Log4j2
public class AdminLogging {

	private final String argsPrefix = "[args] : ";
	private final String returnPrefix = "[return] : ";
	
	@Around("execution(* kr.co.ex.service.*.*(..)) && @annotation(loggable)")
	public Object logArg(ProceedingJoinPoint pjp, Loggable loggable) throws Throwable{
		Object[] args = pjp.getArgs();
		
		if(args != null){
			log.trace(argsPrefix + pjp.getSignature());
			for(Object obj : args){
				log.trace(obj.toString());
			}
		}
		
		Object ret = pjp.proceed();
		if(ret != null) log.trace(returnPrefix + ret.toString());
		
		return ret;
	}
}
