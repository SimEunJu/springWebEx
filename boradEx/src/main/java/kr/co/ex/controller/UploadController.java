package kr.co.ex.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import kr.co.ex.domain.AttachVO;
import kr.co.ex.service.BoardService;
import kr.co.ex.util.file.UploadFileUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/board/daily/file")
public class UploadController {
	
	@NonNull BoardService boardServ;
	
	@Resource
	String uploadPath;
	
	@ResponseBody
	@GetMapping("")
	public ResponseEntity<byte[]> displayFile(@RequestParam String fileName){
			log.info(fileName);
			File file = new File(uploadPath+fileName);
			ResponseEntity<byte[]> result = null;
			HttpHeaders header = new HttpHeaders();
			
			try {
				header.add("Content-Type", Files.probeContentType(file.toPath()));
				return new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
			
			} catch (IOException e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
	}
	
	@ResponseBody
	@PostMapping(value="", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<AttachVO>> uploadFile(@RequestParam("uploadFile") MultipartFile[] file) throws IOException, Exception{
		try {
			List<AttachVO> attaches = new ArrayList<>();
			String savePath = UploadFileUtils.makePath(uploadPath);

			for (MultipartFile f : file) {

				AttachVO attach = new AttachVO();

				String fileName = f.getOriginalFilename();
				String saveFileName = fileName.substring(fileName.lastIndexOf("/") + 1);
				attach.setFileName(fileName);
				attach.setUploadPath(UploadFileUtils.calcFolder());

				UUID uuid = UUID.randomUUID();
				attach.setUuid(uuid.toString());
				saveFileName = uuid.toString() + "_" + saveFileName;
				File saveFile = new File(savePath, saveFileName);
				f.transferTo(saveFile);

				if (UploadFileUtils.isImage(saveFile)) {
					UploadFileUtils.makeThumbnail(savePath, saveFileName);
					attach.setFileType(f.getContentType());
				} else
					attach.setFileType(f.getContentType());
				attaches.add(attach);
				log.info(attaches.toString());
			}
			return new ResponseEntity<>(attaches, HttpStatus.CREATED);
		} catch (Exception e){
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@ResponseBody
	@GetMapping(value="/download", produces=MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<FileSystemResource> downloadFile(@RequestHeader("User-Agent") String userAgent, @RequestParam String fileName){
		log.info("userAgent: "+userAgent);
		log.info("fileName: "+fileName);
	
		FileSystemResource resource = new FileSystemResource(uploadPath+fileName);
		if(resource.exists() == false) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		String resourceName = resource.getFilename();
		String resourceOriginalName = resourceName.substring(resourceName.indexOf("_")+1);
		HttpHeaders header = new HttpHeaders();
		try{
			String downloadName = null;
			if(userAgent.contains("Trident")){
				downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8").replaceAll("/+", " ");
			}else if(userAgent.contains("Edge")){
				downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8");
			}else{
				downloadName = new String(resourceOriginalName.getBytes("UTF-8"), "ISO-8859-1");
			}
			header.add("Content-Disposition", "attachment; filename=\""+downloadName+"\"");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new ResponseEntity<>(resource, header, HttpStatus.OK);
		
	}
	
	@DeleteMapping(value="")
	public ResponseEntity<Void> deleteOneFile(@RequestParam String fileName, @RequestParam String type) throws Exception{
		File file;
		log.info(fileName);
		try{
			file = new File(uploadPath, URLDecoder.decode(fileName, "UTF-8"));
			file.delete();
			if(type.contains("image")){
				String originalFile = file.getAbsolutePath().replace("s_", "");
				file = new File(originalFile);
				file.delete();
			}
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
