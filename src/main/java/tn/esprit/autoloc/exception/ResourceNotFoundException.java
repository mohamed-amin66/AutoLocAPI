package tn.esprit.autoloc.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String ressource, String id) {
        super(ressource + " introuvable avec l'identifiant " + id);
    }
}