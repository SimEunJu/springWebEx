package kr.co.ex.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString 
@RequiredArgsConstructor @NoArgsConstructor
public class PollingNotiDto {
	
	@NonNull private int nno;
	@NonNull private String username;
	@NonNull private int limit;
	
	private int cnt;
}
