package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.CV;
import com.lacouf.rsbjwt.model.Etudiant;
import com.lacouf.rsbjwt.model.OffreDeStage;
import com.lacouf.rsbjwt.model.UserApp;
import com.lacouf.rsbjwt.repository.CVRepository;
import com.lacouf.rsbjwt.repository.EtudiantRepository;
import com.lacouf.rsbjwt.repository.OffreDeStageRepository;
import com.lacouf.rsbjwt.repository.UserAppRepository;
import com.lacouf.rsbjwt.service.dto.CVDTO;
import com.lacouf.rsbjwt.service.dto.EtudiantDTO;
import com.lacouf.rsbjwt.service.dto.OffreDeStageDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EtudiantService {

    private final UserAppRepository userAppRepository;
    private final EtudiantRepository etudiantRepository;
    private final PasswordEncoder passwordEncoder;
    private final CVRepository cvRepository;
    private final OffreDeStageRepository offreDeStageRepository;

    public EtudiantService(UserAppRepository userAppRepository, EtudiantRepository etudiantRepository, PasswordEncoder passwordEncoder, CVRepository cvRepository, OffreDeStageRepository offreDeStageRepository) {
        this.userAppRepository = userAppRepository;
        this.etudiantRepository = etudiantRepository;
        this.passwordEncoder = passwordEncoder;
        this.cvRepository = cvRepository;
        this.offreDeStageRepository = offreDeStageRepository;
    }

    public Optional<EtudiantDTO> creerEtudiant(EtudiantDTO etudiantDTO) {
        try {
            String encodedPassword = passwordEncoder.encode(etudiantDTO.getCredentials().getPassword());
            Etudiant etudiant = new Etudiant(
                    etudiantDTO.getFirstName(),
                    etudiantDTO.getLastName(),
                    etudiantDTO.getCredentials().getEmail(),
                    encodedPassword,
                    etudiantDTO.getPhoneNumber(),
                    etudiantDTO.getDepartement()
            );
            Etudiant savedEtudiant = etudiantRepository.save(etudiant);
            return Optional.of(new EtudiantDTO(savedEtudiant));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<EtudiantDTO> getEtudiantById(Long id) {
        return etudiantRepository.findById(id)
                .map(EtudiantDTO::new);
    }

    public Optional<CVDTO> creerCV(CVDTO cvDTO, String email) {
        try {
            CV cv = new CV(
                    cvDTO.getName(),
                    cvDTO.getType(),
                    cvDTO.getData(),
                    cvDTO.getStatus()
            );

            CV savedCV = cvRepository.save(cv);
            Etudiant etudiant = userAppRepository.findUserAppByEmail(email)
                    .map(userApp -> (Etudiant) userApp)
                    .get();
            etudiant.setCv(savedCV);
            etudiantRepository.save(etudiant);

            return Optional.of(new CVDTO(savedCV));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void supprimerCV(Long id) {
        cvRepository.deleteById(id);
    }

    public Optional<EtudiantDTO> getEtudiantByEmail(String email) {
        Optional<UserApp> utilisateur = userAppRepository.findUserAppByEmail(email);

        if (utilisateur.isEmpty()) {
            return Optional.empty();
        }
        Etudiant etudiant = (Etudiant) utilisateur.get();

        System.out.println(etudiant);
        return Optional.of(new EtudiantDTO(etudiant));
    }

    public Iterable<EtudiantDTO> getAllEtudiants() {
        return etudiantRepository.findAll().stream()
                .map(EtudiantDTO::new)
                .toList();
    }

    public List<OffreDeStageDTO> getOffresApprouvees() {
        return offreDeStageRepository.findAll().stream()
                .filter(offreDeStage -> offreDeStage.getStatus().equals("Validé"))
                .map(OffreDeStageDTO::new)
                .toList();
    }

    public Optional<EtudiantDTO> ajouterOffreDeStage(String etudiantEmail, Long offreId) {
        Optional<Etudiant> etudiantOpt = Optional.ofNullable(etudiantRepository.findByEmail(etudiantEmail));
        Optional<OffreDeStage> offreOpt = offreDeStageRepository.findById(offreId);

        if (etudiantOpt.isPresent() && offreOpt.isPresent()) {
            Etudiant etudiant = etudiantOpt.get();
            OffreDeStage offre = offreOpt.get();

            etudiant.getOffresAppliquees().add(offre);

            etudiantRepository.save(etudiant);
            offreDeStageRepository.save(offre);

            return Optional.of(new EtudiantDTO(etudiant));
        }

        return Optional.empty();
    }

    public Iterable<OffreDeStageDTO> getOffresDeStage(String etudiantEmail) {
        return etudiantRepository.findByEmail(etudiantEmail)
                .getOffresAppliquees().stream()
                .map(OffreDeStageDTO::new)
                .toList();
    }

    public Optional<EtudiantDTO> retirerOffreDeStage (String email, Long offreId) {
        Optional<Etudiant> etudiantOpt = Optional.ofNullable(etudiantRepository.findByEmail(email));
        Optional<OffreDeStage> offreOpt = offreDeStageRepository.findById(offreId);

        if (etudiantOpt.isPresent() && offreOpt.isPresent()) {
            Etudiant etudiant = etudiantOpt.get();
            OffreDeStage offre = offreOpt.get();

            etudiant.getOffresAppliquees().remove(offre);

            etudiantRepository.save(etudiant);

            return Optional.of(new EtudiantDTO(etudiant));
        }

        return Optional.empty();
    }
}
