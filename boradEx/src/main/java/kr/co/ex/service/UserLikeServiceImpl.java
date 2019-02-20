package kr.co.ex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import kr.co.ex.mapper.UserLikeMapper;

@Service
public class UserLikeServiceImpl implements UserLikeService {
	
	@Autowired
	UserLikeMapper likeMapper;

	@Override
	public boolean isUserLiked() throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth == null) return false;
		int likeCnt = likeMapper.isUserLiked(auth.getName());
		if(likeCnt > 0) return true;
		return false;
	}

	@Override
	public void addLike(int bno, String username) throws Exception {
		likeMapper.addLike(bno, username);
	}

	@Override
	public void subList(int bno, String username) throws Exception {
		likeMapper.subLike(bno, username);
	}
	
	
}
