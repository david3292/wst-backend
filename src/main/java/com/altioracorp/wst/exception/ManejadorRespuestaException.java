package com.altioracorp.wst.exception;

import java.time.LocalDate;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.altioracorp.wst.exception.cliente.ClientesException;
import com.altioracorp.wst.exception.cobros.CobrosException;
import com.altioracorp.wst.exception.migracion.MigradorDatosException;
import com.altioracorp.wst.exception.reporte.ReporteExeption;
import com.altioracorp.wst.exception.sistema.SistemaException;
import com.altioracorp.wst.exception.ventas.VentasException;

@ControllerAdvice
@RestController
public class ManejadorRespuestaException extends ResponseEntityExceptionHandler {

	//Metodo genérico para capturar cualquier error que no este mapeado
		@ExceptionHandler(Exception.class)
		public final ResponseEntity<RespuestaException> manejarTodasExcepciones(ModeloNotFoundException ex, WebRequest request){
			
			RespuestaException er = new RespuestaException(LocalDate.now(), ex.getMessage(), request.getDescription(false));
			return new ResponseEntity<RespuestaException>(er, HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
		@ExceptionHandler(ModeloNotFoundException.class)
		public final ResponseEntity<RespuestaException> manejarModeloException(ModeloNotFoundException ex, WebRequest request){
			
			RespuestaException er = new RespuestaException(LocalDate.now(), ex.getMessage(), request.getDescription(false));		
			return new ResponseEntity<RespuestaException>(er, HttpStatus.NOT_FOUND);
			
		}
		
		@ExceptionHandler(SistemaException.class)
		public final ResponseEntity<RespuestaException> manejarModeloException(SistemaException ex, WebRequest request){
			
			RespuestaException er = new RespuestaException(LocalDate.now(), ex.getMessage(), request.getDescription(false));		
			return new ResponseEntity<RespuestaException>(er, HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
		@ExceptionHandler(VentasException.class)
		public final ResponseEntity<RespuestaException> manejarModeloException(VentasException ex, WebRequest request){
			
			RespuestaException er = new RespuestaException(LocalDate.now(), ex.getMessage(), request.getDescription(false));		
			return new ResponseEntity<RespuestaException>(er, HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
		@ExceptionHandler(CobrosException.class)
		public final ResponseEntity<RespuestaException> manejarModeloException(CobrosException ex, WebRequest request){
			
			RespuestaException er = new RespuestaException(LocalDate.now(), ex.getMessage(), request.getDescription(false));		
			return new ResponseEntity<RespuestaException>(er, HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
		@ExceptionHandler(ReporteExeption.class)
		public final ResponseEntity<RespuestaException> manejarModeloException(ReporteExeption ex, WebRequest request){
			
			RespuestaException er = new RespuestaException(LocalDate.now(), ex.getMessage(), request.getDescription(false));		
			return new ResponseEntity<RespuestaException>(er, HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
		@ExceptionHandler(ClientesException.class)
		public final ResponseEntity<RespuestaException> manejarModeloException(ClientesException ex, WebRequest request){
			
			RespuestaException er = new RespuestaException(LocalDate.now(), ex.getMessage(), request.getDescription(false));		
			return new ResponseEntity<RespuestaException>(er, HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
		@ExceptionHandler(MigradorDatosException.class)
		public final ResponseEntity<RespuestaException> manejarModeloException(MigradorDatosException ex, WebRequest request){
			
			RespuestaException er = new RespuestaException(LocalDate.now(), ex.getMessage(), request.getDescription(false));		
			return new ResponseEntity<RespuestaException>(er, HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
		
		//Método sobre escrito para lanzar excepciones de acuerdo a los argumentos validados en la clase
		@Override
		protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
				HttpHeaders headers, HttpStatus status, WebRequest request) {
			// TODO Auto-generated method stub
			RespuestaException er = new RespuestaException(LocalDate.now(), ex.getMessage(), request.getDescription(false));		
			return new ResponseEntity<Object>(er, HttpStatus.BAD_REQUEST);
		}
}
