package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.Employeur;
import com.lacouf.rsbjwt.model.Entrevue;
import com.lacouf.rsbjwt.model.Etudiant;
import com.lacouf.rsbjwt.model.OffreDeStage;
import com.lacouf.rsbjwt.repository.*;
import com.lacouf.rsbjwt.service.dto.EmployeurDTO;
import com.lacouf.rsbjwt.service.dto.EntrevueDTO;
import com.lacouf.rsbjwt.service.dto.EtudiantDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeurService {

    private final EmployeurRepository employeurRepository;
    private final EntrevueRepository entrevueRepository;
    private final OffreDeStageRepository offreDeStageRepository;
    private final UserAppRepository userAppRepository;
    private final EtudiantRepository etudiantRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeurService(EmployeurRepository employeurRepository, PasswordEncoder passwordEncoder, EntrevueRepository entrevueRepository,UserAppRepository userAppRepository, OffreDeStageRepository offreDeStageRepository, EtudiantRepository etudiantRepository) {
        this.employeurRepository = employeurRepository;
        this.passwordEncoder = passwordEncoder;
        this.userAppRepository = userAppRepository;
        this.entrevueRepository = entrevueRepository;
        this.offreDeStageRepository = offreDeStageRepository;
        this.etudiantRepository = etudiantRepository;
    }

    public Optional<EmployeurDTO> creerEmployeur(EmployeurDTO employeurDTO) {
        try {
            String encodedPassword = passwordEncoder.encode(employeurDTO.getCredentials().getPassword());
            Employeur employeur = new Employeur(
                    employeurDTO.getFirstName(),
                    employeurDTO.getLastName(),
                    employeurDTO.getCredentials().getEmail(),
                    encodedPassword,
                    employeurDTO.getPhoneNumber(),
                    employeurDTO.getEntreprise()
            );
            Employeur savedEmployeur = employeurRepository.save(employeur);
            return Optional.of(new EmployeurDTO(savedEmployeur));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<EmployeurDTO> getEmployeurById(Long id) {
        return employeurRepository.findById(id)
                .map(EmployeurDTO::new);
    }


    public Optional<Employeur> findByCredentials_Email(String email) {
        return employeurRepository.findByCredentials_email(email);
    }

    public Optional<EntrevueDTO> createEntrevue(EntrevueDTO entrevueDTO, String email, Long offreId) {
        try {
            Etudiant etudiant = userAppRepository.findUserAppByEmail(email)
                    .map(userApp -> (Etudiant) userApp)
                    .orElseThrow(() -> new Exception("Etudiant non trouvé"));

            OffreDeStage offreDeStage = offreDeStageRepository.findById(offreId)
                    .orElseThrow(() -> new Exception("Offre de stage non trouvée"));

            boolean hasInterview = entrevueRepository.existsByEtudiantAndOffreDeStage(etudiant, offreDeStage);
            if (hasInterview) {
                throw new Exception("L'étudiant a déjà une entrevue programmée pour cette offre.");
            }

            Entrevue entrevue = new Entrevue(
                    entrevueDTO.getDateHeure(),
                    entrevueDTO.getLocation(),
                    entrevueDTO.getStatus(),
                    etudiant,
                    offreDeStage
            );
            Entrevue savedEntrevue = entrevueRepository.save(entrevue);
            return Optional.of(new EntrevueDTO(savedEntrevue));
        } catch (Exception e) {
            System.err.println("Error creating interview: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<EntrevueDTO> getEntrevueById(Long id) {
        return entrevueRepository.findById(id)
                .map(EntrevueDTO::new);
    }

    public List<EntrevueDTO> getAllEntrevues() {
        return entrevueRepository.findAll().stream()
                .map(EntrevueDTO::new)
                .toList();
    }

    public List<EntrevueDTO> getEntrevuesByOffre(Long offreId) {
        return entrevueRepository.findByOffreDeStageId(offreId).stream()
                .map(EntrevueDTO::new)
                .toList();
    }

    public List<EntrevueDTO> getEntrevuesAccepteesByOffre(Long offreId) {
        return entrevueRepository.findByOffreDeStageIdAndStatus(offreId, "accepter").stream()
                .map(EntrevueDTO::new)
                .toList();
    }

    public Optional<EntrevueDTO> accepterCandidature(Long entrevueId) {
        return entrevueRepository.findById(entrevueId)
                .map(entrevue -> {
                    entrevue.accepterEntrevue();
                    entrevueRepository.save(entrevue);
                    return new EntrevueDTO(entrevue);
                });
    }

    public Optional<EntrevueDTO> refuserCandidature(Long entrevueId) {
        return entrevueRepository.findById(entrevueId)
                .map(entrevue -> {
                    entrevue.refuserEntrevue();
                    entrevueRepository.save(entrevue);
                    return new EntrevueDTO(entrevue);
                });
    }

}
