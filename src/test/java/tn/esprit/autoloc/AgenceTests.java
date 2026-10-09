package tn.esprit.autoloc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IAgenceRepository;
import org.springframework.data.domain.Sort;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Fail.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class AgenceTests {
    @Autowired
    private AgenceRepositoryMock basicAgenceRepository;
    @Autowired
    private IAgenceRepository fullAgenceRepository;
    public void addAgence(CrudRepository<Agence, Long> repository){
        Agence a = new Agence();
        a.setNom("Agence ariana");
        a.setAdresse("1 Rue Hedi");
        a.setVille("Tunis");
        a.setTelephone("71585874");
        int ms = (int)System.currentTimeMillis();
        Vehicule v1 = new Vehicule() ;
        v1.setCategorie(CategorieVehicule.SUV);
        v1.setImmatriculation("785414TU96" + ms);
        v1.setMarque("Isuzu");
        v1.setModele("DMax");
        v1.setStatut(StatutVehicule.EN_MAINTENANCE);
        v1.setTarifJournalier(new BigDecimal(100));

        Vehicule v2 = new Vehicule();
        v2.setCategorie(CategorieVehicule.UTILITAIRE);
        v2.setImmatriculation("785414TU95" + ms);
        v2.setMarque("Toyota");
        v2.setModele("Yaris");
        v2.setStatut(StatutVehicule.DISPONIBLE);
        v2.setTarifJournalier(new BigDecimal(80));

        a.setVehicules(Set.of(v1,v2));
        repository.save(a);
    }
    private void loadAgence(CrudRepository<Agence, Long> repository,
                            String repositoryName) {
        Iterable<Agence> agences = repository.findAll();

        StringBuilder sb = new StringBuilder();
        sb.append("===== ").append(repositoryName).append(" =====\n");

        for (Agence agence : agences) {
            sb.append(agence.getIdAgence())
                    .append(" | ")
                    .append(agence.getNom())
                    .append("\n");

            Set<Vehicule> vehicules = agence.getVehicules();
            int nb = (vehicules == null) ? 0 : vehicules.size();

            sb.append("Nombre de véhicules : ")
                    .append(nb)
                    .append("\n");

            if (vehicules != null) {
                for (Vehicule v : vehicules) {
                    sb.append("   ")
                            .append(v.getIdVehicule())
                            .append(" | ")
                            .append(v.getImmatriculation())
                            .append("\n");
                }
            }
        }

        System.out.println(sb);
    }
    @Test
    public void basicAddAgence() {
        addAgence(basicAgenceRepository);
    }

    @Test
    public void fullAddAgence() {
        addAgence(fullAgenceRepository);
    }
    @Test
    public void basicLoadAgence() {
        loadAgence(basicAgenceRepository, "Basic Repository Mock");
    }

    @Test
    public void fullLoadAgence() {
        loadAgence(fullAgenceRepository, "Full Repository");
    }
    @Test
    public void loadSortedAgences() {
        List<Agence> agences = fullAgenceRepository.findAll(
                Sort.by(Sort.Direction.DESC, "idAgence")
        );

        System.out.println("===== Agences triées par ID décroissant =====");

        for (Agence agence : agences) {
            System.out.println(
                    agence.getIdAgence() + " | " +
                            agence.getNom() + " | " +
                            agence.getAdresse() + " | " +
                            agence.getVille() + " | " +
                            agence.getTelephone()
            );
        }
    }
    @Test
    public void loadPagedAgences() {
        Pageable pageable = PageRequest.of(
                0,
                2,
                Sort.by(Sort.Direction.DESC, "idAgence")
        );

        Page<Agence> page = fullAgenceRepository.findAll(pageable);

        System.out.println("===== Agences paginées =====");
        System.out.println("Nombre total de pages : " + page.getTotalPages());
        System.out.println("Page en cours : " + (page.getNumber() + 1));

        for (Agence agence : page.getContent()) {
            System.out.println(
                    agence.getIdAgence() + " | " +
                            agence.getNom() + " | " +
                            agence.getAdresse() + " | " +
                            agence.getVille() + " | " +
                            agence.getTelephone()
            );
        }
    }
}

interface AgenceRepositoryMock extends CrudRepository<Agence, Long>{

}