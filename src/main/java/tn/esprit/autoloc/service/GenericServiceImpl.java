package tn.esprit.autoloc.service;

import org.springframework.data.repository.CrudRepository;
import tn.esprit.autoloc.exception.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;

public abstract class GenericServiceImpl<T, ID> implements IGenericService<T, ID> {
    protected abstract String getEntityName();
    protected abstract ID getId(T entity);
    protected abstract void setFields(T source, T target);

    protected final CrudRepository<T, ID> repository;

    protected GenericServiceImpl(CrudRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    public T create(T entity) {
        if (getId(entity) != null) {
            throw new IllegalArgumentException(
                    "identifiant de " + getEntityName() + " doit etre null !!!");
        }
        if (!check(entity, true)) {
            throw new IllegalArgumentException("champs obligatoires !!!");
        }
        return repository.save(entity);
    }

    @Override
    public T findById(ID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getEntityName(), id.toString()));
    }

    @Override
    public List<T> findAll() {
        List<T> result = new ArrayList<>();
        repository.findAll().forEach(result::add);
        return result;
    }

    @Override
    public T update(ID id, T entity) {
        // 1. Récupère l'entité existante (exception si introuvable)
        T existante = findById(id);

        // 2. Vérifie les champs obligatoires
        if (!check(entity, false)) {
            throw new IllegalArgumentException("champs obligatoires !!!");
        }

        // 3. Copie tous les champs, puis enregistre
        setFields(entity, existante);
        return repository.save(existante);
    }

    @Override
    public void deleteById(ID id) {
        // existsById : on ne charge pas les données
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(getEntityName(), id.toString());
        }
        repository.deleteById(id);
    }
}