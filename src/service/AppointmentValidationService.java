package src.service;

import java.time.LocalDateTime;
import java.util.List;
import src.model.Appointment;

public class AppointmentValidationService {
    
    public static class ValidationResult {
        public final boolean isValid;
        public final String message;

        public ValidationResult(boolean isValid, String message) {
            this.isValid = isValid;
            this.message = message;
        }

        public static ValidationResult valid() {
            return new ValidationResult(true, "");
        }

        public static ValidationResult invalid(String message) {
            return new ValidationResult(false, message);
        }
    }


    public static ValidationResult validateStatusTransition(Appointment appointment, Appointment.Status fromStatus, Appointment.Status toStatus) {

        // Rule 1: cannot go backwards from Attended to Missed
        if (fromStatus == Appointment.Status.ATTENDED || fromStatus == Appointment.Status.MISSED) {
            if (toStatus != fromStatus) {
                return ValidationResult.invalid("Cannot move apointment after it has been " + 
                    fromStatus.toString().toLowerCase() + ".");
            }
        }       

        // Rule 2: Cannot skip directly to Attended/Missed without being Scheduled first
        if ((toStatus == Appointment.Status.ATTENDED || toStatus == Appointment.Status.MISSED) &&
            fromStatus != Appointment.Status.SCHEDULED) {
                return ValidationResult.invalid("Appointment must be scheduled before marking as " + 
                toStatus.toString().toLowerCase() + ".");
            }

        // Rule 3: Check for past appointment dates (only if already scheduled)
        if (toStatus == Appointment.Status.SCHEDULED && appointment.getDateTime() != null &&
            appointment.getLocation() != null && !appointment.getLocation().isEmpty()) {
            if (appointment.getDateTime().isBefore(LocalDateTime.now())) {
                return ValidationResult.invalid("Cannot schedule appointment in the past.");
            }
        }

        return ValidationResult.valid();
    }

    /*
     * Check for appointment conflicts (same patient, overlapping times)
     */
    public static ValidationResult validateTimeConflict(Appointment newAppointment, List<Appointment> existingAppointments) {
        if (newAppointment.getDateTime() == null || newAppointment.getLocation() == null) {
            return ValidationResult.valid(); // Unscheduled appointments do not conflict
        }

        for (Appointment existing : existingAppointments) {
            if (existing == newAppointment) continue;
            if (existing.getDateTime() == null) continue;
                
            // Check if appointments overlap (assuming 1 hour duration)
            LocalDateTime newStart = newAppointment.getDateTime();
            LocalDateTime newEnd = newStart.plusHours(1);
            LocalDateTime existingStart = existing.getDateTime();
            LocalDateTime existingEnd = existingStart.plusHours(1);

            boolean overlaps = newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart);

            if (overlaps) {
                return ValidationResult.invalid("Time conflict with existing " + existing.getSpecialty() +
                    " appointment at " + existing.getFormattedDateTime());
            }
        }

        return ValidationResult.valid();
    }
}