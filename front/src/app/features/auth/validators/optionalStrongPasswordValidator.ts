import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export function optionalStrongPasswordValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value as string | null;
    if (!value || value.trim() === '') {
      // vide => valide (champ optionnel)
      return null;
    }
    // >=8 + minuscule + majuscule + chiffre + caractère spécial
    const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).{8,}$/;
    return regex.test(value) ? null : { strongPassword: true };
  };
}