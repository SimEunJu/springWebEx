package kr.co.ex.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.ex.domain.AttachVO;
import kr.co.ex.service.BoardService;
import kr.co.ex.util.UploadFileUtils;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
public class UploadController {
	
	@Autowired
	private ApplicationContext appContext;
	
	@Autowired
	BoardService serv;
	
	@Resource
	String uploadPath;
	
	@PostMapping(value="/uploadAjax", produces="text/plain; charset=utf-8")
	@ResponseBody
	public ResponseEntity<String> uploadFile(MultipartFile[] uploadFile, HttpServletRequest req) throws IOException, Exception{
		 MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) req;
		   MultipartFile multipartFile = multipartRequest.getFile("uploadFile");
		   log.info(multipartFile.getName());
		List<AttachVO> attaches = new ArrayList<>();
		String savePath = UploadFileUtils.makePath(uploadPath);
		
		for(MultipartFile f : uploadFile){
		
			AttachVO attach = new AttachVO();
			
			String fileName = f.getOriginalFilename();
			String saveFileName = fileName.substring(fileName.lastIndexOf("\\")+1);
			attach.setFileName(fileName);
			attach.setUploadPath(UploadFileUtils.calcFolder());
			
			UUID uuid = UUID.randomUUID();
			attach.setUuid(uuid.toString());
			saveFileName = uuid.toString() + "_" + saveFileName;
			File saveFile = new File(savePath, saveFileName);
			f.transferTo(saveFile);
			
			if(UploadFileUtils.isImage(saveFile)){
				UploadFileUtils.makeThumbnail(savePath, saveFileName);
				attach.setFileType(true);
			}
			
			attaches.add(attach);
		}
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@GetMapping(value="/donwload", produces=MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public ResponseEntity<FileSystemResource> downloadFile(@RequestHeader("User-Agent") String userAgent, String fileName) throws IOException{
		FileSystemResource resource = new FileSystemResource(uploadPath+fileName);
		if(resource.exists() == false) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		String resourceName = resource.getFilename();
		String resourceOriginalName = resourceName.substring(resourceName.indexOf("_")+1);
		HttpHeaders header = new HttpHeaders();
		try{
			String downloadName = null;
			if(userAgent.contains("Trident")){
				downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8").replaceAll("\\+", " ");
			}else if(userAgent.contains("Edge")){
				downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8");
			}else{
				downloadName = new String(resourceOriginalName.getBytes("UTF-8"), "ISO-8859-1");
			}
			header.add("Content-Disposition", "attachment; filename="+downloadName);
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return new ResponseEntity<>(resource, header, HttpStatus.OK);
		
	}
	
	@PostMapping("/deleteFile")
	@ResponseBody
	public ResponseEntity<String> deleteOneFile(String fileName, String type) throws Exception{
		File file;
		try{
			file = new File(uploadPath, URLDecoder.decode(fileName, "UTF-8"));
			file.delete();
			if(type.equals("image")){
				String originalFile = file.getAbsolutePath().replace("s_", "");
				file = new File(originalFile);
				file.delete();
			}
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>("deleted", HttpStatus.OK);
	}

}
