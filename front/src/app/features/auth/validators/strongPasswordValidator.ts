import { AbstractControl, ValidationErrors } from '@angular/forms';

export function strongPasswordValidator(control: AbstractControl): ValidationErrors | null {
  const value = control.value;
  const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).{8,}$/;

  if (!value) return null;
  return regex.test(value) ? null : { strongPassword: true };
}