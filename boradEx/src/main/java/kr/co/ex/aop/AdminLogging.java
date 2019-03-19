package kr.co.ex.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import kr.co.ex.annoation.Loggable;
import lombok.extern.log4j.Log4j;

@Aspect
@Component
@Log4j
public class AdminLogging {

	private final String argsPrefix = "[args] : ";
	private final String returnPrefix = "[return] : ";
	
	@Around("execution(* kr.co.ex.service.*.*(..)) && annoation(loggable)")
	public Object logArg(ProceedingJoinPoint pjp, Loggable loggable) throws Throwable{
		
		log.trace(pjp.getArgs().toString());
		Object ret = pjp.proceed();
		log.trace(returnPrefix + ret.toString());
		
		return ret;
	}
}
