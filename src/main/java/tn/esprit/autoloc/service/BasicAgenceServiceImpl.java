package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.Utils;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicAgenceServiceImpl implements IAgenceService {
    private final IAgenceRepository Repository;

    @Override
    public boolean check(Agence agence, boolean isNew) {
        return Utils.isNotEmpty(agence.getNom()) && Utils.isNotEmpty(agence.getTelephone());
    }

    @Override
    public Agence create(Agence agence) {
        if(agence.getIdAgence() != null){
            throw new IllegalArgumentException("identifient de l'agence doit etre null !!!");
        }
        if(!check(agence,true)){
            throw new IllegalArgumentException("champs obligatoires !!!");
        }
        return Repository.save(agence);
    }

    @Override
    public Agence findById(Long id) {
        return Repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Agence",id.toString()));
    }

    public List<Agence> findAll() {
        return Repository.findAll();
    }

    @Override
    public Agence update(Long id, Agence agence) {
        Agence existante = findById(id);

        if (!check(agence, false)) {
            throw new IllegalArgumentException("champs obligatoires !!!");
        }

        existante.setNom(agence.getNom());
        existante.setTelephone(agence.getTelephone());
        existante.setAdresse(agence.getAdresse());
        existante.setVille(agence.getVille());

        return Repository.save(existante);
    }

    @Override
    public void deleteById(Long id) {
        if (!Repository.existsById(id)) {
            throw new ResourceNotFoundException("Agence", id.toString());
        }
        Repository.deleteById(id);
    }

    @Override
    public boolean hasVehicules(Long id) {
        Agence agence = findById(id);
        return agence.getVehicules() != null && !agence.getVehicules().isEmpty();
    }

}