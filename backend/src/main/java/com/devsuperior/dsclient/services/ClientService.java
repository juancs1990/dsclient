package com.devsuperior.dsclient.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dsclient.DTO.ClientDTO;
import com.devsuperior.dsclient.entities.Client;
import com.devsuperior.dsclient.exceptions.DatabaseExceptionHandler;
import com.devsuperior.dsclient.exceptions.ResourceNotFoundException;
import com.devsuperior.dsclient.repositories.ClientRepository;

@Service
public class ClientService {
	
	@Autowired
	ClientRepository repository;
	
	@Transactional(readOnly = true)
	public Page<ClientDTO> findAllPaged(PageRequest pageRequest){
		Page<Client> list = repository.findAll(pageRequest);
		return  list.map(x -> new ClientDTO(x));
	}

	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		 Optional<Client> obj = repository.findById(id);
		 Client entity = obj.orElseThrow(()-> new ResourceNotFoundException("Entity Not Found")) ;
		 return new ClientDTO(entity);
	}
	
	@Transactional
	public ClientDTO insert(ClientDTO dto) {
		Client entity = new Client();
		clientToDto(dto,entity);
		entity = repository.save(entity);
		return new ClientDTO(entity);
		
	}
	
	
	@Transactional
	public ClientDTO update(ClientDTO dto, Long id) {
		try {
		Client entity = repository.getOne(id);
		clientToDto(dto,entity);
		entity = repository.save(entity);
		return new ClientDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id Not Found: "+id);
		 
		}
		 
	}

	public void delete(Long id) {
		try {
		repository.deleteById(id);
		} catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id Not Found: "+id);
			
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseExceptionHandler("Id Not Found: "+id);
			
		}
	}
	
	public void clientToDto (ClientDTO dto, Client entity) {
		entity.setBirthDate(dto.getBirthDate());
		entity.setChildren(dto.getChildren());
		entity.setCpf(dto.getCpf());
		entity.setIncome(dto.getIncome());
		entity.setName(dto.getName());	
		
      
		
	}

}
