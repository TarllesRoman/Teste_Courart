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

import com.courart.backend.api.model.Veiculo;
import com.courart.backend.api.repository.VeiculoRepository;
import com.courart.backend.api.service.exception.VeiculoInexistenteException;
import com.courart.backend.api.utils.PdfUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class VeiculoService {

	@Autowired
	private VeiculoRepository veiculoRepository;
	
	public List<Veiculo> buscar() {
		List<Veiculo> veis = null;
		
		try {
			veis = veiculoRepository.findAll();
		} catch (Exception e) {
			throw new EmptyResultDataAccessException(1);
		}
		
		return veis;
	}
	
	public Veiculo buscarCodigo(Long codigo) {
		Veiculo vei = null;
		
		try {
			vei = veiculoRepository.findById(codigo).get();
		} catch (Exception e) {
			throw new EmptyResultDataAccessException(1);
		}
		
		return vei;
	}
	
	public List<Veiculo> buscarPlaca(String placa) {
		List<Veiculo> vei = null;
		
		try {
			vei = veiculoRepository.findByPlaca(placa);
		} catch (Exception e) {
			throw new EmptyResultDataAccessException(1);
		}
		
		return vei;
	}
	
	/**Busca o veículo com a placa
	 * 
	 * @param placa
	 * @return Retorna o veiculo com a placa ou null se não encontrar
	 */
	public Veiculo getPlaca(String placa) {
		Veiculo vei = null;
		
		try {
			vei = veiculoRepository.getByPlaca(placa);
		} catch (Exception e) {
			throw new EmptyResultDataAccessException(1);
		}
		
		return vei;
	}
	
	public List<Veiculo> buscarModelo(String modelo) {
		List<Veiculo> veis = null;
		
		try {
			veis = veiculoRepository.findByModelo(modelo);
		} catch (Exception e) {
			throw new EmptyResultDataAccessException(1);
		}
		
		return veis;
	}
	
	public Veiculo atualizar(Long codigo, Veiculo veiculo) {
		Veiculo vei = buscarCodigo(codigo);
		
		if(vei != null) {
			BeanUtils.copyProperties(veiculo, vei, "codigo");
		
			if(!vei.getAtivo())
				vei.setDataDesativacao(new java.sql.Date(new Date().getTime()));
		}else
			throw new VeiculoInexistenteException();
		
		return veiculoRepository.save(vei);
	}
	
	public void atualizarAtivo(Long codigo, Boolean ativo) {
		Veiculo salvo = buscarCodigo(codigo);
		salvo.setDataDesativacao(new java.sql.Date(new Date().getTime()));
		
		if(salvo != null)
			salvo.setAtivo(ativo);
		
		veiculoRepository.save(salvo);
	}
	
	public Veiculo salvar(Veiculo veiculo) {
		return veiculoRepository.save(veiculo);
	}
	
	public void remover(Long codigo) {
		veiculoRepository.deleteById(codigo);
	}
	
	/**Busca veiculos que foram cadastrados(ativados) no periodo.
	 * Esse método não verifica se a dataI é menor que dataF, deixando por sua responsabilidade essa verificação.
	 * 
	 * @param dataI data no formato yyyy-MM-dd
	 * @param dataF data no formato yyyy-MM-dd
	 * 
	 * @return um List<Veiculo> com veiculos ativados entre as datas recebidas
	 * @throws ParseException caso as datas não estejam em formato válido
	 */
	public List<Veiculo> getAtivados(String dataI, String dataF) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		Date dataInicio = sdf.parse(dataI);
		Date dataFim = sdf.parse(dataF);
		
		return veiculoRepository.findAtivados(dataInicio, dataFim);
	}
	
	public ByteArrayInputStream reportAtivadosPDF(String dataI, String dataF) throws DocumentException, IOException, ParseException {
		List<Veiculo> veiculos = getAtivados(dataI, dataF);
		SimpleDateFormat sdf_f = new SimpleDateFormat("yyyy-MM-dd");
		
		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		 
		Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
		 
		PdfPTable table = new PdfPTable(8);
		table.setWidthPercentage(100);
		 
		PdfUtils.mkTabelaHead(table, "Placa", "Ativo", "Cadastrado", "Desativado",
				 					"Modelo", "Cor", "Consumo","Passageiros");
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String desativado, cadastrado;
        for(Veiculo v: veiculos) {
        	desativado =  v.getAtivo()? "-": sdf.format(v.getDataDesativacao());
        	cadastrado = sdf.format(v.getDataCadastro());
       	 	PdfUtils.addTabelaCell(table, v.getPlaca(), v.getAtivo().toString(),
       			 cadastrado, desativado, v.getModelo(), v.getCor(), v.getConsumoMedio().toString(), v.getQtdPassageiros().toString());
        }
		 
        Paragraph preface = new Paragraph();
	    String title = String.format("Veiculos ativados entre %s - %s",
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
