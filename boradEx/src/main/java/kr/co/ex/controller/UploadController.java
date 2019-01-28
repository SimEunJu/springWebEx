package kr.co.ex.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import kr.co.ex.domain.AttachVO;
import kr.co.ex.service.BoardService;
import kr.co.ex.util.UploadFileUtils;

@Controller
public class UploadController {
	
	@Autowired
	BoardService serv;
	
	@Resource
	String uploadPath;
	
	@PostMapping(value="/uploadAjax", produces="text/plain; charset=utf-8")
	@ResponseBody
	public ResponseEntity<String> uploadFile(MultipartFile[] file) throws IOException, Exception{
		List<AttachVO> attaches = new ArrayList<>();
		String savePath = UploadFileUtils.makePath(uploadPath);
		
		for(MultipartFile f : file){
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
	
	@GetMapping("/displayFile")
	@ResponseBody
	public ResponseEntity<byte[]> displayFile(String fileName) throws IOException{
		InputStream in = null;
		ResponseEntity<byte[]> res = null;
		try{
			String format = fileName.substring(fileName.indexOf(".")+1);
			HttpHeaders header = new HttpHeaders();
			in = new FileInputStream(uploadPath+fileName.replace("/", File.separator));
		
			if(mediaType != null){
				header.setContentType(mediaType);
			}else{
				fileName = fileName.substring(fileName.indexOf("_")+1);
				header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				header.set("Content-Disposition", "attachment; filename=\""+new String(fileName.getBytes("UTF-8"), "ISO-8859-1")+"\"");
			}
			res = new ResponseEntity<>(IOUtils.toByteArray(in), header, HttpStatus.CREATED);
		}catch(Exception e){
			e.printStackTrace();
			res = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}finally{
			in.close();
		}
		return res;
	}
	
	@PostMapping("/deleteFile")
	@ResponseBody
	public ResponseEntity<Void> deleteOneFile(@RequestParam String file, @RequestParam(defaultValue="false") boolean deep) throws Exception{
		if(file == null) return new ResponseEntity<>(HttpStatus.OK);
		
		String foramt = file.substring(file.indexOf(".")+1);
		MediaType type = MediaUtil.getMediaType(foramt);
		if(type != null){
			String front = file.substring(0, 12);
			String end = file.substring(14);
			new File(uploadPath+(front+end).replace("/", File.separator)).delete();
			if(deep){
				serv.removeAttach(front+end);
			}
		}
		new File(uploadPath+file.replace("/", File.separator)).delete();
		if(deep){
			serv.removeAttach(file);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/deleteAllFiles")
	@ResponseBody
	public ResponseEntity<Void> deleteFile(@RequestParam("files[]") String[] files){
		if(files == null || files.length == 0) return new ResponseEntity<>(HttpStatus.OK);
		for(String file : files){
			String foramt = file.substring(file.indexOf(".")+1);
			MediaType type = MediaUtil.getMediaType(foramt);
			if(type != null){
				String front = file.substring(0, 12);
				String end = file.substring(14);
				new File(uploadPath+(front+end).replace("/", File.separator)).delete();
			}
			new File(uploadPath+file.replace("/", File.separator)).delete();
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
