package kr.co.ex.aop;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class Sample {
	private static final Logger logger = LoggerFactory.getLogger(Sample.class);
	
	@Around("execution(* kr.co.ex.service.MsgService*.*(..))")
	public Object startLog(ProceedingJoinPoint jp) throws Throwable{
		// ProceedingJoinPoint -> JoinPoint
		// Exception -> Throwable
		// Object[] getArgs()/ String getKind()/ Signature getSignature()
		// Object getTarget()/ Object getThis()
		logger.info("---------------------------------------");
		long startT = System.currentTimeMillis();
		logger.info(Arrays.toString(jp.getArgs()));
		Object obj = jp.proceed();
		long endT = System.currentTimeMillis();
		logger.info(jp.getSignature().getName()+" : "+(endT-startT));
		logger.info("---------------------------------------");
		return obj;
	}
}
