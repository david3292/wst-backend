package com.altioracorp.wst.controlador.migracion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.altioracorp.wst.servicio.migracion.IMigracionServicio;

@RestController
@RequestMapping("/migraciones")
public class MigracionControlador {
	
	@Autowired
	private IMigracionServicio servicio;
	
	@PostMapping(value = "/importarDatos", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<Object> importarDatos(@RequestParam("adjunto") MultipartFile file) {
		
			boolean finalizado = Boolean.FALSE;
			File archivo = new File("java.io.tmpdir");

			try (OutputStream os = new FileOutputStream(archivo)) {
			    os.write(file.getBytes());
			    
			    servicio.importarDatos(archivo);
			    finalizado = Boolean.TRUE;
			    
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return new ResponseEntity<Object>(finalizado,HttpStatus.CREATED);
		
	}

}
