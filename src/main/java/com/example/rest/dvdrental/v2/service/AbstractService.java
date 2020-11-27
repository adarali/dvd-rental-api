package com.example.rest.dvdrental.v2.service;

import com.example.rest.dvdrental.v2.entities.AbstractEntity;
import com.example.rest.dvdrental.v2.exceptions.AppException;
import com.example.rest.dvdrental.v2.exceptions.AppValidationException;
import com.example.rest.dvdrental.v2.model.ValidationMessage;
import com.example.rest.dvdrental.v2.repository.GenericRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractService<E extends AbstractEntity<ID>, ID extends Serializable> {
	
	@Autowired
	Validator validator;
	
	public List<E> findAll() {
		return getRepo().findAll();
	}
	
	@Transactional(readOnly=true)
	public E find(ID id) {
		if (id == null) {
			throw new AppException("The id was not specified");
		}
		return getRepo().findById(id).orElse(null);
	}
	
	public E save(E entity) {
		return getRepo().save(entity);
	}
	
	public void saveAll(List<E> entities) {
		getRepo().saveAll(entities);
	}
	
	public void deleteById(ID id) {
		getRepo().deleteById(id);
	}
	
	public void delete(E entity) {
		getRepo().delete(entity);
	}
	
	public E create(E entity) {
		return save(entity);
	}
	
	public E update(E entity) {
		return save(entity);
	}
	
	public E createOrUpdate(E entity) {
		return entity.getId() == null ? create(entity) : update(entity);
	}
	
	protected abstract GenericRepository<E, ID> getRepo();
	
	protected void validate(E entity) {
		validate(entity, new ArrayList<>());
	}
	
	protected void validate(E entity, List<ValidationMessage> validationMessageList) {
		val violations = validator.validate(entity);
		
		for (ConstraintViolation<E> violation : violations) {
			validationMessageList.add(new ValidationMessage(violation.getPropertyPath().toString(), violation.getMessage()));
		}
		
		new AppValidationException(validationMessageList).throwMe();
		
	}
	
}
