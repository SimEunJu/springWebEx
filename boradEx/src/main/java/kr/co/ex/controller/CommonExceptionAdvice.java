package kr.co.ex.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class CommonExceptionAdvice {
	private static final Logger logger = LoggerFactory.getLogger(CommonExceptionAdvice.class);
	
	@ExceptionHandler(Exception.class)
	public ModelAndView common(Exception e){
		logger.info(e.toString());
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/err_common");
		mav.addObject("exception", e);
		return mav;
	}
}
