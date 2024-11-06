package com.lacouf.rsbjwt.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.access.method.P;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EvaluationStageProf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Professeur professeur;

    @ManyToOne
    private Etudiant etudiant;

    private String nomEntreprise;
    private String personneContact;
    private String adresse;
    private String ville;
    private String codePostal;
    private String telephone;
    private String telecopieur;

    // Identification du stagiaire
    private String nomStagiaire;
    private String dateStage;
    private int numeroStage;

    // Évaluation des tâches
    private EvaluationConformite tachesConformite;
    private EvaluationConformite accueilIntegration;
    private EvaluationConformite encadrementSuffisant;
    private int heuresEncadrementPremierMois;
    private int heuresEncadrementDeuxiemeMois;
    private int heuresEncadrementTroisiemeMois;

    private EvaluationConformite respectNormesHygiene;
    private EvaluationConformite climatDeTravail;
    private EvaluationConformite accesTransportCommun;
    private EvaluationConformite salaireInteressant;
    private double salaireHoraire;
    private EvaluationConformite communicationSuperviseur;
    private EvaluationConformite equipementAdequat;
    private EvaluationConformite volumeTravailAcceptable;

    // Observations générales
    private boolean privilegiePremierStage;
    private boolean privilegieDeuxiemeStage;
    private int nombreStagiairesAccueillis;
    private boolean souhaiteRevoirStagiaire;
    private boolean offreQuartsVariables;
    private String horairesQuartsDeTravail;

    // Commentaires et date
    private String commentaires;
    private String signatureEnseignant;
    private String dateSignature;



    public enum EvaluationConformite {
        TOTAL_EN_ACCORD,
        PLUTOT_EN_ACCORD,
        PLUTOT_EN_DESACCORD,
        TOTAL_EN_DESACCORD,
        IMPOSSIBLE_SE_PRONONCER
    }
}
