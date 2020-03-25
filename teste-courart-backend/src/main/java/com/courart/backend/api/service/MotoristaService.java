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

import com.courart.backend.api.model.Motorista;
import com.courart.backend.api.repository.MotoristaRepository;
import com.courart.backend.api.service.exception.MotoristaInexistenteException;
import com.courart.backend.api.utils.PdfUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class MotoristaService {

	@Autowired
	private MotoristaRepository motoristaRepository;
	
	public List<Motorista> buscar() {
		List<Motorista> motos = null;
		
		try {
			motos = motoristaRepository.findAll();
		} catch (Exception e) {
			throw new EmptyResultDataAccessException(1);
		}
		
		return motos;
	}
	
	public Motorista buscarCodigo(Long codigo) {
		Motorista moto = null;
		
		try {
			moto = motoristaRepository.findById(codigo).get();
		} catch (Exception e) {
			throw new EmptyResultDataAccessException(1);
		}
		
		return moto;
	}
	
	public List<Motorista> buscarCPF(String cpf) {
		List<Motorista> moto = null;
		
		try {
			moto = motoristaRepository.findByCPF(cpf);
		} catch (Exception e) {
			throw new EmptyResultDataAccessException(1);
		}
		
		return moto;
	}
	
	/**Busca pelo motorista que tenha esse cpf
	 * 
	 * @param cpf
	 * @return Retorna o motorista com exatamente esse cpf ou null
	 */
	public Motorista getCPF(String cpf) {
		Motorista moto = null;
		
		try {
			moto = motoristaRepository.getByCPF(cpf);
		} catch (Exception e) {
			throw new EmptyResultDataAccessException(1);
		}
		
		return moto;
	}
	
	public List<Motorista> buscarNome(String nome) {
		List<Motorista> motos = null;
		
		try {
			motos = motoristaRepository.findByNome(nome);
		} catch (Exception e) {
			throw new EmptyResultDataAccessException(1);
		}
		
		return motos;
	}
	
	public Motorista atualizar(Long codigo, Motorista motorista) {
		Motorista moto = buscarCodigo(codigo);
		
		if(moto != null)
			BeanUtils.copyProperties(motorista, moto, "codigo");
		else
			throw new MotoristaInexistenteException();
		
		return motoristaRepository.save(moto);
	}
	
	public Motorista salvar(Motorista motorista) {
		return motoristaRepository.save(motorista);
	}
	
	public void remover(Long codigo) {
		motoristaRepository.deleteById(codigo);;
	}
	
	/**Busca motoristas que nasceram entre as datas recebidas.
	 * Esse método não verifica se a dataI é menor que dataF, deixando por sua responsabilidade essa verificação
	 * 
	 * @param dataI data no formato yyyy-MM-dd
	 * @param dataF data no formato yyyy-MM-dd
	 * 
	 * @return um List<Motorista> com dataNascimento entre as datas recebidas
	 * @throws ParseException caso as datas não estejam em formato válido
	 */
	public List<Motorista> getAniversariantes(String dataI, String dataF) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		Date dataInicio = sdf.parse(dataI);
		Date dataFim = sdf.parse(dataF);
		
		return motoristaRepository.findBirthdays(dataInicio, dataFim);
	}
	
	public ByteArrayInputStream reportAniversariantesPDF(String dataI, String dataF) throws DocumentException, IOException, ParseException {
		 List<Motorista> motoristas = getAniversariantes(dataI, dataF);
		 SimpleDateFormat sdf_f = new SimpleDateFormat("yyyy-MM-dd");
		
		 Document document = new Document();
		 ByteArrayOutputStream out = new ByteArrayOutputStream();
		 
		 Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
		 
		 PdfPTable table = new PdfPTable(4);
		 table.setWidthPercentage(80);
		 
		 PdfUtils.mkTabelaHead(table, "Nome", "CPF", "Nascimento", "Login");
         
         SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
         for(Motorista m : motoristas) {
        	 PdfUtils.addTabelaCell(table, m.getNome(), m.getCpf(),
        			 sdf.format(m.getDataNascimento()), m.getLogin());
         }
		 
         Paragraph preface = new Paragraph();
	     String title = String.format("Motoristas aniversariantes entre %s - %s",
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
