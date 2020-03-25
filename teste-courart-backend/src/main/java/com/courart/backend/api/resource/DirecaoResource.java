package com.courart.backend.api.resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.courart.backend.api.event.RecursoCriadoEvent;
import com.courart.backend.api.model.Direcao;
import com.courart.backend.api.service.DirecaoService;
import com.itextpdf.text.DocumentException;

@RestController
@RequestMapping("/direcoes")
public class DirecaoResource {

	@Autowired 
	private DirecaoService direcaoService;

	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping
	public List<Direcao> listar(){
		return direcaoService.buscar();
	}
	
	@GetMapping("/{codigo}")
	public Direcao buscarCodigo(@PathVariable Long codigo) {
		return direcaoService.buscarCodigo(codigo);
	}
	
	@GetMapping("/placa/{placa}")
	public List<Direcao> buscarPlaca(@PathVariable String placa) {
		return direcaoService.buscarPlaca(placa);
	}
	
	@GetMapping("/cpf/{cpf}")
	public List<Direcao> buscarCPF(@PathVariable String cpf) {
		return direcaoService.buscarCPF(cpf);
	}
	
	@PostMapping
	public ResponseEntity<Direcao> criar(@Valid @RequestBody Direcao direcao, HttpServletResponse response) {
		Direcao criada = direcaoService.salvar(direcao);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, criada.getCodigo()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(criada);
	}
	
	@PutMapping("/{codigo}")
	public ResponseEntity<Direcao> atualizar(@PathVariable Long codigo, @Valid @RequestBody Direcao direcao){
		return ResponseEntity.ok(direcaoService.atualizar(codigo, direcao));
	}
	
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo) {
		direcaoService.remover(codigo);
	}
	
	@GetMapping("/relatorio/{data1}/{data2}")
	public ResponseEntity<InputStreamResource> direcaoReport(@PathVariable String data1, @PathVariable String data2) {
		String dataInicio, dataFim;
		if(data1.compareTo(data2) >= 0) {
			dataInicio = data2;
			dataFim = data1;
		}else {
			dataInicio = data1;
			dataFim = data2;
		}
		
        ByteArrayInputStream bis = null;
		try {
			bis = direcaoService.reportDirecoesPDF(dataInicio, dataFim);
		} catch (DocumentException | IOException | ParseException e) {
			e.printStackTrace();
		}

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=guidancereport.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
	
}
