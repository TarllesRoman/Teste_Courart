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
import com.courart.backend.api.model.Veiculo;
import com.courart.backend.api.service.VeiculoService;
import com.itextpdf.text.DocumentException;

@RestController
@RequestMapping("/veiculos")
public class VeiculoResource {

	@Autowired
	private VeiculoService veiculoService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping
	public List<Veiculo> listar(){
		return veiculoService.buscar();
	}
	
	@GetMapping("/{codigo}")
	public Veiculo buscarCodigo(@PathVariable Long codigo) {
		return veiculoService.buscarCodigo(codigo);
	}
	
	@GetMapping("/placa/{placa}")
	public List<Veiculo> buscarPlaca(@PathVariable String placa) {
		return veiculoService.buscarPlaca(placa);
	}
	
	@GetMapping("/modelo/{modelo}")
	public List<Veiculo> buscarModelo(@PathVariable String modelo){
		return veiculoService.buscarModelo(modelo);
	}
	
	@PostMapping
	public ResponseEntity<Veiculo> criar(@Valid @RequestBody Veiculo veiculo, HttpServletResponse response) {
		if(veiculo.getDataDesativacao() != null)
			veiculo.setAtivo(false);
		Veiculo criado = veiculoService.salvar(veiculo);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, criado.getCodigo()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(criado);
	}
	
	@PutMapping("/{codigo}")
	public ResponseEntity<Veiculo> atualizar(@PathVariable Long codigo, @Valid @RequestBody Veiculo veiculo){
		Veiculo v = veiculoService.atualizar(codigo, veiculo);
		return ResponseEntity.ok(v);
	}
	
	@PutMapping("/{codigo}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarAtivo(@PathVariable Long codigo, @RequestBody Boolean ativo) {
		veiculoService.atualizarAtivo(codigo, ativo);
	}
	
	@GetMapping("/ativados/{data1}/{data2}")
	public ResponseEntity<InputStreamResource> ativadosReport(@PathVariable String data1, @PathVariable String data2) {
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
			bis = veiculoService.reportAtivadosPDF(dataInicio, dataFim);
		} catch (DocumentException | IOException | ParseException e) {
			e.printStackTrace();
		}

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=enabledreport.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
	
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo) {
		veiculoService.remover(codigo);
	}
}
