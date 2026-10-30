package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Agence;

import java.util.List;

public interface IAgenceService extends IGenericService<Agence, Long> {
    boolean hasVehicules(Long id);
}