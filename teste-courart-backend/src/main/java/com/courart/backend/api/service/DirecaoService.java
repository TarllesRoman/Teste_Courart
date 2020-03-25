package com.courart.backend.api.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.courart.backend.api.model.Direcao;
import com.courart.backend.api.model.Motorista;
import com.courart.backend.api.model.Veiculo;
import com.courart.backend.api.repository.DirecaoRepository;
import com.courart.backend.api.repository.MotoristaRepository;
import com.courart.backend.api.repository.VeiculoRepository;
import com.courart.backend.api.service.exception.MotoristaInexistenteException;
import com.courart.backend.api.service.exception.VeiculoInexistenteException;
import com.courart.backend.api.utils.PdfUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class DirecaoService {

	@Autowired
	private DirecaoRepository direcaoRepository;
	
	@Autowired
	private MotoristaRepository motoristaRepository;
	
	@Autowired
	private VeiculoRepository veiculoRepository;
	
	public List<Direcao> buscar() {
		List<Direcao> dirs = null;
		
		try {
			dirs = direcaoRepository.findAll();
		} catch (Exception e) {
			throw new EmptyResultDataAccessException(1);
		}
		
		return dirs;
	}
	
	public Direcao buscarCodigo(Long codigo) {
		Direcao dir = null;
		
		try {
			dir = direcaoRepository.findById(codigo).get();
		} catch (Exception e) {
			throw new EmptyResultDataAccessException(1);
		}
		
		return dir;
	}
	
	public List<Direcao> buscarCPF(String cpf) {
		List<Direcao> dirs = null;
		
		try {
			dirs = direcaoRepository.findByCPF(cpf);
		} catch (Exception e) {
			throw new EmptyResultDataAccessException(1);
		}
		
		return dirs;
	}
	
	public List<Direcao> buscarPlaca(String placa) {
		List<Direcao> dirs = null;
		
		try {
			dirs = direcaoRepository.findByPlaca(placa);
		} catch (Exception e) {
			throw new EmptyResultDataAccessException(1);
		}
		
		return dirs;
	}
	
	public Direcao atualizar(Long codigo, Direcao direcao) {
		Direcao dir = buscarCodigo(codigo);
		
		if(dir != null) {
			BeanUtils.copyProperties(direcao, dir, "codigo");
			Veiculo ve = veiculoRepository.getByPlaca(direcao.getPlaca());
			Motorista mo = motoristaRepository.getByCPF(direcao.getCpf());
			
			if(mo == null)
				throw new MotoristaInexistenteException();
			
			if(ve == null)
				throw new VeiculoInexistenteException();
		}
		
		return direcaoRepository.save(dir);
	}
	
	public Direcao salvar(Direcao direcao) {
		Veiculo ve = veiculoRepository.getByPlaca(direcao.getPlaca());
		Motorista mo = motoristaRepository.getByCPF(direcao.getCpf());
		
		if(mo == null)
			throw new MotoristaInexistenteException();
		
		if(ve == null)
			throw new VeiculoInexistenteException();
		
		return direcaoRepository.save(direcao);
	}
	
	public void remover(Long codigo) {
		direcaoRepository.deleteById(codigo);
	}
	
	/**Busca motoristas que dirigiram carros no periodo.
	 * Esse método não verifica se a dataI é menor que dataF, deixando por sua responsabilidade essa verificação
	 * 
	 * @param dataI data no formato yyyy-MM-dd
	 * @param dataF data no formato yyyy-MM-dd
	 * 
	 * @return um List<Direcao> com direções entre as datas recebidas
	 * @throws ParseException caso as datas não estejam em formato válido
	 */
	public List<Direcao> getDirecoes(String dataI, String dataF) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		Date dataInicio = sdf.parse(dataI);
		Date dataFim = sdf.parse(dataF);
		
		return direcaoRepository.findGuidance(dataInicio, dataFim);
	}
	
	public ByteArrayInputStream reportDirecoesPDF(String dataI, String dataF) throws DocumentException, IOException, ParseException {
		 List<Direcao> direcoes = getDirecoes(dataI, dataF);
		 SimpleDateFormat sdf_f = new SimpleDateFormat("yyyy-MM-dd");
		
		 Document document = new Document();
		 ByteArrayOutputStream out = new ByteArrayOutputStream();
		 
		 Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
		 
		 PdfPTable table = new PdfPTable(3);
		 table.setWidthPercentage(80);
		 
		 PdfUtils.mkTabelaHead(table, "Placa", "CPF", "Inicio na direcao");
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for(Direcao d : direcoes) {
       	 PdfUtils.addTabelaCell(table, d.getPlaca(), d.getCpf(), sdf.format(d.getInicioDirecao()) );
        }
		 
        Paragraph preface = new Paragraph();
	    String title = String.format("Motoristas na direcao entre %s - %s",
	    	 sdf.format( sdf_f.parse(dataI) ), sdf.format( sdf_f.parse(dataF) ));
	    preface.add(new Paragraph(title, catFont));
        
	    PdfWriter.getInstance(document, out);
	    document.open();
	        
	    document.add(preface);
	    document.add(new Paragraph(" "));
	    document.add(new Paragraph(" "));
	    document.add(table);
	        
	    document.close();
       
		return new ByteArrayInputStream(out.toByteArray());
	}
	
}
