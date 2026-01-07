package fr.cnamts.cpam33.ordonnance.infrastructure.out.acls.hl7;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v25.message.ORM_O01;
import ca.uhn.hl7v2.model.v25.segment.ORC;
import ca.uhn.hl7v2.model.v25.segment.PID;
import ca.uhn.hl7v2.model.v25.segment.PV1;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.ExportMedecinDto;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.ExportPatientDto;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HL7PatientMedecinMapper {

    public ORM_O01 buildMessage(
            ExportPatientDto patient,
            ExportMedecinDto medecin
    ) throws HL7Exception, IOException {
        ORM_O01 message = new ORM_O01();
        message.initQuickstart("ORM", "O01", "P");
        mapPatient(message.getPATIENT().getPID(), patient);
        mapMedecin(message.getPATIENT().getPATIENT_VISIT().getPV1(), medecin);
        mapPrescripteur(message.getORDER().getORC(), medecin);
        return message;
    }

    private void mapPatient(PID pid, ExportPatientDto patient) throws DataTypeException {
        pid.getPatientIdentifierList(0)
                .getIDNumber()
                .setValue(patient.ins());
        pid.getPatientName(0)
                .getFamilyName()
                .getSurname()
                .setValue(patient.nom());
        pid.getPatientName(0)
                .getGivenName()
                .setValue(patient.prenom());
        pid.getDateTimeOfBirth()
                .getTime()
                .setValue(patient.dateNaissance().toString().replace("-", ""));
        pid.getAdministrativeSex()
                .setValue(patient.sexe());
    }

    private void mapMedecin(PV1 pv1, ExportMedecinDto medecin)
            throws DataTypeException {
        pv1.getAttendingDoctor(0)
                .getIDNumber()
                .setValue(medecin.rpps());
        pv1.getAttendingDoctor(0)
                .getFamilyName()
                .getSurname()
                .setValue(medecin.nom());
        pv1.getAttendingDoctor(0)
                .getGivenName()
                .setValue(medecin.prenom());
    }

    private void mapPrescripteur(ORC orc, ExportMedecinDto medecin)
            throws DataTypeException {
        orc.getOrderingProvider(0)
                .getIDNumber()
                .setValue(medecin.rpps());
        orc.getOrderingProvider(0)
                .getFamilyName()
                .getSurname()
                .setValue(medecin.nom());
        orc.getOrderingProvider(0)
                .getGivenName()
                .setValue(medecin.prenom());
    }

}
