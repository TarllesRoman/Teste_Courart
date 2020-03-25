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
import com.courart.backend.api.model.Motorista;
import com.courart.backend.api.service.MotoristaService;
import com.itextpdf.text.DocumentException;

@RestController
@RequestMapping("/motoristas")
public class MotoristaResource {

	@Autowired
	private MotoristaService motoristaService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping
	public List<Motorista> listar(){
		return motoristaService.buscar();
	}
	
	@GetMapping("/{codigo}")
	public Motorista buscarCodigo(@PathVariable Long codigo) {
		return motoristaService.buscarCodigo(codigo);
	}
	
	@GetMapping("/cpf/{cpf}")
	public List<Motorista> buscarCPF(@PathVariable String cpf) {
		return motoristaService.buscarCPF(cpf);
	}
	
	@GetMapping("/nome/{nome}")
	public List<Motorista> buscarNome(@PathVariable String nome){
		return motoristaService.buscarNome(nome);
	}
	
	@GetMapping("/aniversariantes/{data1}/{data2}")
	public ResponseEntity<InputStreamResource> aniversariantesReport(@PathVariable String data1, @PathVariable String data2) {
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
			bis = motoristaService.reportAniversariantesPDF(dataInicio, dataFim);
		} catch (DocumentException | IOException | ParseException e) {
			e.printStackTrace();
		}

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=birthdaysreport.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
	
	@PostMapping
	public ResponseEntity<Motorista> criar(@Valid @RequestBody Motorista motorista, HttpServletResponse response) {
		Motorista criado = motoristaService.salvar(motorista);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, criado.getCodigo()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(criado);
	}
	
	@PutMapping("/{codigo}")
	public ResponseEntity<Motorista> atualizar(@PathVariable Long codigo, @Valid @RequestBody Motorista motorista){
		return ResponseEntity.ok(motoristaService.atualizar(codigo, motorista));
	}
	
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo) {
		motoristaService.remover(codigo);
	}
	
}
